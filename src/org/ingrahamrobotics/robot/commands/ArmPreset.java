package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings.Key;

import edu.wpi.first.wpilibj.command.Command;

public class ArmPreset extends Command {

	private Key key;
	private int target;
	private boolean done;

	private void init(Key key, int target) {
		requires(Robot.armPreset);
		done = false;
		this.key = key;
		this.target = target;
	}

	public ArmPreset(Key key) {
		this.init(key, 0);
	}

	public ArmPreset(int target) {
		this.init(null, target);
	}

	// This code uses Robot.arm to modify the arm setpoint
	// The Robot.armRun command should *also* be running
	@Override
	protected void initialize() {

		// Bail if the arm is not ready
		if (!Robot.arm.ready()) {
			System.err.println("ArmPreset started while arm not ready");
			done = true;
			return;
		}

		// Start regulation as needed
		if (!Robot.armRun.isRunning()) {
			Robot.armRun.start();
		}
	}

	@Override
	protected void execute() {

		// Error bypass
		if (done) {
			return;
		}

		// Use the integer or parameter setpoint
		int setpoint = target;
		if (key != null) {
			setpoint = key.getInt();
		}
		Robot.arm.set(setpoint);

		// Only run one
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
		this.end();
	}
}
