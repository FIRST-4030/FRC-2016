package org.usfirst.frc.team4030.robot.subsystems;

import org.usfirst.frc.team4030.robot.RobotMap;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Kicker extends Subsystem {	
	Talon motor = new Talon(RobotMap.pwmKicker);

	public Kicker() {
	}
	
	public void stop() {
		motor.disable();
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
}
