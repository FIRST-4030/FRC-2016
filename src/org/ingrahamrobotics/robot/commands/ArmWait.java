package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.pid.WaitPID;

public class ArmWait extends WaitPID {

	// Not configurable because these are programming features
	public static final int kTOLERANCE = 25;
	public static final int kMIN_SUCCESS = 2;

	public ArmWait() {
		super(Robot.arm, kTOLERANCE, Mode.kABS_RANGE, kMIN_SUCCESS);
	}
}
