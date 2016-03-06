package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.subsystems.DriveFull.HalfTarget;
import org.ingrahamrobotics.robot.subsystems.DriveFull.Side;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveEncoderTest extends CommandGroup {

	private static final int ticks = 2000;
	private HalfTarget targets[] = new HalfTarget[Side.values().length];

	public DriveEncoderTest() {

		// Left and right motors
		targets[Side.kLEFT.i].side = Side.kLEFT;
		targets[Side.kRIGHT.i].side = Side.kRIGHT;

		// Drive forward
		targets[Side.kLEFT.i].setpoint = ticks;
		targets[Side.kRIGHT.i].setpoint = ticks;
		addSequential(new DriveToTarget(targets));
		addSequential(new DriveWait());

		/*
		// Arm up
		addSequential(new ArmPreset_Shoot());
		addSequential(new ArmWait());

		// Shoot
		addSequential(new ShooterShoot());

		// Drive backward
		targets[Side.kLEFT.i].setpoint = -ticks;
		targets[Side.kRIGHT.i].setpoint = -ticks;
		addSequential(new DriveToTarget(targets));
		addSequential(new DriveWait());
		*/

		// Return to manual control
		addSequential(new DriveStop());
	}
}
