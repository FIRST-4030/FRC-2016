package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings.Key;

import edu.wpi.first.wpilibj.command.Command;

public class ArmPreset extends Command {
	
	private Key key = null;
	public ArmPreset(Key key) {
		this.key = key;
		
		// This code uses Robot.Arm to modify the arm setpoint
		// The ArmRun command should *also* be running to control of the arm position
	}

	@Override
	protected void initialize() {
		if (Robot.armRun != null && !Robot.armRun.isRunning()) {
			Robot.armRun.start();
		}
	}

	@Override
	protected void execute() {
		int setpoint = key.getInt();
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
