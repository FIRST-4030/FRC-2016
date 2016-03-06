package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStop extends Command {
	
	public DriveStop() {
	}
	
	@Override
	protected void initialize() {
		Robot.drive.stop();
	}

	@Override
	protected void execute() {
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
		end();
	}
}
