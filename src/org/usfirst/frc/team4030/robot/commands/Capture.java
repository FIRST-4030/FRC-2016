package org.usfirst.frc.team4030.robot.commands;

import org.usfirst.frc.team4030.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class Capture extends Command {
	
	// Kick motion duration in ms
	public static final int duration = 500;
	
	// When should we stop
	private long done = 0;

	public Capture() {
    	requires(Robot.kicker);
	}

	protected void initialize() {
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
		this.end();
	}
}
