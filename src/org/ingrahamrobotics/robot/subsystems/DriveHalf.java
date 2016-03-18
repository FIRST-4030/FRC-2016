package org.ingrahamrobotics.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.subsystems.Sensors.Sensor;

public class DriveHalf extends PIDSubsystem {

	// Not configurable because these are programming features
	public static final int kSTOP = 0;

	private Talon motor;
	private String name;
	private Sensor sensor;

	public DriveHalf(String name, int motorIndex, boolean invert, Sensor sensor) {
		super(1.0, 0.0, 0.0);
		this.name = name;
		motor = new Talon(motorIndex);
		motor.setInverted(invert);
		this.sensor = sensor;
		System.err.println(fullName() + ": init");
	}

	public Talon getMotor() {
		return motor;
	}

	public String fullName() {
		return getName() + name;
	}

	public void start() {
		System.err.println(fullName() + ": start");
		this.getPIDController().enable();
		enabled();
	}

	public void stop() {
		System.err.println(fullName() + ": stop");
		this.getPIDController().disable();
		enabled();

		motor.disable();
		Output.output(OutputLevel.MOTORS, fullName() + "-speed", 0);
	}

	public void set(double setpoint) {
		System.err.println(fullName() + " set: " + setpoint);
		if (setpoint == kSTOP) {
			stop();
		} else {
			sensor.reset();
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
		System.err.println(fullName() + " setPower: " + speed);
		stop();
		if (speed != kSTOP) {
			motor.set(speed);
		}
		Output.output(OutputLevel.MOTORS, fullName() + "-speed", speed);
	}

	@Override
	public double returnPIDInput() {
		System.err.println(fullName() + " actual: " + sensor.getDouble());
		System.err.println(fullName() + " setpoint: " + this.getSetpoint());
		System.err.println(fullName() + " p: " + this.getPIDController().getP());
		return sensor.getDouble();
	}

	@Override
	protected void usePIDOutput(double output) {
		System.err.println(fullName() + " raw speed: " + output);
		if (enabled()) {
			if (motor.getInverted()) {
				output *= -1.0;
			}
			System.err.println(fullName() + " actual speed: " + output);
			motor.set(output);
			Output.output(OutputLevel.MOTORS, fullName() + "-speed", output);
		}
	}

	@Override
	protected void initDefaultCommand() {
		// No default command
	}
}