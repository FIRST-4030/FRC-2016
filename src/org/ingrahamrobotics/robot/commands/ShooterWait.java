package org.ingrahamrobotics.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterWait extends Command {
	
	// This should query the shooter speed error, but for now just delay for a fixed period
	public int duration = 2000;
	private long done = 0;
	
    public ShooterWait() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		done = System.currentTimeMillis() + duration;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
		return System.currentTimeMillis() >= done;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
