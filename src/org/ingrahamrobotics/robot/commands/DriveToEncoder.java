package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveToEncoder extends Command {

	public static final int kTicksPerRotation = 21600;
	public static final int kTicksPerDegree = kTicksPerRotation / 360;
	public static final int kSTOP = 0;

	private int left;
	private int right;

	public DriveToEncoder(double angle) {
		this(kSTOP, kSTOP, angle);
	}

	public DriveToEncoder(double left, double right) {
		this((int) left, (int) right, 0);
	}

	private DriveToEncoder(int left, int right, double angle) {
		requires(Robot.drive);
		if (left == kSTOP && right == kSTOP) {
			left = (int)(angle * kTicksPerDegree);
			right = left * -1;
		}
		this.left = left;
		this.right = right;
	}

	protected void initialize() {
		Robot.drive.set(left, right);
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
