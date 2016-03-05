package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

public class ArmWait extends WaitPID {
	
	// Not configurable because these are programming features not runtime features
	public static final int kTOLERANCE = 25;
	public static final int kMIN_SUCCESS = 2;

	public ArmWait() {
		super(Robot.arm, 0, Mode.kABS_RANGE, kMIN_SUCCESS);
	}
}
