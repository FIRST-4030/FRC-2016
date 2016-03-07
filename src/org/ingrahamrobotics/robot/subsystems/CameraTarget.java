package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ImageType;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.HSLImage;
import edu.wpi.first.wpilibj.image.ImageBase;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class CameraTarget extends Subsystem {

	private USBCamera cam;

	public void initDefaultCommand() {
		// No default command
	}

	public void start() {
		if (isRunning()) {
			return;
		}
		cam = new USBCamera(RobotMap.usbTargetCamera);
		cam.openCamera();
		Output.output(OutputLevel.SENSORS, getName() + "-camera", RobotMap.usbTargetCamera);
		Output.output(OutputLevel.SENSORS, getName() + "-open", true);
	}

	public void stop() {
		if (isRunning()) {
			cam.closeCamera();
		}
		cam = null;
		Output.output(OutputLevel.SENSORS, getName() + "-camera", "<Disabled>");
		Output.output(OutputLevel.SENSORS, getName() + "-open", false);
	}

	public boolean isRunning() {
		return (cam == null);
	}

	public void save(NIVision.Image image, String path) {
	    NIVision.RGBValue value = new NIVision.RGBValue();
	    NIVision.imaqWriteFile(image, path, value);
	    value.free();		
	}
	
	public NIVision.Image thresholdHSL(NIVision.Image image, int hLow, int hHigh, int sLow, int sHigh, int lLow, int lHigh) {
		NIVision.Image res = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_HSL, 0);
	    NIVision.Range range1 = new NIVision.Range(hLow, hHigh);
	    NIVision.Range range2 = new NIVision.Range(sLow, sHigh);
	    NIVision.Range range3 = new NIVision.Range(lLow, lHigh);
	    NIVision.imaqColorThreshold(res, image, 1, NIVision.ColorMode.HSL, range1, range2, range3);
	    res.free();
	    range1.free();
	    range2.free();
	    range3.free();
	    return res;
	}
	
	public void capture(NIVision.Image image) {
		this.start();
		cam.getImage(image);
		Output.output(OutputLevel.SENSORS, getName() + "-captured", System.currentTimeMillis());
		
		// Save the raw image
		save(image, "raw.jpg");
		
		// Reduce to HSL
		
		// Particle analysis
		NIVision.imaqCountParticles(image, 1);
	}
}