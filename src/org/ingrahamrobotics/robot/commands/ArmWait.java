package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ArmWait extends Command {
	
    public ArmWait() {
    }

    protected void initialize() {
    }

    protected void execute() {
    }

    // Done when we're on-target according to the PID subsystem
    protected boolean isFinished() {
		return Robot.arm.onTarget();
    }

    protected void end() {
    }

    protected void interrupted() {
    	this.end();
    }
}
