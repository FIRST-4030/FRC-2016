package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings.Key;

import edu.wpi.first.wpilibj.command.Command;

public class ArmPreset extends Command {

	private Key key;
	private int target;

	private void init(Key key, int target) {
		synchronized(Robot.shooter) {
			if (Robot.shooterLock) {
				return;
			}
		}

		// This code uses Robot.arm to modify the arm setpoint
		// The Robot.armRun command should *also* be running
		requires(Robot.armPreset);

		this.key = key;
		this.target = target;
	}

	public ArmPreset(Key key) {
		init(key, 0);
	}

	public ArmPreset(int target) {
		init(null, target);
	}

	@Override
	protected void initialize() {

		// Start regulation as needed
		if (!Robot.armRun.isRunning()) {
			System.err.println("ArmPreset called while armRun is not running");
			Robot.armRun.start();
		}

		// Bail if the arm is not ready
		if (!Robot.arm.ready()) {
			System.err.println("ArmPreset called while arm not ready");
			return;
		}

		// Use the integer or parameter setpoint
		int setpoint = target;
		if (key != null) {
			setpoint = key.getInt();
		}
		Robot.arm.set(setpoint);
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
