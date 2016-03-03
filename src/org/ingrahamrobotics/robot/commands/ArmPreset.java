package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings.Key;

import edu.wpi.first.wpilibj.command.Command;

public class ArmPreset extends Command {

	private Key key;
	private int target;

	public ArmPreset(Key key) {
		this.target = 0;
		this.key = key;
		requires(Robot.armPreset);
	}

	public ArmPreset(int target) {
		this.key = null;
		this.target = target;
		requires(Robot.armPreset);
	}

	// This code uses Robot.arm to modify the arm setpoint
	// The Robot.armRun command should *also* be running
	@Override
	protected void initialize() {
		// Bail if the arm is not ready
		if (!Robot.arm.ready()) {
			System.err.println("ArmPreset started while arm not ready");
			return;
		}

		// Start regulation as needed
		if (!Robot.armRun.isRunning()) {
			Robot.armRun.start();
		}
	}

	@Override
	protected void execute() {
		
		// Bail if the arm is not ready
		if (!Robot.arm.ready()) {
			System.err.println("ArmPreset running while arm not ready");
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
	protected boolean isFinished() {
		return Robot.arm.onTarget();
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
		this.end();
	}
}
