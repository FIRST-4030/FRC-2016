package org.ingrahamrobotics.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoLow extends CommandGroup {
	
	private static final int ticks = 18002;

	public AutoLow() {
		
		// Wait for the arm to be ready
		addSequential(new ArmWaitReady());
		
		// Arm up
		addSequential(new ArmPreset_Home());
		// This should arm-wait, but with the encoder being frequently down...
		addSequential(new Wait(750));

		// Drive forward
		addSequential(new DriveToTarget(ticks, ticks));
		addSequential(new DriveWait());
		
		// Stop any twitching from PID drive now that we are in position
		addSequential(new DriveStop());
	}
}
