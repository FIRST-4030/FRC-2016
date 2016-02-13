package org.usfirst.frc.team4030.robot.subsystems;

import org.usfirst.frc.team4030.robot.RobotMap;
import org.usfirst.frc.team4030.robot.commands.DriverView;

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
