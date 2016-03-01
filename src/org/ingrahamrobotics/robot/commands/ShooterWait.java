package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterWait extends Command {
	
    public ShooterWait() {
    }

    protected void initialize() {
    }

    protected void execute() {
    }

    // Done when we're at speed according to the PID subsystem
    protected boolean isFinished() {
		return Robot.shooter.onTarget();
    }

    protected void end() {
    }

    protected void interrupted() {
    	this.end();
    }
}
