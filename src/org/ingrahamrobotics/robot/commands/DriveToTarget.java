package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveToTarget extends Command {

	public static final int kTicksPerRotation = 21600;
	public static final int kTicksPerDegree = kTicksPerRotation / 360;

	private static final int kSTOP = 0;

	private int left;
	private int right;

	private DriveToTarget(int left, int right, int angle) {
		requires(Robot.drive);
		if (left == kSTOP && right == kSTOP) {
			left = angle * kTicksPerDegree;
			right = left * -1;
		}
		this.left = left;
		this.right = right;
	}

	public DriveToTarget(int angle) {
		this(kSTOP, kSTOP, angle);
	}

	public DriveToTarget(double left, double right) {
		this((int) left, (int) right, 0);
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
