package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ArmManual extends Command {

	private Joystick stick;
	
    public ArmManual() {
        requires(Robot.arm);
    }

    protected void initialize() {
    	stick = Robot.oi.joyArm;
    	Robot.arm.start();
    }

    protected void execute() {
    	Robot.arm.updatePID();
    	double y = stick.getY();
    	Robot.arm.set(y);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.arm.stop();
    }

    protected void interrupted() {
    	this.end();
    }
}
