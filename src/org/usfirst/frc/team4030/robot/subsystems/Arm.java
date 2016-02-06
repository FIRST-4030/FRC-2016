package org.usfirst.frc.team4030.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class Arm extends PIDSubsystem {
	
	Talon driveMotor;
    Encoder driveEncoder;

	public Arm(double p, double i, double d) {
		super(p, i, d);
		// TODO Auto-generated constructor stub
	}

	public Arm(String name, double p, double i, double d) {
		super(name, p, i, d);
		// TODO Auto-generated constructor stub
	}

	public Arm(double p, double i, double d, double period) {
		super(p, i, d, period);
		// TODO Auto-generated constructor stub
	}

	public Arm(String name, double p, double i, double d, double f) {
		super(name, p, i, d, f);
		// TODO Auto-generated constructor stub
	}

	public Arm(double p, double i, double d, double period, double f) {
		super(p, i, d, period, f);
		// TODO Auto-generated constructor stub
	}

	public Arm(String name, double p, double i, double d, double f,
			double period) {
		super(name, p, i, d, f, period);
		// TODO Auto-generated constructor stub
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
