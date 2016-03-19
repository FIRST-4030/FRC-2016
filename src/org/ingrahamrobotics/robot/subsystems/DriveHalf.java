package org.ingrahamrobotics.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.subsystems.DriveSide.Side;
import org.ingrahamrobotics.robot.subsystems.Sensors.Sensor;
import org.ingrahamrobotics.robot.subsystems.Sensors.SensorType;

public class DriveHalf extends PIDSubsystem {

	// Not configurable because these are programming features
	public static final int kSTOP = 0;

	private Talon motor;
	private Side side;
	private Sensor sensor;
	private DriveHalf partner;
	private double output;
	private double offset;

	private static final boolean kDEBUG = false;

	public DriveHalf(Side side, int motorIndex, boolean invert) {
		super(1.0, 0.0, 0.0);
		this.side = side;
		motor = new Talon(motorIndex);
		motor.setInverted(invert);
		sensor = null;
		partner = null;
		if (kDEBUG) {
			System.err.println(fullName() + ": init");
		}
	}

	public void setPartner(DriveHalf partner) {
		this.partner = partner;
		Output.output(OutputLevel.MOTORS, fullName() + "-partner", partner);
	}

	public Talon getMotor() {
		return motor;
	}

	public boolean isSensorType(SensorType type) {
		if (sensor == null) {
			return false;
		}
		return (sensor.type == type);
	}

	public String fullName() {
		return getName() + side.name;
	}

	public void start() {
		if (kDEBUG) {
			System.err.println(fullName() + ": start");
		}
		this.getPIDController().enable();
		enabled();
	}

	public void stop() {
		if (kDEBUG) {
			System.err.println(fullName() + ": stop");
		}
		this.getPIDController().disable();
		enabled();

		motor.disable();
		Output.output(OutputLevel.MOTORS, fullName() + "-speed", 0);
	}

	public void set(double setpoint, Sensor sensor) {
		if (kDEBUG) {
			System.err.println(fullName() + " set: " + setpoint);
		}
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
		if (kDEBUG) {
			System.err.println(fullName() + " setPower: " + speed);
		}
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
		if (kDEBUG) {
			System.err.println(fullName() + " actual: " + sensor.getDouble());
			System.err.println(fullName() + " setpoint: " + this.getSetpoint());
			System.err.println(fullName() + " p: " + this.getPIDController().getP());
		}
		return sensor.getDouble();
	}

	public double output() {
		return output;
	}

	public void offset(double offset) {
		// Ensure the offset doesn't change between check and use
		synchronized (this) {
			this.offset = offset;
		}
	}

	@Override
	protected void usePIDOutput(double output) {
		System.err.println(fullName() + " raw speed: " + output);
		if (enabled()) {

			// Ensure the offset doesn't change between check and use
			synchronized (this) {
				if (partner != null) {
					// Work with our partner to adjust our relative speeds
					double diff = output / 2.0;
					partner.offset(diff);
					Output.output(OutputLevel.MOTORS, fullName() + "-offset", 0.0);
					Output.output(OutputLevel.MOTORS, fullName() + "-diff", diff);

					// Our speed is differential
					output = partner.output() + diff;
				} else {
					Output.output(OutputLevel.MOTORS, fullName() + "-diff", 0.0);
					Output.output(OutputLevel.MOTORS, fullName() + "-offset", offset);

					// Slow down if our partner asks us to
					if (offset != kSTOP) {
						output -= offset;
						// But only for this round
						offset = kSTOP;
					}
				}
			}

			// Turn invert if regulating to an angle
			if (isSensorType(SensorType.GYRO) && side.invertTurn) {
				output *= -1.0;
			}

			// Frame invert as final step
			if (motor.getInverted()) {
				output *= -1.0;
			}

			this.output = output;
			System.err.println(fullName() + " final speed: " + output);
			motor.set(this.output);
			Output.output(OutputLevel.MOTORS, fullName() + "-speed", this.output);

		}
	}

	@Override
	protected void initDefaultCommand() {
		// No default command
	}
}