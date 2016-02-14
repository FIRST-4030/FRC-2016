package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriverView extends Command {

    public DriverView() {
        requires(Robot.camDriver);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.camDriver.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.camDriver.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.camDriver.stop();
    }
}
