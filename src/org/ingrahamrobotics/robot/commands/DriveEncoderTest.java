package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.subsystems.DriveFull.HalfTarget;
import org.ingrahamrobotics.robot.subsystems.DriveFull.Side;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveEncoderTest extends CommandGroup {
    
	private HalfTarget targets[] = new HalfTarget[Side.values().length];

	public  DriveEncoderTest() {
		
		// Drive left and right motors
    	targets[Side.kLEFT.i].side = Side.kLEFT;
    	targets[Side.kRIGHT.i].side = Side.kRIGHT;
    	
    	// Drive forward 4000 ticks
    	targets[Side.kLEFT.i].setpoint = 4000;    	
    	targets[Side.kRIGHT.i].setpoint = 4000;    	
    	addSequential(new DriveToTarget(targets));
    	addSequential(new DriveWait());
    	    	
    	// Arm up
    	addSequential(new ArmPreset_Shoot());
    	addSequential(new ArmWait());
    	
    	// Shoot
    	addSequential(new ShooterShoot());
    	
    	// Drive backward 4000 ticks
    	targets[Side.kLEFT.i].setpoint = -4000;    	
    	targets[Side.kRIGHT.i].setpoint = -4000;    	
    	addSequential(new DriveToTarget(targets));
    	addSequential(new DriveWait());
    	
    	// Return to manual control
    	addSequential(new DriveStop());
    }
}
