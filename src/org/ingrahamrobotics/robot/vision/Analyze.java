package org.ingrahamrobotics.robot.vision;

import java.nio.file.Path;

import org.ingrahamrobotics.robot.dashboard.Output;
import org.ingrahamrobotics.robot.dashboard.OutputLevel;
import org.ingrahamrobotics.robot.vision.Data.Confidence;
import org.ingrahamrobotics.robot.vision.Transform.ThresholdType;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.MeasurementType;

import edu.wpi.first.wpilibj.command.Subsystem;

abstract public class Analyze extends Subsystem {
	
	public static final boolean kDEBUG = true;
	public static final double kMIN_AREA_FACTOR = 0.5;
	public static final double kROTATION_OFFSET = 180.0;
	public static final double kSOME_TRIG = 1.0;
	
	private int area;
	private Camera cam;
	private Data data;
	private Transform trans;
	private Log log;
	
	public Analyze(String name, int area) {
		this.area = area;
		
		cam = new Camera(name);
		data = new Data();
		trans = new Transform();
		log = new Log();
		log.rotate();
	}
	
	abstract public void initDefaultCommand();
	
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
	}
	
	public void updateSettings() {
		trans.updateSettings();
		cam.updateSettings();
	}
	
	public Data get() {
		return data;
	}
	
	public Data analyze() {
		return analyze(null);
	}
	
	public Data analyze(Path src) {
		Data data = new Data();
		data.start = System.currentTimeMillis();
		Output.output(OutputLevel.VISION, "analyze-tsStart", data.start);

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
			Output.output(OutputLevel.VISION, "analyze-blobs", blobs);
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
			Output.output(OutputLevel.VISION, "analyze-area", data.area);
		}

		// Sanity check
		if (particleIndex < 0) {
			return null;
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
				Output.output(OutputLevel.VISION, "analyze-x", data.x);
				Output.output(OutputLevel.VISION, "analyze-y", data.y);
				Output.output(OutputLevel.VISION, "analyze-height", data.rotation);
				Output.output(OutputLevel.VISION, "analyze-width", data.rotation);
				Output.output(OutputLevel.VISION, "analyze-rotation", data.rotation);
			}
		}

		// Target plane angle calculation (NOT DONE)
		data.plane = data.rotation * kSOME_TRIG;
		Output.output(OutputLevel.VISION, "analyze-angle", data.plane);

		// Distance calculation (NOT DONE)
		data.distance = data.height * kSOME_TRIG;
		Output.output(OutputLevel.VISION, "analyze-distance", data.distance);

		// Azimuth calculation (NOT DONE)
		data.azimuth = ((data.y - (Camera.width / 2)) * Camera.fovH) / Camera.width;
		Output.output(OutputLevel.VISION, "analyze-azimuth", data.azimuth);

		// Altitude calculation (NOT DONE)
		data.altitude = ((data.x - (Camera.height / 2)) * Camera.fovV) / Camera.height;
		Output.output(OutputLevel.VISION, "analyze-altitude", data.altitude);

		// Confidence
		if (data.area > area) {
			data.confidence = Confidence.kNOMINAL;
		} else if (data.area > area * kMIN_AREA_FACTOR) {
			data.confidence = Confidence.kMINIMAL;
		}
		Output.output(OutputLevel.VISION, "analyze-confidence", data.confidence);

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
		data.valid = true;
		this.data = data;
		Output.output(OutputLevel.VISION, "analyze-duration", System.currentTimeMillis() - data.start);
		return data;
	}
}
