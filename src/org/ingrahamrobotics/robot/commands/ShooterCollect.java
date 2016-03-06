package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterCollect extends Command {

	public ShooterCollect() {
		requires(Robot.collector);
		requires(Robot.shooter);
	}

	protected void initialize() {
	}

	protected void execute() {
		double speed = Settings.Key.SHOOTER_COLLECT.getDouble();
		Robot.shooter.setPower(speed);
		Robot.collector.set(speed);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.shooter.stop();
		Robot.collector.stop();
	}

	protected void interrupted() {
		end();
	}
}
