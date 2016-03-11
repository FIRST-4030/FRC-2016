package org.ingrahamrobotics.robot.pid;

import edu.wpi.first.wpilibj.command.Command;

public class Wait extends Command {

	// Wait duration in ms
	public int duration = 500;

	// When should we stop
	private long done = 0;

	public Wait(int duration) {
		this.duration = duration;
	}

	protected void initialize() {
		done = System.currentTimeMillis() + duration;
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return System.currentTimeMillis() >= done;
	}

	protected void end() {
	}

	protected void interrupted() {
		this.end();
	}
}
