package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterRun extends Command {

	public ShooterRun() {
		requires(Robot.shooter);
	}

	protected void initialize() {
		Robot.shooterRun = this;
		Robot.shooter.start();
	}

	protected void execute() {
		Robot.shooter.updatePID();
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.shooter.stop();
	}

	protected void interrupted() {
		end();
	}
}
