package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterLock extends Command {

	private boolean wait;

	public ShooterLock() {
		wait = true;
	}

	protected void initialize() {
		synchronized (Robot.shooter) {
			if (!Robot.shooterLock) {
				Robot.shooterLock = true;
				wait = false;
			}
		}
	}

	protected void execute() {
		synchronized (Robot.shooter) {
			if (wait && Robot.shooterLock) {
				return;
			}
			wait = false;
			Robot.shooterLock = true;
		}
	}

	protected boolean isFinished() {
		return !wait;
	}

	protected void end() {
	}

	protected void interrupted() {
		end();
	}
}
