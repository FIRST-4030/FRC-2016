package org.usfirst.frc.team4030.robot.commands;

import org.usfirst.frc.team4030.robot.Robot;

import edu.wpi.first.wpilibj.command.PIDCommand;

public class RunPIDDrive extends PIDCommand {

	public RunPIDDrive() {
		super(1.0, 0.0, 0.0);
		requires(Robot.drive);
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
