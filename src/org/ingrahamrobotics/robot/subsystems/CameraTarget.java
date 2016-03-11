package org.ingrahamrobotics.robot.subsystems;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.commands.CameraAnalyze;
import org.ingrahamrobotics.robot.vision.Analyze;

public class CameraTarget extends Analyze {

	public static final int kNOMINAL_BLOB_AREA = 4000;
	
	public CameraTarget() {
		super(RobotMap.usbCameraTarget, kNOMINAL_BLOB_AREA);
	}
	
	@Override
	public void initDefaultCommand() {
		if (!Robot.disableCamTarget) {
			setDefaultCommand(new CameraAnalyze());
		}
	}

	// Public interface to test using file input
	public void test() {
		Path file = Paths.get("/home/lvuser/input.jpg");
		if (Files.exists(file)) {
			System.err.println("Testing with vision file: " + file.toString());
			analyze(file);
		}
	}
}
