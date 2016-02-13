package org.usfirst.frc.team4030.robot.subsystems;

import org.usfirst.frc.team4030.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveBase extends Subsystem {
	
	Talon motorLeft = new Talon(RobotMap.pwmDriveLeft);
	Talon motorRight = new Talon(RobotMap.pwmDriveRight);
    Encoder encoderLeft = new Encoder(RobotMap.dioDriveLeftA, RobotMap.dioDriveLeftB);
    Encoder encoderRight = new Encoder(RobotMap.dioDriveRightA, RobotMap.dioDriveRightB);

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
       // setDefaultCommand(new Drive());
    }
    
    public void start() {
    }
    
    public void stop() {
    	motorLeft.disable();
    	motorRight.disable();
    }
}
