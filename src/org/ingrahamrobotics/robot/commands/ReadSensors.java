package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ReadSensors extends Command {

	public ReadSensors() {
		requires(Robot.sensors);
	}

	protected void initialize() {
		Robot.sensors.calibrate();
	}

	protected void execute() {
		Robot.sensors.update();
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
	}

	protected void interrupted() {
		end();
	}
}
