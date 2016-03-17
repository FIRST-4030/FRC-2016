package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightToEncoder extends Command {

	private int left;

	public DriveStraightToEncoder(double left) {
		requires(Robot.drive);
		this.left = (int)left;
	}
	
	protected void initialize() {
		Robot.drive.setStraight(left);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return true;
	}

	protected void end() {
	}

	protected void interrupted() {
		end();
	}
}
