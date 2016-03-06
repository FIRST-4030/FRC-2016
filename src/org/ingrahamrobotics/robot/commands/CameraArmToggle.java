package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CameraArmToggle extends Command {

	public CameraArmToggle() {
		requires(Robot.cameraArm);
	}

	protected void initialize() {
		Robot.cameraArm.toggle();
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return true;
	}

	protected void end() {
	}

	protected void interrupted() {
		end();
	}
}
