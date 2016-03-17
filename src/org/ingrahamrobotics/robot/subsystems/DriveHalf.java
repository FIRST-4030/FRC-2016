package org.ingrahamrobotics.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.subsystems.Sensors.Sensor;
import org.ingrahamrobotics.robot.subsystems.Sensors.SensorType;

public class DriveHalf extends PIDSubsystem {

	// Not configurable because these are programming features
	public static final int kSTOP = 0;

	private Talon motor;
	private String name;
	private Sensor sensor;

	public DriveHalf(String name, int motorIndex, boolean invert) {
		super(1.0, 0.0, 0.0);
		this.name = name;
		motor = new Talon(motorIndex);
		motor.setInverted(invert);
		sensor = null;
	}

	public Talon getMotor() {
		return motor;
	}
	
	public boolean isSensorType(SensorType type) {
		return (sensor.type == type);
	}

	public String fullName() {
		return getName() + name;
	}

	public void start() {
		this.getPIDController().enable();
		enabled();
	}

	public void stop() {
		this.getPIDController().disable();
		enabled();

		motor.disable();
		Output.output(OutputLevel.MOTORS, fullName() + "-speed", 0);
	}

	public void set(double setpoint, Sensor sensor) {
		if (setpoint == kSTOP) {
			stop();
		} else {
			this.sensor = sensor;
			this.sensor.reset();
			start();
			this.setSetpoint(setpoint);
		}
		Output.output(OutputLevel.DRIVE_PID, fullName() + "-setpoint", setpoint);
	}

	public boolean enabled() {
		boolean enabled = this.getPIDController().isEnabled();
		Output.output(OutputLevel.DRIVE_PID, fullName() + "-enabled", enabled);
		return enabled;
	}

	public void setPower(double speed) {
		stop();
		if (speed != kSTOP) {
			motor.set(speed);
		}
		Output.output(OutputLevel.MOTORS, fullName() + "-speed", speed);
	}

	@Override
	public double returnPIDInput() {
		if (!enabled()) {
			return 0.0;
		}
		return sensor.getDouble();
	}

	@Override
	protected void usePIDOutput(double output) {
		if (enabled()) {
			if (motor.getInverted()) {
				output *= -1.0;
			}
			motor.set(output);
			Output.output(OutputLevel.MOTORS, fullName() + "-speed", output);
		}
	}

	@Override
	protected void initDefaultCommand() {
		// No default command
	}
}