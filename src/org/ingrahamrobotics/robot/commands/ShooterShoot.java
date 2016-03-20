package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ShooterShoot extends CommandGroup {

	public ShooterShoot() {
				
		// Lock
		addSequential(new ShooterLock());

		// Spin up the shooter
		addParallel(new ShooterPreset(Settings.Key.SHOOTER_SPEED));
		addSequential(new ShooterWait());

		// Fire
		addSequential(new KickerKick());
		addSequential(new KickerWait());
		addSequential(new KickerCapture());

		// Spin down
		addSequential(new ShooterStop());

		// Unlock
		addSequential(new ShooterUnlock());

		// Drop the arm
		addSequential(new ArmPreset_Home());
	}
}
