
package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;

public class DriveTest extends DriveTank {
	
	private Joystick stick;
	
	@Override
	protected void initialize() {
		stick =  Robot.oi.joyTest;
		stick.setAxisChannel(Joystick.AxisType.kZ, 5);
	}

	@Override
	protected void execute() {
		double leftVal = stick.getY();
		double rightVal = stick.getZ();
		Robot.drive.tankDrive(leftVal, rightVal);
	}
}
