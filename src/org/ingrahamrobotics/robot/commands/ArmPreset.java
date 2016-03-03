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
	// The Robot.armRun command should *also* be running to control of the arm
	// position
	@Override
	protected void initialize() {
		if (Robot.armRun != null && !Robot.armRun.isRunning()) {
			Robot.armRun.start();
		}
	}

	@Override
	protected void execute() {
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
