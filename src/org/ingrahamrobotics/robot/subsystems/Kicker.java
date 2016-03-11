package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.dashboard.Output;
import org.ingrahamrobotics.robot.dashboard.OutputLevel;
import org.ingrahamrobotics.robot.dashboard.Settings;
import org.ingrahamrobotics.robot.sensors.Sensors;
import org.ingrahamrobotics.robot.vision.Log;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Kicker extends Subsystem {

	private Log log;
	private Talon motor = new Talon(RobotMap.pwmKicker);

	public Kicker() {
		log = new Log();
	}

	@Override
	protected void initDefaultCommand() {
		// No default command
	}

	public void kick() {
		double speed = Settings.Key.KICKER_SPEED.getDouble();
		set(speed);

		String str = "A:" + Sensors.Sensor.ARM_ENCODER.getInt() + ";S:" + Sensors.Sensor.SHOOTER_ENCODER.getDouble();
		log.save(str, System.currentTimeMillis() + "-kick.txt");
	}

	public void capture() {
		double speed = Settings.Key.KICKER_SPEED.getDouble();
		set(-speed);
	}

	private void set(double speed) {
		Output.output(OutputLevel.MOTORS, getName() + "-speed", speed);
		motor.set(speed);
	}

	public void stop() {
		Output.output(OutputLevel.MOTORS, getName() + "-speed", 0);
		motor.disable();
	}
}
