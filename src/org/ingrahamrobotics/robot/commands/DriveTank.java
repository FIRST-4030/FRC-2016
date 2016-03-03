
package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTank extends Command {
	
	private Joystick left;
	private Joystick right;
		
    public DriveTank() {
        requires(Robot.drive);
    }

    protected void initialize() {
    	left = Robot.oi.joyLeft;
    	right = Robot.oi.joyRight;
    }

    protected void execute() {
    	double leftVal = left.getY();
    	double rightVal = right.getY();
    	Robot.drive.tankDrive(leftVal, rightVal);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.drive.stop();
    }

	protected void interrupted() {
		end();
	}
}
