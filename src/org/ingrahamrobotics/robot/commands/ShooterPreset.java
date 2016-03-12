package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings.Key;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterPreset extends Command {

	private Key key;
	private double target;

	private void init(Key key, int target) {

		// This code uses Robot.shooter to modify the shooter setpoint
		// The Robot.shooterRun command should *also* be running
		requires(Robot.shooterPreset);

		this.key = key;
		this.target = target;
	}

	public ShooterPreset(Key key) {
		init(key, 0);
	}

	public ShooterPreset(int target) {
		init(null, target);
	}

	@Override
	protected void initialize() {
		if (!Robot.shooterRun.isRunning()) {
			System.err.println("ShooterPreset called while shooterRun not running");
			return;
		}

		double setpoint = target;
		if (key != null) {
			setpoint = key.getDouble();
		}
		Robot.shooter.set(setpoint);
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
		end();
	}
}
