package org.ingrahamrobotics.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Auto extends CommandGroup {

	public Auto() {
		
		// Always zero the arm
		addSequential(new ArmInit());
		
		// Consider other fun
	}
}
