package org.usfirst.frc.team4030.robot.commands;

import org.usfirst.frc.team4030.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ArmManual extends Command {

	private Joystick arm = Robot.oi.arm;
	
    public ArmManual() {
        requires(Robot.arm);
    }

    protected void initialize() {
    }

    protected void execute() {
    	double y = arm.getY();
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
