package org.ingrahamrobotics.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ArmInit extends CommandGroup {

	// Run the arm down to the reset switch
	public ArmInit() {
		// This command protects itself (a bit) against starting under the switch
		// If it did not we would use a timeout here for safety
		addSequential(new ArmZero());
		
		// Start regulation at HOME
		addSequential(new ArmPreset_Home());
	}
}
