
package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class TankDrive extends Command {
	
	private Joystick left = Robot.oi.driveLeft;
	private Joystick right = Robot.oi.driveRight;
	
    public TankDrive() {
        requires(Robot.drive);
    }

    protected void initialize() {
    }

    protected void execute() {
    	Robot.drive.drive(left, right);
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
