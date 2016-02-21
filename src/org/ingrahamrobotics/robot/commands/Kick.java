package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.Command;

public class Kick extends Command {
	
	private long done = 0;

	public Kick() {
    	requires(Robot.kicker);
	}

	protected void initialize() {
		int duration = Settings.Key.KICKER_TIME.getInt();
		done = System.currentTimeMillis() + duration;
	}
	
	protected void execute() {
		Robot.kicker.kick();
	}

	protected boolean isFinished() {
		return System.currentTimeMillis() >= done;
	}

	protected void end() {
		Robot.kicker.stop();
	}

	protected void interrupted() {
		this.end();
	}
}
