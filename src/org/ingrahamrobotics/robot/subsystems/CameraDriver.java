package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.dashboard.Output;
import org.ingrahamrobotics.robot.dashboard.OutputLevel;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CameraDriver extends Subsystem {

	CameraServer server;

	public void initDefaultCommand() {
		// No default command
	}

	public void start() {
		if (server == null) {
			server = CameraServer.getInstance();
			server.setQuality(50);
			server.startAutomaticCapture(RobotMap.usbCameraDriver);
		}
		Output.output(OutputLevel.SENSORS, getName() + "-camera", RobotMap.usbCameraDriver);
	}

	public void stop() {
		server = null;
		Output.output(OutputLevel.SENSORS, getName() + "-camera", "<Disabled>");
	}

	public boolean isRunning() {
		return server.isAutoCaptureStarted();
	}
}
