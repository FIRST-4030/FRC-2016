
package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class TestDrive extends Command {

	private Joystick stick;

	public TestDrive() {
		requires(Robot.drive);
	}

	protected void initialize() {
		stick =  Robot.oi.joyTest;
		stick.setAxisChannel(Joystick.AxisType.kZ, 5);
	}

	protected void execute() {
		double leftVal = stick.getY();
		double rightVal = stick.getZ();
		Robot.drive.tankDrive(leftVal, rightVal);
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
