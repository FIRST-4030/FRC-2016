package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings.Key;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterPreset extends Command {

	private Key key;
	private int speed;

	public ShooterPreset(Key key) {
		this.speed = 0;
		this.key = key;
		requires(Robot.shooterPreset);
	}

	public ShooterPreset(int speed) {
		this.speed = speed;
		this.key = null;
		requires(Robot.shooterPreset);
	}

	// This code uses Robot.shooter to modify the shooter setpoint
	// The Robot.shooterRun command should *also* be running to control of the
	// shooter speed
	@Override
	protected void initialize() {
		if (Robot.shooterRun != null && !Robot.shooterRun.isRunning()) {
			Robot.shooterRun.start();
		}
	}

	@Override
	protected void execute() {
		int setpoint = speed;
		if (key != null) {
			setpoint = key.getInt();
		}
		Robot.shooter.set(setpoint);
	}

	@Override
	protected boolean isFinished() {
		return Robot.shooter.onTarget();
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
		this.end();
	}
}
