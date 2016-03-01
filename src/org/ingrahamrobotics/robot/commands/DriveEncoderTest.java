package org.ingrahamrobotics.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveEncoderTest extends CommandGroup {
    
    public  DriveEncoderTest() {
    	
    	// Drive forward 5000 ticks
    	addSequential(new DriveToEncoder(5000, 5000, 0.5));
    	
    	// Drive backward 2500 ticks
    	addSequential(new DriveToEncoder(2500, 2500, 0.5));
    }
}
