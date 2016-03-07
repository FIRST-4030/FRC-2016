package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings;

public class ShooterWait extends WaitPID {

	// Not configurable because these are programming features
	public static final double kTOLERANCE = 0;
	public static final int kMIN_SUCCESS = 3;
	
	private long doneTS;

	public ShooterWait() {
		super(Robot.shooter, kTOLERANCE, Mode.kABS_MIN, kMIN_SUCCESS);
	}
	
	@Override
	public void initialize() {
		if (Robot.disableShooterPID) {
			doneTS = System.currentTimeMillis() + Settings.Key.KICKER_SHOOT.getInt();
		} else {
			super.initialize();
		}
	}
	
	@Override
	public boolean isFinished() {
		if (Robot.disableShooterPID) {
			return (System.currentTimeMillis() > doneTS);
		} else {
			return super.isFinished();
		}
	}
}
