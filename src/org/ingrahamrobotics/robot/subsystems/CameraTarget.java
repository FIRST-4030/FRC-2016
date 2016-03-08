package org.ingrahamrobotics.robot.subsystems;

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
	private static final boolean kDEBUG_FILES = kDEBUG & true;
	private static final String kRAW_FILE = "/home/lvuser/raw.jpg";
	private static final String kTHRESHOLD_FILE = "/home/lvuser/thresh.jpg";

	// Camera settings (some of these may need to be exposed on the dashboard)
	public static final int width = 640;
	public static final int height = 480;
	public static final int fps = 10;
	public static int brightness = -1; // 0 - 100, -1 is "do not set"
	public static int exposure = -1; // 0 - 100, -1 is "auto"
	public static int whitebalance = 4700; // Color temperature in K, -1 is auto

	// Analysis constants (not tunable without algorithm adjustments)
	public static final int kBINARY_COLOR = 255;
	public static final int kMIN_BLOB_AREA = 2500;
	public static final int kNOMINAL_BLOB_AREA = kMIN_BLOB_AREA * 4;

	// Structures
	public class TargetData {
		boolean valid = false;
		public Confidence confidence = Confidence.kNONE;
		public int distance = 0;
		public double azimuth = 0;
		public double altitude = 0;
		public long start = 0;
		public long end = 0;
	}

	public enum Confidence {
		kNONE(), kMINIMAL(), kNOMINAL();
	}

	// Members
	private USBCamera cam;
	private TargetData data;

	public CameraTarget() {
		super();
		data = new TargetData();
	}

	public void initDefaultCommand() {
		setDefaultCommand(new CameraAnalyze());
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

	public boolean isRunning() {
		return (cam != null);
	}

	public void save(Image image, String path) {
		RGBValue value = new NIVision.RGBValue();
		NIVision.imaqWriteFile(image, path, value);
		value.free();
	}

	public Image capture() {
		start();
		Image image = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
		cam.getImage(image);

		if (kDEBUG_FILES) {
			save(image, kRAW_FILE);
		}
		return image;
	}

	public NIVision.Image thresholdHSL(NIVision.Image image) {
		Image binary = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		Range range1 = new Range(Settings.Key.VISION_H_LOW.getInt(), Settings.Key.VISION_H_HIGH.getInt());
		Range range2 = new Range(Settings.Key.VISION_S_LOW.getInt(), Settings.Key.VISION_S_HIGH.getInt());
		Range range3 = new Range(Settings.Key.VISION_L_LOW.getInt(), Settings.Key.VISION_L_HIGH.getInt());
		NIVision.imaqColorThreshold(binary, image, kBINARY_COLOR, NIVision.ColorMode.HSL, range1, range2, range3);
		range1.free();
		range2.free();
		range3.free();

		if (kDEBUG_FILES) {
			save(binary, kTHRESHOLD_FILE);
		}
		return binary;
	}

	public void analyze() {
		TargetData data = new TargetData();
		data.start = System.currentTimeMillis();
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-tsStart", data.start);
		}

		// Capture
		Image image = capture();
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-tsCapture", System.currentTimeMillis());
		}

		// Reduce to HSL
		Image binary = thresholdHSL(image);
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-tsThreshold", System.currentTimeMillis());
		}

		// Find contiguous blobs
		int blobs = NIVision.imaqCountParticles(binary, 1);
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-blobCout", blobs);
			Output.output(OutputLevel.VISION, getName() + "-tsParticles", System.currentTimeMillis());
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
			Output.output(OutputLevel.VISION, getName() + "-tsBiggest", System.currentTimeMillis());
		}

		// Confidence
		if (maxArea > kMIN_BLOB_AREA) {
			data.confidence = Confidence.kMINIMAL;
		} else if (maxArea > kNOMINAL_BLOB_AREA) {
			data.confidence = Confidence.kNOMINAL;
		}
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-confidence", data.confidence);
			Output.output(OutputLevel.VISION, getName() + "-tsConfidence", System.currentTimeMillis());
		}

		// Position extraction
		if (data.confidence != Confidence.kNONE) {
			double x = NIVision.imaqMeasureParticle(binary, biggest, 0, MeasurementType.MT_CENTER_OF_MASS_X);
			double y = NIVision.imaqMeasureParticle(binary, biggest, 0, MeasurementType.MT_CENTER_OF_MASS_Y);
			if (kDEBUG) {
				Output.output(OutputLevel.VISION, getName() + "-x", x);
				Output.output(OutputLevel.VISION, getName() + "-y", y);
				Output.output(OutputLevel.VISION, getName() + "-tsXY", System.currentTimeMillis());
			}
		}

		// Buffer cleanup
		image.free();
		binary.free();

		// Azimuth calculation
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-azimuth", data.azimuth);
			Output.output(OutputLevel.VISION, getName() + "-tsAzimuth", System.currentTimeMillis());
		}

		// Altitude calculation
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-altitude", data.altitude);
			Output.output(OutputLevel.VISION, getName() + "-tsAltitude", System.currentTimeMillis());
		}

		// Distance calculation
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-distance", data.distance);
			Output.output(OutputLevel.VISION, getName() + "-tsDistance", System.currentTimeMillis());
		}

		// Publish
		data.end = System.currentTimeMillis();
		data.valid = true;
		this.data = data;
		Output.output(OutputLevel.VISION, getName() + "-lastTS", data.end);
		Output.output(OutputLevel.VISION, getName() + "-duration", data.end - data.start);
	}

	public TargetData lastResult() {
		return data;
	}
}