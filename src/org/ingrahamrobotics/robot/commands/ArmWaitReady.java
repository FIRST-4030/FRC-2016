package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ArmWaitReady extends Command {

	public ArmWaitReady() {
	}

	protected void initialize() {
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return Robot.arm.ready();
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}
