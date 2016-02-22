package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.commands.DriverView;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriverCamera extends Subsystem {

    CameraServer server = null;
    
    public void initDefaultCommand() {
        //setDefaultCommand(new DriverView());
    }
    
    public void start() {
    	if (server == null) {
    		server = CameraServer.getInstance();
    	}
        server.setQuality(50);
        server.startAutomaticCapture(RobotMap.usbDriverCamera);
		Output.output(OutputLevel.SENSORS, getName() + "-camera", RobotMap.usbDriverCamera);
		Output.output(OutputLevel.SENSORS, getName() + "-streaming", true);
    }
    
    public void stop() {
    	server = null;
		Output.output(OutputLevel.SENSORS, getName() + "-camera", "<Disabled>");
		Output.output(OutputLevel.SENSORS, getName() + "-streaming", false);
    }
    
    public boolean isRunning() {
    	return server.isAutoCaptureStarted();
    }
}
