package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterUnlock extends Command {

	protected void initialize() {
		synchronized (Robot.shooter) {
			Robot.shooterLock = false;
		}
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
