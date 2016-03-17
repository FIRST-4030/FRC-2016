package org.ingrahamrobotics.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveEncoderTest extends CommandGroup {

	private static final int ticks = 4000;

	public DriveEncoderTest() {

		// Arm down
		addSequential(new ArmPreset_Home());
		addSequential(new ArmWait());

		// Drive forward
		addSequential(new DriveToEncoder(ticks, ticks));
		addSequential(new DriveWait());

		// Arm up
		addSequential(new ArmPreset_Shoot());
		addSequential(new ArmWait());

		// Shoot
		addSequential(new ShooterShoot());

		// Drive backward
		addSequential(new DriveToEncoder(ticks * -0.5, ticks * -0.5));
		addSequential(new DriveWait());

		// Return to manual control
		addSequential(new DriveStop());
	}
}
