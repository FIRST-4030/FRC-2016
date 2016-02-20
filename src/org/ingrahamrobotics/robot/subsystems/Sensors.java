package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.commands.ReadSensors;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Sensors extends Subsystem {

	private static final Encoder armEncoder = new Encoder(RobotMap.dioArmA,
			RobotMap.dioArmB);
	private static final DigitalInput armSwitch = new DigitalInput(
			RobotMap.dioArmSwitch);
	private static final Counter shooterEncoder = new Counter(RobotMap.dioShooter);
	/*private static final Encoder driveLeftEncoder = new Encoder(
			RobotMap.dioDriveLeftA, RobotMap.dioDriveLeftB);
	private static final Encoder driveRightEncoder = new Encoder(
			RobotMap.dioDriveRightA, RobotMap.dioDriveRightB);
	*/

	public enum Sensor {
		ARM_SWITCH(
			"Arm Homing Switch", armSwitch, SensorType.SWITCH),
		ARM_ENCODER(
			"Arm Encoder", armEncoder, SensorType.ENCODER),
		SHOOTER_ENCODER(
			"Shooter Speed", shooterEncoder, SensorType.COUNTER_RATE),
		/*DRIVE_ENCODER_LEFT(
			"Drive Encoder Left", driveLeftEncoder, SensorType.ENCODER),
		DRIVE_ENCODER_RIGHT(
			"Drive Encoder Right", driveRightEncoder, SensorType.ENCODER),*/
		;

		public final String name;
		public final Object device;
		public final SensorType type;
		public String value;

		private Sensor(String name, Object device, SensorType type) {
			this.name = name;
			this.device = device;
			this.type = type;
			this.value = "";
		}

		public String toString() {
			return name;
		}

		public void reset() {
			value = "";
			switch (type) {
			case COUNTER:
			case COUNTER_RATE:
				((Counter) device).reset();
				break;
			case ENCODER:
			case ENCODER_RATE:
				((Encoder) device).reset();
				break;
			default:
				break;
			}
		}

		public String get() {
			return value;
		}

		public int getInt() {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException ex) {
				System.err.println("Warning: Value '" + value + "' of '" + name
						+ "' is not valid.");
				return 0;
			}
		}

		public double getDouble() {
			try {
				return Double.parseDouble(value);
			} catch (NumberFormatException ex) {
				System.err.println("Warning: Value '" + value + "' of '" + name
						+ "' is not valid.");
				return 0.0;
			}
		}

		public boolean getBoolean() {
			try {
				return Boolean.parseBoolean(value);
			} catch (NumberFormatException ex) {
				System.err.println("Warning: Value '" + value + "' of '" + name
						+ "' is not valid.");
				return false;
			}
		}
	}

	public enum SensorType {
		COUNTER("Counter"),
		COUNTER_RATE("Counter Rate"),
		ENCODER("Encoder"),
		ENCODER_RATE("Encoder Rate"),
		SWITCH("Switch");

		public final String name;

		private SensorType(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}

	public void update() {
		for (Sensor sensor : Sensor.values()) {
			boolean b;
			int i;
			double d;

			switch (sensor.type) {
			case SWITCH:
				b = ((DigitalInput) sensor.device).get();
				Output.output(OutputLevel.SENSORS, sensor.name, b);
				sensor.value = Boolean.toString(b);
				break;
			case COUNTER:
				i = ((Counter) sensor.device).get();
				Output.output(OutputLevel.SENSORS, sensor.name, i);
				sensor.value = Integer.toString(i);
				break;
			case COUNTER_RATE:
				d = ((Counter) sensor.device).getRate();
				Output.output(OutputLevel.SENSORS, sensor.name, d);
				sensor.value = Double.toString(d);
				break;
			case ENCODER:
				i = ((Encoder) sensor.device).getRaw();
				Output.output(OutputLevel.SENSORS, sensor.name, i);
				sensor.value = Integer.toString(i);
				break;
			case ENCODER_RATE:
				d = ((Encoder) sensor.device).getRaw();
				Output.output(OutputLevel.SENSORS, sensor.name, d);
				sensor.value = Double.toString(d);
				break;
			}
		}
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new ReadSensors());
	}
}