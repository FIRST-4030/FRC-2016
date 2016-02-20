package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ShooterManual extends Command {

	private Joystick stick;
	
    public ShooterManual() {
        requires(Robot.shooter);
    }

    protected void initialize() {
    	stick = Robot.oi.joyArm;
    	Robot.shooter.start();
    }

    protected void execute() {
    	Robot.shooter.updatePID();
    	double x = stick.getX();
    	Robot.shooter.set(x);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.shooter.stop();
    }

    protected void interrupted() {
    	this.end();
    }
}
