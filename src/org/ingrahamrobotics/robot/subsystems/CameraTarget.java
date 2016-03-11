package org.ingrahamrobotics.robot.subsystems;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.commands.CameraAnalyze;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.vision.Camera;
import org.ingrahamrobotics.robot.vision.Data;
import org.ingrahamrobotics.robot.vision.Data.Confidence;
import org.ingrahamrobotics.robot.vision.Log;
import org.ingrahamrobotics.robot.vision.Transform;
import org.ingrahamrobotics.robot.vision.Transform.ThresholdType;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.MeasurementType;

import edu.wpi.first.wpilibj.command.Subsystem;

public class CameraTarget extends Subsystem {

	// Debug
	private static final boolean kDEBUG = true;

	// Analysis constants (probably not tunable without algorithm adjustments)
	public static final int kMIN_BLOB_AREA = 2000;
	public static final int kNOMINAL_BLOB_AREA = kMIN_BLOB_AREA * 2;
	public static final double kROTATION_OFFSET = 180.0;
	public static final double kSOME_TRIG = 1.0;

	// Members
	private Camera cam;
	private Data data;
	private Transform trans;
	private Log log;

	// Subsystem control
	public CameraTarget() {
		super();
		cam = new Camera(RobotMap.usbCameraTarget);
		data = new Data();
		trans = new Transform();
		log = new Log();
		log.rotate();
	}

	public void initDefaultCommand() {
		if (!Robot.disableCamTarget) {
			setDefaultCommand(new CameraAnalyze());
		}
	}

	public void start() {
		cam.start();
	}

	public void stop() {
		cam.stop();
	}

	public void warmup() {
		updateSettings();
		analyze();
		analyze();
		analyze();

		// Reset once things are stable
		log.reset();
		analyze();

		// Run a static analysis, if the input file exists
		test();
	}

	// Update component settings
	public void updateSettings() {
		trans.updateSettings();
		cam.updateSettings();
	}

	// External data interface -- always provide the last completed analysis
	public Data get() {
		return data;
	}

	// Public interface for analysis -- capture from camera
	public void analyze() {
		analyze(null);
	}

	// Public interface to test using file input
	public void test() {
		Path file = Paths.get("/home/lvuser/input.jpg");
		if (Files.exists(file)) {
			System.err.println("Testing with vision file: " + file.toString());
			analyze(file);
		}
	}

	// Analysis -- from camera or saved file
	private void analyze(Path src) {
		Data data = new Data();
		data.start = System.currentTimeMillis();
		Output.output(OutputLevel.VISION, getName() + "-tsStart", data.start);

		// Capture or load
		Image image = null;
		if (src != null) {
			// Mark this non-capture analysis
			data.start = 0;

			// These have no meaning when loading from a file
			data.brightness = 0;
			data.exposure = 0;
			data.whitebalance = 0;
			image = trans.read(src);
		} else {
			data.brightness = cam.getBrightness();
			data.exposure = cam.getExposure();
			data.whitebalance = cam.getWhitebalance();
			image = cam.capture();
		}

		// Reduce to binary colors with HSL threshold processing
		data.h_min = trans.getThreshold(ThresholdType.H_LOW);
		data.h_max = trans.getThreshold(ThresholdType.H_HIGH);
		data.s_min = trans.getThreshold(ThresholdType.S_LOW);
		data.s_max = trans.getThreshold(ThresholdType.S_HIGH);
		data.l_min = trans.getThreshold(ThresholdType.L_LOW);
		data.l_max = trans.getThreshold(ThresholdType.L_HIGH);
		Image binary = trans.thresholdHSL(image);

		// Find contiguous blobs
		int blobs = NIVision.imaqCountParticles(binary, 1);
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-blobs", blobs);
		}

		// Find the biggest blob
		int particleIndex = -1;
		data.area = 0;
		for (int i = 0; i < blobs; i++) {
			double area = NIVision.imaqMeasureParticle(binary, i, 0, MeasurementType.MT_AREA);
			if (area > data.area) {
				data.area = area;
				particleIndex = i;
			}
		}
		if (kDEBUG) {
			Output.output(OutputLevel.VISION, getName() + "-area", data.area);
		}

		// Sanity check
		if (particleIndex < 0) {
			return;
		}

		// Position, size, and orientation extraction
		if (data.confidence != Confidence.kNONE) {
			data.x = NIVision.imaqMeasureParticle(binary, particleIndex, 0, MeasurementType.MT_CENTER_OF_MASS_X);
			data.y = NIVision.imaqMeasureParticle(binary, particleIndex, 0, MeasurementType.MT_CENTER_OF_MASS_Y);
			data.height = NIVision.imaqMeasureParticle(binary, particleIndex, 0,
					MeasurementType.MT_BOUNDING_RECT_HEIGHT);
			data.width = NIVision.imaqMeasureParticle(binary, particleIndex, 0, MeasurementType.MT_BOUNDING_RECT_WIDTH);
			data.rotation = NIVision.imaqMeasureParticle(binary, particleIndex, 0, MeasurementType.MT_ORIENTATION);
			data.rotation = kROTATION_OFFSET - data.rotation;
			if (kDEBUG) {
				Output.output(OutputLevel.VISION, getName() + "-x", data.x);
				Output.output(OutputLevel.VISION, getName() + "-y", data.y);
				Output.output(OutputLevel.VISION, getName() + "-height", data.rotation);
				Output.output(OutputLevel.VISION, getName() + "-width", data.rotation);
				Output.output(OutputLevel.VISION, getName() + "-rotation", data.rotation);
			}
		}

		// Target plane angle calculation (NOT DONE)
		data.plane = data.rotation * kSOME_TRIG;
		Output.output(OutputLevel.VISION, getName() + "-angle", data.plane);

		// Distance calculation (NOT DONE)
		data.distance = data.height * kSOME_TRIG;
		Output.output(OutputLevel.VISION, getName() + "-distance", data.distance);

		// Azimuth calculation (NOT DONE)
		data.azimuth = ((data.y - (Camera.width / 2)) * Camera.fovH) / Camera.width;
		Output.output(OutputLevel.VISION, getName() + "-azimuth", data.azimuth);

		// Altitude calculation (NOT DONE)
		data.altitude = ((data.x - (Camera.height / 2)) * Camera.fovV) / Camera.height;
		Output.output(OutputLevel.VISION, getName() + "-altitude", data.altitude);

		// Confidence
		if (data.area > kNOMINAL_BLOB_AREA) {
			data.confidence = Confidence.kNOMINAL;
		} else if (data.area > kMIN_BLOB_AREA) {
			data.confidence = Confidence.kMINIMAL;
		}
		Output.output(OutputLevel.VISION, getName() + "-confidence", data.confidence);

		// Finalize
		data.end = System.currentTimeMillis();
		data.valid = true;

		// Vision log
		if (data.confidence != Confidence.kNONE || kDEBUG) {
			log.save(image, data.start + "-capture.jpg");
			log.save(binary, data.start + "-threshold.jpg");
			log.save(data.toString(), data.start + "-analysis.txt");
		}

		// Buffer cleanup
		image.free();
		binary.free();

		// Publish
		this.data = data;
		Output.output(OutputLevel.VISION, getName() + "-duration", data.end - data.start);
	}
}
