package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveToGyro extends Command {

	private double angle;

	public DriveToGyro(double angle) {
		requires(Robot.drive);
		this.angle = angle;
	}
	
	protected void initialize() {
		Robot.drive.set(angle);
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
