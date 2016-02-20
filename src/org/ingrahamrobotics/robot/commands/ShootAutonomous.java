package org.ingrahamrobotics.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ShootAutonomous extends CommandGroup {
	
	/* Steps:
	 * Lower arm
	 * Drive under low bar or over obstacle (hopefully low bar)
	 * Turn to find goal
	 * Aim vertically
	 * Shoot
	 */

	public ShootAutonomous() {
		addSequential(new LowBarArm());
		//...
		
		addSequential(new AutoShoot());
	}

	public ShootAutonomous(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
