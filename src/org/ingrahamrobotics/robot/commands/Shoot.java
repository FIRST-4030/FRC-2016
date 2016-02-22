package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Shoot extends CommandGroup {
	
	public Shoot() {
		int wait = Settings.Key.KICKER_SHOOT_TIME.getInt();

		// Spin up the shooter
		addParallel(new ShooterRun());
		addSequential(new ShooterWait());
		
		// Fire
		addSequential(new Kick());
		addSequential(new Wait(wait));
		
		// Return to capture mode (but do not run)
		addSequential(new Capture());
	}
}