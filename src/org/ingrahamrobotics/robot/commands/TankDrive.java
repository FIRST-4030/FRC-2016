
package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class TankDrive extends Command {
	
	private static final boolean testMode = true;

	private Joystick left = Robot.oi.joyLeft;
	private Joystick right = Robot.oi.joyRight;
		
    public TankDrive() {
        requires(Robot.drive);
    }

    protected void initialize() {
    	if (testMode) {
    		left = Robot.oi.joyTest;
    		right = Robot.oi.joyTest;
    		right.setAxisChannel(Joystick.AxisType.kZ, 5);
    	}
    }

    protected void execute() {
    	double leftVal = left.getY();
    	double rightVal = right.getY();
    	if (testMode) {
    		rightVal = right.getZ();
    	}
    	Robot.drive.drive(leftVal, rightVal);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.drive.stop();
    }

	protected void interrupted() {
		this.end();
	}
}
