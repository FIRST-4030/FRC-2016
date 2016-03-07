package org.ingrahamrobotics.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

public class Arm extends PIDSubsystem {

	private Talon motor;
	private boolean ready = false;

	public Arm() {
		super(1.0, 0.0, 0.0);
		ready = false;
		motor = new Talon(RobotMap.pwmArm);
		motor.setInverted(true);
	}

	public void start() {
		this.getPIDController().enable();
		isEnabled();
	}

	public void stop() {
		this.getPIDController().disable();
		isEnabled();

		motor.disable();
		Output.output(OutputLevel.MOTORS, getName() + "-speed", 0);
	}

	public boolean ready() {
		Output.output(OutputLevel.ARM_PID, getName() + "-ready", ready);
		return ready;
	}

	public void zero() {
		stop();
		ready = false;
		ready();

		Sensors.Sensor.ARM_ENCODER.reset();
		double speed = Settings.Key.ARM_ZERO_SPEED.getDouble();
		motor.set(speed);
		Output.output(OutputLevel.MOTORS, getName() + "-speed", speed);
	}

	private void setReady() {
		Sensors.Sensor.ARM_ENCODER.reset();
		ready = true;
		ready();

		// Default to "home"
		this.set(Settings.Key.ARM_PRESET_HOME.getInt());
	}

	public boolean checkReady() {
		boolean atZero = Sensors.Sensor.ARM_SWITCH.getBoolean();
		if (!ready && atZero) {
			stop();
			setReady();
		}
		return ready;
	}

	public void set(double setpoint) {
		start();
		this.setSetpoint(setpoint);
		Output.output(OutputLevel.ARM_PID, getName() + "-setpoint", setpoint);
	}

	public boolean isEnabled() {
		boolean enabled = this.getPIDController().isEnabled();
		Output.output(OutputLevel.ARM_PID, getName() + "-enabled", enabled);
		return enabled;
	}

	/**
	 * Sets the arm pid settings from Settings.
	 */
	public void updatePID() {
		double p = Settings.Key.ARM_PID_P.getDouble();
		double i = Settings.Key.ARM_PID_I.getDouble();
		double d = Settings.Key.ARM_PID_D.getDouble();
		this.getPIDController().setPID(p, i, d);
	}

	@Override
	protected double returnPIDInput() {
		return Sensors.Sensor.ARM_ENCODER.getDouble();
	}

	@Override
	protected void usePIDOutput(double output) {
		if (isEnabled()) {
			motor.set(output);
			Output.output(OutputLevel.MOTORS, getName() + "-speed", output);
		}
	}

	@Override
	protected void initDefaultCommand() {
		// No default command
	}
}