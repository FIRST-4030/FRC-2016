package org.usfirst.frc.team4030.robot.subsystems;

import org.usfirst.frc.team4030.robot.RobotMap;

import com.ni.vision.NIVision;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 *
 */
public class Camera extends Subsystem {

	private USBCamera cam;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void start() {
    	if (isRunning()) {
    		return;
    	}
    	cam = new USBCamera(RobotMap.usbCamera);
    	cam.openCamera();
    }
    
    public void stop() {
    	cam.closeCamera();
    	cam = null;
    }
    
    public boolean isRunning() {
    	if (cam == null) {
    		return false;
    	}
    	return true;
    }
    
    public void capture(NIVision.Image image) {
    	this.start();
    	cam.getImage(image);
    }
}
