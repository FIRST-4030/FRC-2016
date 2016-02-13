package org.usfirst.frc.team4030.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class Arm extends PIDSubsystem {
	
	Talon driveMotor;
    Encoder driveEncoder;

	public Arm() {
		super(1.0, 0.0, 0.0);
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
