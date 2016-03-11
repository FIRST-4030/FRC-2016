package org.ingrahamrobotics.robot.sensors;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ReadSensors extends Command {

	public ReadSensors() {
		requires(Robot.sensors);
	}

	protected void initialize() {
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
