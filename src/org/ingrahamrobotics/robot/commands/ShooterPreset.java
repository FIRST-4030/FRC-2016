package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings.Key;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterPreset extends Command {

	private Key key;
	private int target;
	private boolean done;
	
	private void init(Key key, int target) {
		
		// This code uses Robot.shooter to modify the shooter setpoint
		// The Robot.shooterRun command should *also* be running
		requires(Robot.shooterPreset);

		done = false;
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
			Robot.shooterRun.start();
			System.err.println("ShooterPreset called while shooterRun not running");
		}

		int setpoint = target;
		if (key != null) {
			setpoint = key.getInt();
		}
		Robot.shooter.set(setpoint);
		done = true;
	}

	@Override
	protected void execute() {
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
