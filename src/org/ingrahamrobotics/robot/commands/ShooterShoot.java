package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ShooterShoot extends CommandGroup {
	
	public ShooterShoot() {
		int wait = Settings.Key.KICKER_SHOOT.getInt();

		// Spin up the shooter
		addParallel(new ShooterPreset(Settings.Key.SHOOTER_SPEED));
		addSequential(new ShooterWait());
		
		// Fire
		addSequential(new KickerKick());
		addSequential(new Wait(wait));
		
		// Return to the kicker capture position
		addSequential(new KickerCapture());
		
		// Drop the arm -- we don't have a ball
		addSequential(new ArmPreset_Home());
	}
}
