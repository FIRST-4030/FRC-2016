package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.dashboard.Settings;

import edu.wpi.first.wpilibj.command.Command;

public class KickerMove extends Command {

	public static enum Mode {
		CAPTURE(), KICK();
	}

	private Mode mode;
	private long done = 0;

	public KickerMove(Mode mode) {
		requires(Robot.kicker);
		this.mode = mode;
	}

	protected void initialize() {
		int duration = Settings.Key.KICKER_TIME.getInt();
		done = System.currentTimeMillis() + duration;
	}

	protected void execute() {
		switch (mode) {
		case CAPTURE:
			Robot.kicker.capture();
			break;
		case KICK:
			Robot.kicker.kick();
			break;
		}
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
