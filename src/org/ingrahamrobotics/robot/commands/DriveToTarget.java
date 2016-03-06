package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.subsystems.DriveFull.HalfTarget;

import edu.wpi.first.wpilibj.command.Command;

public class DriveToTarget extends Command {

	HalfTarget targets[];

	public DriveToTarget(HalfTarget targets[]) {
		requires(Robot.drive);
		this.targets = targets;
	}

	protected void initialize() {
		Robot.drive.set(targets);
	}

	protected void execute() {
		Robot.drive.updatePID();
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.drive.stop();
	}

	protected void interrupted() {
		end();
	}
}
