package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ArmManual extends Command {

	private Joystick shooterJoystick = Robot.oi.arm;
	
    public ArmManual() {
        requires(Robot.arm);
    }

    protected void initialize() {
    	Robot.arm.start();
    }

    protected void execute() {
    	Robot.arm.updatePID();
    	double y = shooterJoystick.getY();
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
