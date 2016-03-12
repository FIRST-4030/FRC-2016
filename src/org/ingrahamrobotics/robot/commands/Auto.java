package org.ingrahamrobotics.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Auto extends CommandGroup {
	
	private static final int ticks = 4500;

	public Auto() {
		
		// Wait for the arm to be ready
		addSequential(new ArmWaitReady());
		
		// Arm up
		addSequential(new ArmPreset_Shoot());
		addSequential(new ArmWait());

		// Drive forward
		addSequential(new DriveToTarget(ticks, ticks));
		addSequential(new DriveWait());
		
		// Stop any twitching from PID drive now that we are in position
		addSequential(new DriveStop());
		
		// Arm down
		addSequential(new ShooterShoot());
	}
}
