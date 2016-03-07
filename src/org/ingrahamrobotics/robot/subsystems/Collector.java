package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Collector extends Subsystem {

	Talon motor;

	public Collector() {
		motor = new Talon(RobotMap.pwmCollector);
	}

	public void set(double speed) {
		motor.set(speed);
		Output.output(OutputLevel.MOTORS, getName() + "-speed", speed);
	}

	public void stop() {
		motor.disable();
	}

	public void initDefaultCommand() {
		// No default command
	}
}
