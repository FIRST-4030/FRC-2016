package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Shoot extends CommandGroup {
	public Shoot() {
		int wait = Settings.Key.KICKER_TIME.getInt();

		addSequential(new Kick());
		addSequential(new Wait(wait));
		addSequential(new Capture());
	}
}