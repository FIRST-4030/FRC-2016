package org.ingrahamrobotics.robot.subsystems;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.ingrahamrobotics.robot.Robot;
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
		save();
	}

	private void save() {
		if (!CameraTarget.kENABLE_VISION_LOG) {
			return;
		}

		Path file = Paths.get(Robot.camTarget.getVisionDir().toString(), System.currentTimeMillis() + "-kick.txt");
		try {
			PrintWriter out = new PrintWriter(file.toFile());
			out.println("A:" + Sensors.Sensor.ARM_ENCODER.getInt() + ";S:" + Sensors.Sensor.SHOOTER_ENCODER.getDouble());
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("Unable to save kick file");
		}
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
