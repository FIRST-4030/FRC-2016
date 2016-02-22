package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class Shoot extends CommandGroup {
	Command ballCmd = new Collect();
	
	public Shoot() {
		int wait = Settings.Key.KICKER_TIME.getInt();

		// Stop what we're doing
		ballCmd.cancel();
		
		// Spin up the shooter
		ballCmd = new ShooterManual();
		ballCmd.start();
		addSequential(new Wait(wait));
		
		// Fire
		addSequential(new Kick());
		addSequential(new Wait(wait));
		
		// Return to capture mode
		addSequential(new Capture());
		ballCmd.cancel();
		ballCmd = new Collect();
		ballCmd.start();
	}
}