package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveToTarget extends Command {

	public static final int kTicksPerRotation = 21600;
	public static final int kTicksPerDegree = kTicksPerRotation / 360;
	
	private double left;
	private double right;

	private void set(double left, double right) {
		this.left = left;
		this.right = right;		
	}
	
	public DriveToTarget(int angle, boolean clockwise) {
		requires(Robot.drive);
		double left = angle * kTicksPerDegree;
		double right = left;
		if (clockwise) {
			right *= -1;
		} else {
			left *= -1;
		}
		set(left, right);
	}
	
	public DriveToTarget(double left, double right) {
		requires(Robot.drive);
		set(left, right);
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
