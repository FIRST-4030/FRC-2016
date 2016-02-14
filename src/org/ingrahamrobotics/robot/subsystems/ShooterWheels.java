package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class ShooterWheels extends PIDSubsystem {

	Talon motor = new Talon(RobotMap.pwmShooter);
	Counter encoder = new Counter(RobotMap.dioShooter);
	
	public ShooterWheels() {
		super(1.0, 0.0, 0.0);
	}
	
	public void stop() {
		motor.disable();
	}

	@Override
	protected double returnPIDInput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
}