package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;

import com.ni.vision.NIVision;

import edu.wpi.first.wpilibj.command.Subsystem;
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

	public void capture(NIVision.Image image) {
		this.start();
		cam.getImage(image);
		Output.output(OutputLevel.SENSORS, getName() + "-captured", System.currentTimeMillis());
	}
}