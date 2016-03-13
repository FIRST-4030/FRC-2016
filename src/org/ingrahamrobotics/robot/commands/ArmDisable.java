package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ArmDisable extends Command {

	public ArmDisable() {
	}

	protected void initialize() {
		if (Robot.arm.ready()) {
			Robot.arm.stop();
		}
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		if (Robot.arm.ready()) {
			Robot.arm.start();
		}
	}

	protected void interrupted() {
		end();
	}
}
