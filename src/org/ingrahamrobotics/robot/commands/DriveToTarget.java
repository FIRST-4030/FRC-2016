package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveToTarget extends Command {

	private double left;
	private double right;

	public DriveToTarget(double left, double right) {
		requires(Robot.drive);
		this.left = left;
		this.right = right;
	}

	protected void initialize() {
		Robot.drive.updatePID();
		Robot.drive.set(left, right);
	}

	protected void execute() {
		Robot.drive.updatePID();
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
