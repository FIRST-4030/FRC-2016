package org.ingrahamrobotics.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveStraightTest extends CommandGroup {

	public DriveStraightTest() {

		// Wait for the arm to be ready
		addSequential(new ArmWaitReady());

		// Turn
		addSequential(new DriveStraightToEncoder(8000));
		addSequential(new DriveWait());

		// Stop any twitching from PID drive now that we are in position
		addSequential(new DriveStop());
	}
}
