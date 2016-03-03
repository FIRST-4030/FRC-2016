package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.Command;

public class KickerCapture extends Command {
	private long done = 0;

	public KickerCapture() {
    	requires(Robot.kicker);
    	requires(Robot.shooter);
	}

	protected void initialize() {
		int duration = Settings.Key.KICKER_TIME.getInt();
		done = System.currentTimeMillis() + duration;
	}
	
	protected void execute() {
		Robot.kicker.capture();
	}

	protected boolean isFinished() {
		return System.currentTimeMillis() >= done;
	}

	protected void end() {
		Robot.kicker.stop();
	}

	protected void interrupted() {
		end();
	}
}
