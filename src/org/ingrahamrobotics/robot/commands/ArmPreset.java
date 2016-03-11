package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.dashboard.Settings.Key;
import org.ingrahamrobotics.robot.pid.PIDPreset;

public class ArmPreset extends PIDPreset {

	public ArmPreset(Key key) {
		super(Robot.arm, Robot.armRun, Robot.armPreset, key);
	}

	public ArmPreset(int target) {
		super(Robot.arm, Robot.armRun, Robot.armPreset, (double)target);
	}
}
