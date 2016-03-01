package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ShooterShoot extends CommandGroup {
	
	public ShooterShoot() {
		int wait = Settings.Key.KICKER_SHOOT.getInt();

		// Spin up the shooter
		addParallel(new ShooterRun());
		addSequential(new ShooterWait());
		
		// Fire
		addSequential(new KickerKick());
		addSequential(new Wait(wait));
		
		// Return to capture mode (but do not run)
		addSequential(new KickerCapture());
	}
}