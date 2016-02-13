package org.usfirst.frc.team4030.robot.subsystems;

import org.usfirst.frc.team4030.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class Arm extends PIDSubsystem {
	
	Talon motor = new Talon(RobotMap.pwmArm);
    Encoder encoder = new Encoder(RobotMap.dioArmA, RobotMap.dioArmB);

	public Arm() {
		super(1.0, 0.0, 0.0);
	}
	
	public void stop() {
		motor.disable();
	}

	@Override
	protected double returnPIDInput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
}
