package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Kicker extends Subsystem {
	
	Talon motor = new Talon(RobotMap.pwmKicker);

	@Override
	protected void initDefaultCommand() {
		// No default command
	}

	public void kick() {
		double speed = Settings.Key.KICKER_SPEED.getDouble();
		set(speed);
	}

	public void capture() {
		double speed = Settings.Key.KICKER_SPEED.getDouble();
		set(-speed);
	}
	
	private void set (double speed) {
		Output.output(OutputLevel.MOTORS, getName() + "-speed", speed);
		motor.set(speed);
	}

	public void stop() {
		Output.output(OutputLevel.MOTORS, getName() + "-speed", 0);
		motor.disable();
	}
}
