package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.commands.ShooterRun;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class ShooterWheels extends PIDSubsystem {

	// Not configurable because these are safety features
	public static final double kMIN_SHOOTER_SPEED = 0.1;

	// Not configurable because these are programming features
	public static final int kSTOP = 0;

	private Talon motor;
	private boolean ready;

	public ShooterWheels() {
		super(1.0, 0.0, 0.0);
		motor = new Talon(RobotMap.pwmShooter);
		motor.setInverted(true);
	}

	public void setPower(double speed) {
		stop();
		if (speed != kSTOP) {
			motor.set(speed);
		}
		Output.output(OutputLevel.MOTORS, getName() + "-speed", speed);
	}

	public void start() {
		ready = false;
		this.getPIDController().enable();
		enabled();
	}

	public void stop() {
		this.getPIDController().disable();
		enabled();

		motor.disable();
		Output.output(OutputLevel.MOTORS, getName() + "-speed", 0);
	}

	public void set(double setpoint) {
		if (setpoint == kSTOP) {
			stop();
		} else {
			start();
			this.setSetpoint(setpoint);
			ready = true;
		}
		Output.output(OutputLevel.SHOOTER_PID, getName() + "-setpoint", setpoint);
	}

	public boolean enabled() {
		boolean enabled = this.getPIDController().isEnabled();
		Output.output(OutputLevel.SHOOTER_PID, getName() + "-enabled", enabled);
		return enabled;
	}

	public void updatePID() {
		double p = Settings.Key.SHOOTER_PID_P.getDouble();
		double i = Settings.Key.SHOOTER_PID_I.getDouble();
		double d = Settings.Key.SHOOTER_PID_D.getDouble();
		this.getPIDController().setPID(p, i, d);
	}

	@Override
	protected double returnPIDInput() {
		if (Robot.disableShooterPID) {
			return 0.0;
		}
		return Sensors.Sensor.SHOOTER_ENCODER.getDouble();
	}

	@Override
	protected void usePIDOutput(double output) {

		// Ensure we always spin "forward" when enabled, as the encoder does not
		// regulate direction
		if (output < kMIN_SHOOTER_SPEED) {
			output = kMIN_SHOOTER_SPEED;
		}

		// Always run at 100% if PID is disabled
		if (Robot.disableShooterPID) {
			output = 1.0;
		}

		if (enabled() && ready) {
			motor.set(output);
			Output.output(OutputLevel.MOTORS, getName() + "-speed", output);
		}
	}

	@Override
	protected void initDefaultCommand() {
		this.setDefaultCommand(new ShooterRun());
	}
}