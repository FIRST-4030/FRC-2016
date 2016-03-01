package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ArmInit extends CommandGroup {

	// Run the arm down to the reset switch
	public ArmInit() {
		// This command protects itself against starting under the switch
		// If it did not we would use a timeout here for safety
		addSequential(new ArmZero());
	}
	
	// If zeroing worked the setpoint will be "home" and we can start regulation
	@Override
	protected void end() {
		if (Robot.arm.ready()) {
			Robot.armRun.start();
		}
	}
}
