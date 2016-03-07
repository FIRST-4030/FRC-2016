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
	}

	public Talon getMotor() {
		return motor;
	}

	public String fullName() {
		return getName() + name;
	}

	public void start() {
		this.getPIDController().enable();
		isEnabled();
	}

	public void stop() {
		this.getPIDController().disable();
		isEnabled();

		motor.disable();
		Output.output(OutputLevel.MOTORS, fullName() + "-speed", 0);
	}

	public void set(double setpoint) {
		if (setpoint == kSTOP) {
			stop();
		} else {
			sensor.reset();
			start();
			this.setSetpoint(setpoint);
		}
		Output.output(OutputLevel.DRIVE_PID, fullName() + "-setpoint", setpoint);
	}

	public boolean isEnabled() {
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
		return sensor.getDouble();
	}

	@Override
	protected void usePIDOutput(double output) {
		if (isEnabled()) {
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