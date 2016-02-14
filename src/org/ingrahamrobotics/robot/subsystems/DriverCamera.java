package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.commands.DriverView;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriverCamera extends Subsystem {

    CameraServer server = null;
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DriverView());
    }
    
    public void start() {
    	if (server == null) {
    		server = CameraServer.getInstance();
    	}
        server.setQuality(50);
        server.startAutomaticCapture(RobotMap.usbDriverCamera);
    }
    
    public void stop() {
    	server = null;
    }
    
    public boolean isRunning() {
    	return server.isAutoCaptureStarted();
    }
}
