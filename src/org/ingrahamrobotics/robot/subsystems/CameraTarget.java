package org.ingrahamrobotics.robot.subsystems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.commands.CameraAnalyze;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.MeasurementType;
import com.ni.vision.NIVision.RGBValue;
import com.ni.vision.NIVision.Range;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class CameraTarget extends Subsystem {

	// Debug
	private static final boolean kDEBUG = true;

	// Image log
	public static boolean kENABLE_VISION_LOG = kDEBUG | true;
	public static final int kNUM_VISION_LOGS = 3;
	public static final String kDEFAULT_HOME = "/home/lvuser";
	public static final String kVISION_DIR_NAME = "vision";

	// Camera settings (some of these may need to be exposed on the dashboard)
	public static final int width = 640;
	public static final int height = 480;
	public static final int fps = 4;
	public static int brightness = -1; // 0 - 100, -1 is "do not set"
	public static int exposure = -1; // 0 - 100, -1 is "auto"
	public static int whitebalance = 4700; // Color temperature in K, -1 is auto

	// Analysis constants (not tunable without algorithm adjustments)
	public static final int kBINARY_COLOR = 255;
	public static final int kMIN_BLOB_AREA = 2500;
	public static final int kNOMINAL_BLOB_AREA = kMIN_BLOB_AREA * 4;

	// Structures
	public enum Confidence {
		kNONE(), kMINIMAL(), kNOMINAL();
	}

	public class TargetData {
		boolean valid = false;
		public Confidence confidence = Confidence.kNONE;
		public int distance = 0;
		public double azimuth = 0;
		public double altitude = 0;
		public long start = 0;
		public long end = 0;

		@Override
		public String toString() {
			return new String("V:" + valid + ";C:" + confidence + ";D" + distance + ";Z" + azimuth + ";L" + altitude);
		}

		public void save(Path file) {
			PrintWriter out;
			try {
				out = new PrintWriter(file.toFile());
			} catch (FileNotFoundException e) {
				System.err.println("Unable to save analysis file");
				return;
			}
			out.println(toString());
			out.close();
		}
	}

	// Members
	private USBCamera cam;
	private TargetData data;
	private String dirHome;
	private Path dirVision;

	// Subsystem control
	public CameraTarget() {
		super();
		data = new TargetData();
		dirHome = System.getProperty("user.home", kDEFAULT_HOME);
		dirVision = Paths.get(dirHome, kVISION_DIR_NAME);

		cleanVision();
	}

	public void cleanVision() {
		if (!kENABLE_VISION_LOG) {
			return;
		}

		for (int i = kNUM_VISION_LOGS; i > 0; i--) {
			Path dir = Paths.get(dirHome, kVISION_DIR_NAME + "." + i);

			// Skip missing folders
			if (Files.notExists(dir)) {
				continue;
			}

			// Die if there is an existing naming error
			if (!Files.isDirectory(dir)) {
				System.err.println("Existing non-directory vision log: " + dir.toString());
				kENABLE_VISION_LOG = false;
				return;
			}

			// Move the numbered logs, deleting the oldest
			if (i == kNUM_VISION_LOGS) {
				// Delete (not fully recursive -- assumes only one layer of
				// files)
				for (File file : dir.toFile().listFiles()) {
					file.delete();
				}
				try {
					Files.delete(dir);
				} catch (IOException e) {
					System.err.println("Unable to move vision log directory: " + dir.toString());
					kENABLE_VISION_LOG = false;
					return;
				}
			} else {
				// Rename
				Path target = Paths.get(dirHome, kVISION_DIR_NAME + "." + (i + 1));
				try {
					Files.move(dir, target);
				} catch (IOException e) {
					System.err.println("Unable to move vision log directory: " + dir.toString());
					kENABLE_VISION_LOG = false;
					return;
				}
			}
		}

		// Move the unnumbered log
		if (Files.isDirectory(dirVision)) {
			// Rename
			Path target = Paths.get(dirHome, kVISION_DIR_NAME + ".1");
			try {
				Files.move(dirVision, target);
			} catch (IOException e) {
				System.err.println("Unable to move vision log directory: " + dirVision.toString());
				kENABLE_VISION_LOG = false;
				return;
			}
		}

		// Build our current directory
		try {
			Files.createDirectory(dirVision);
		} catch (IOException e) {
			System.err.println("Unable to create vision log directory");
			kENABLE_VISION_LOG = false;
			return;
		}
	}

	public void initDefaultCommand() {
		setDefaultCommand(new CameraAnalyze());
	}

	public boolean isRunning() {
		return (cam != null);
	}

	public void start() {
		if (isRunning()) {
			return;
		}
		cam = new USBCamera(RobotMap.usbCameraTarget);
		cam.openCamera();
		updateSettings();
		cam.startCapture();
		Output.output(OutputLevel.VISION, getName() + "-camera", RobotMap.usbCameraTarget);
	}

	public void stop() {
		if (isRunning()) {
			cam.closeCamera();
		}
		cam = null;
		Output.output(OutputLevel.VISION, getName() + "-camera", "<Disabled>");
	}

	public void updateSettings() {
		if (!isRunning()) {
			return;
		}

		cam.setSize(width, height);
		cam.setFPS(fps);
		if (exposure >= 0) {
			cam.setExposureManual(exposure);
		} else {
			cam.setExposureAuto();
		}
		if (whitebalance >= 0) {
			cam.setWhiteBalanceManual(whitebalance);
		} else {
			cam.setWhiteBalanceAuto();
		}
		if (brightness >= 0) {
			cam.setBrightness(brightness);
		}
		cam.updateSettings();
	}

	// External data interface -- always provide the last completed analysis
	public TargetData lastResult() {
		return data;
	}

	// Utilities
	public Image capture() {
		start();
		Image image = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
		cam.getImage(image);
		return image;
	}

	public void save(Image image, String path) {
		RGBValue value = new NIVision.RGBValue();
		NIVision.imaqWriteFile(image, path, value);
		value.free();
	}

	public Image thresholdHSL(Image image) {
		Image binary = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		Range range1 = new Range(Settings.Key.VISION_H_LOW.getInt(), Settings.Key.VISION_H_HIGH.getInt());
		Range range2 = new Range(Settings.Key.VISION_S_LOW.getInt(), Settings.Key.VISION_S_HIGH.getInt());
		Range range3 = new Range(Settings.Key.VISION_L_LOW.getInt(), Settings.Key.VISION_L_HIGH.getInt());
		NIVision.imaqColorThreshold(binary, image, kBINARY_COLOR, NIVision.ColorMode.HSL, range1, range2, range3);
		range1.free();
		range2.free();
		range3.free();
		return binary;
	}

	// Analysis
	public void analyze() {
		TargetData data = new TargetData();
		data.start = System.currentTimeMillis();
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-tsStart", data.start);
		}

		// Capture
		Image image = capture();
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-tsCapture", System.currentTimeMillis() - data.start);
		}

		// Reduce to binary colors with HSL threshold processing
		Image binary = thresholdHSL(image);
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-tsThreshold", System.currentTimeMillis() - data.start);
		}

		// Find contiguous blobs
		int blobs = NIVision.imaqCountParticles(binary, 1);
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-blobCout", blobs);
			Output.output(OutputLevel.VISION, getName() + "-tsParticles", System.currentTimeMillis() - data.start);
		}

		// Find the biggest blob
		int biggest = 0;
		double maxArea = 0;
		for (int i = 0; i < blobs; i++) {
			double area = NIVision.imaqMeasureParticle(binary, i, 0, MeasurementType.MT_AREA);
			if (area > maxArea) {
				maxArea = area;
				biggest = i;
			}
		}
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-biggestBlob", biggest);
			Output.output(OutputLevel.VISION, getName() + "-biggestBlobArea", maxArea);
			Output.output(OutputLevel.VISION, getName() + "-tsBiggest", System.currentTimeMillis() - data.start);
		}

		// Confidence
		if (maxArea > kMIN_BLOB_AREA) {
			data.confidence = Confidence.kMINIMAL;
		} else if (maxArea > kNOMINAL_BLOB_AREA) {
			data.confidence = Confidence.kNOMINAL;
		}
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-confidence", data.confidence);
			Output.output(OutputLevel.VISION, getName() + "-tsConfidence", System.currentTimeMillis() - data.start);
		}

		// Vision log
		if (kENABLE_VISION_LOG && data.confidence != Confidence.kNONE) {
			Path file = Paths.get(dirVision.toString(), data.start + "-raw.jpg");
			save(image, file.toString());
			file = Paths.get(dirVision.toString(), data.start + "-threshold.jpg");
			save(binary, file.toString());
		}

		// Position extraction
		if (data.confidence != Confidence.kNONE) {
			double x = NIVision.imaqMeasureParticle(binary, biggest, 0, MeasurementType.MT_CENTER_OF_MASS_X);
			double y = NIVision.imaqMeasureParticle(binary, biggest, 0, MeasurementType.MT_CENTER_OF_MASS_Y);
			if (kDEBUG) {
				Output.output(OutputLevel.VISION, getName() + "-x", x);
				Output.output(OutputLevel.VISION, getName() + "-y", y);
				Output.output(OutputLevel.VISION, getName() + "-tsXY", System.currentTimeMillis() - data.start);
			}
		}

		// Buffer cleanup
		image.free();
		binary.free();

		// Azimuth calculation
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-azimuth", data.azimuth);
			Output.output(OutputLevel.VISION, getName() + "-tsAzimuth", System.currentTimeMillis() - data.start);
		}

		// Altitude calculation
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-altitude", data.altitude);
			Output.output(OutputLevel.VISION, getName() + "-tsAltitude", System.currentTimeMillis() - data.start);
		}

		// Distance calculation
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-distance", data.distance);
			Output.output(OutputLevel.VISION, getName() + "-tsDistance", System.currentTimeMillis() - data.start);
		}

		// Finalize
		data.end = System.currentTimeMillis();
		data.valid = true;
		if (kENABLE_VISION_LOG && data.confidence != Confidence.kNONE) {
			data.save(Paths.get(dirVision.toString(), data.start + "-analysis.txt"));
		}
		Output.output(OutputLevel.VISION, getName() + "-duration", data.end - data.start);

		// Publish
		this.data = data;
	}
}
