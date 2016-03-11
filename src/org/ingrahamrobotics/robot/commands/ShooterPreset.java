package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.dashboard.Settings.Key;
import org.ingrahamrobotics.robot.pid.PIDPreset;

public class ShooterPreset extends PIDPreset {

	public ShooterPreset(Key key) {
		super(Robot.shooter, Robot.shooterRun, Robot.shooterPreset, key);
	}

	public ShooterPreset(int target) {
		super(Robot.shooter, Robot.shooterRun, Robot.shooterPreset, (double)target);
	}
}
