package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArmZero extends Command {

    public ArmZero() {
    	requires(Robot.arm);
    }

    protected void initialize() {
    }

    protected void execute() {
    	Robot.arm.zero();
    }

    protected boolean isFinished() {
    	return Robot.arm.checkZero();
    }

    protected void end() {
    	Robot.arm.stop();
    }

    protected void interrupted() {
    	this.end();
    }
}
