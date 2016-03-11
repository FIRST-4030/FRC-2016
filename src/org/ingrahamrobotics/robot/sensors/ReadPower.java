package org.ingrahamrobotics.robot.sensors;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ReadPower extends Command {

	public ReadPower() {
		requires(Robot.power);
	}

	protected void initialize() {
	}

	protected void execute() {
		Robot.power.update();
	}

	protected boolean isFinished() {
		return Robot.disableReadPower;
	}

	protected void end() {
	}

	protected void interrupted() {
		end();
	}
}
