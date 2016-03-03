package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings.Key;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterPreset extends Command {

	private Key key;
	private int speed;
	private boolean done;
	
	private void init(Key key, int speed) {
		requires(Robot.shooterPreset);
		done = false;
		this.key = key;
		this.speed = speed;
	}

	public ShooterPreset(Key key) {
		init(key, 0);
	}

	public ShooterPreset(int speed) {
		init(null, speed);
	}

	// This code uses Robot.shooter to modify the shooter setpoint
	// The Robot.shooterRun command should *also* be running
	@Override
	protected void initialize() {
		if (!Robot.shooterRun.isRunning()) {
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
		
		// Only run once
		done = true;
	}

	@Override
	protected boolean isFinished() {
		return done;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
		end();
	}
}
