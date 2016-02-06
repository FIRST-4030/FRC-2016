package org.usfirst.frc.team4030.robot.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;

public class RunPIDDrive extends PIDCommand {
	
	//Should this be a PIDCommand or a Command?

	public RunPIDDrive(double p, double i, double d) {
		super(p, i, d);
		// TODO Auto-generated constructor stub
	}

	public RunPIDDrive(String name, double p, double i, double d) {
		super(name, p, i, d);
		// TODO Auto-generated constructor stub
	}

	public RunPIDDrive(double p, double i, double d, double period) {
		super(p, i, d, period);
		// TODO Auto-generated constructor stub
	}

	public RunPIDDrive(String name, double p, double i, double d, double period) {
		super(name, p, i, d, period);
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
	protected void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
