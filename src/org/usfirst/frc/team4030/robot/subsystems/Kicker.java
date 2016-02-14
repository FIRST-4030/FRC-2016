package org.usfirst.frc.team4030.robot.subsystems;

import org.usfirst.frc.team4030.robot.RobotMap;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Kicker extends Subsystem {
	Talon motor = new Talon(RobotMap.pwmKicker);
	public static final double speed = 0.5;
	
	public Kicker() {
	}

	@Override
	protected void initDefaultCommand() {
		// No default command
	}

	public void kick() {
		motor.set(speed);
	}

	public void capture() {
		motor.set(-speed);
	}

	public void stop() {
		motor.disable();
	}
}
