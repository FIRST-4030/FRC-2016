package org.ingrahamrobotics.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveEncoderTest extends CommandGroup {
    
    public  DriveEncoderTest() {
    	
    	// Init arm
    	//addSequential(new ArmZero());
    	
    	// Drive forward 5000 ticks
    	addSequential(new DriveToEncoder(4000, 4000, 1.0));
    	    	
    	// Arm up
    	addSequential(new ArmPreset_Shoot());
    	addSequential(new ArmWait());
    	
    	// Shoot
    	addSequential(new ShooterShoot());
    	
    	// Drive backward 2500 ticks
    	addSequential(new DriveToEncoder(-4500, -4500, 1.0));
    	
    	// Return to manual control
    	addSequential(new DriveTest());
    }
}
