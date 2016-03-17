package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveToTarget extends Command {

	public static final int kTicksPerRotation = 21600;
	public static final int kTicksPerDegree = kTicksPerRotation / 360;

	private static final double kSTOP = 0.0;

	private double left;
	private double right;

	private DriveToTarget(double left, double right, int angle) {
		requires(Robot.drive);
		if (left == kSTOP && right == kSTOP) {
			left = angle * kTicksPerDegree;
			right = left * -1.0;
		}
		this.left = left;
		this.right = right;
	}

	public DriveToTarget(int angle) {
		this(kSTOP, kSTOP, angle);
	}

	public DriveToTarget(double left, double right) {
		this(left, right, 0);
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
