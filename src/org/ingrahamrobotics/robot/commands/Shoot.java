package org.ingrahamrobotics.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Shoot extends CommandGroup {
    
    public  Shoot() {
    	addSequential(new Kick());
    	addSequential(new Wait(750));
    	addSequential(new Capture());
    }
}