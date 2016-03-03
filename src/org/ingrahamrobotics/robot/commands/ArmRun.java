package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ArmRun extends Command {

    public ArmRun() {
        requires(Robot.arm);
    }

    protected void initialize() {
    	Robot.arm.start();
    }

    protected void execute() {
    	Robot.arm.updatePID();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.arm.stop();
    }

    protected void interrupted() {
    	end();
    }
}
