package org.usfirst.frc.team4030.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class DriveBase extends PIDSubsystem {
	
	Talon driveMotor;
    Encoder driveEncoder;

	public DriveBase(int wheelNum) {
		super("PIDDrive" + wheelNum, 1.0, 0.0, 0.0);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected double returnPIDInput() {
		return driveEncoder.getRaw();
	}

	@Override
	protected void usePIDOutput(double output) {
		driveMotor.set(output);
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}
