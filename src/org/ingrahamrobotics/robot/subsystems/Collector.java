package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Collector extends Subsystem {

	Talon motor;
	
	public Collector() {
		motor = new Talon(RobotMap.pwmCollector);
	}
	
	public void set(double speed) {
		motor.set(speed);
	}
	
	public void stop() {
		motor.disable();
	}
	
    public void initDefaultCommand() {
    	// No default command
    }
}

