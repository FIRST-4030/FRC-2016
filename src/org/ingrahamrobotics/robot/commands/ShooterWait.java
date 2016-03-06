package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

public class ShooterWait extends WaitPID {

	// Not configurable because these are programming features
	public static final double kTOLERANCE = 0;
	public static final int kMIN_SUCCESS = 3;

	public ShooterWait() {
		super(Robot.shooter, kTOLERANCE, Mode.kABS_MIN, kMIN_SUCCESS);
	}
}
