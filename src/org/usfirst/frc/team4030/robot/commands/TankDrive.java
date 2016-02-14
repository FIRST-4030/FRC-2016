
package org.usfirst.frc.team4030.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team4030.robot.Robot;

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
