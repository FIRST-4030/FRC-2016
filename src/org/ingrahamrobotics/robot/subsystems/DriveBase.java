package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.commands.TankDrive;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveBase extends Subsystem {
	
	Talon motorLeft = new Talon(RobotMap.pwmDriveLeft);
	Talon motorRight = new Talon(RobotMap.pwmDriveRight);
    Encoder encoderLeft = new Encoder(RobotMap.dioDriveLeftA, RobotMap.dioDriveLeftB);
    Encoder encoderRight = new Encoder(RobotMap.dioDriveRightA, RobotMap.dioDriveRightB);

    private RobotDrive drive = new RobotDrive(motorLeft, motorRight);
	
    public void initDefaultCommand() {
        setDefaultCommand(new TankDrive());
    }
    
    public void drive(GenericHID stick) {
    	drive.arcadeDrive(stick);
    }

    public void drive(GenericHID left, GenericHID right) {
    	drive.tankDrive(left, right);
    }
    
    protected void set(double left, double right) {
		Output.output(OutputLevel.MOTORS, getName() + "-left", left);
		Output.output(OutputLevel.MOTORS, getName() + "-right", right);
    	this.motorLeft.set(left);
    	this.motorRight.set(right);
    }
    
    public void stop() {
		Output.output(OutputLevel.MOTORS, getName() + "-left", 0);
		Output.output(OutputLevel.MOTORS, getName() + "-right", 0);
    	motorLeft.disable();
    	motorRight.disable();
    }
}
