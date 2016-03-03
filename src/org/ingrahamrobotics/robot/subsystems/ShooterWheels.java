package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class ShooterWheels extends PIDSubsystem {

	// Not configurable because these are safety features not runtime features
	public static final double kMIN_SHOOTER_SPEED = 0.1;
	public static final double kTOLERANCE = 20.0;

	private Talon motor;
	
	public ShooterWheels() {
		super(1.0, 0.0, 0.0);
		motor = new Talon(RobotMap.pwmShooter);
		motor.setInverted(true);
		this.setPercentTolerance(kTOLERANCE);
	}
	
	public void setPower(double speed) {
		stop();
		motor.set(speed);
		Output.output(OutputLevel.PID, getName() + "-speed", speed);
	}
	
	public void start() {
		this.getPIDController().enable();
		this.isEnabled();
	}

	public void stop() {
		this.getPIDController().disable();
		this.isEnabled();

		motor.disable();
		Output.output(OutputLevel.PID, getName() + "-speed", 0);		
	}

	public void set(double setpoint) {
		this.setSetpoint(setpoint);
		Output.output(OutputLevel.PID, getName() + "-setpoint", setpoint);
	}

	public boolean isEnabled() {
		boolean enabled = this.getPIDController().isEnabled();
		Output.output(OutputLevel.PID, getName() + "-enabled", enabled);
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
		return Sensors.Sensor.SHOOTER_ENCODER.getDouble();
	}

	@Override
	protected void usePIDOutput(double output) {		
		// Ensure we always spin "forward" when enabled, as the encoder does not regulate direction
		if (output < kMIN_SHOOTER_SPEED) {
			output = kMIN_SHOOTER_SPEED;
		}
		if (this.isEnabled()) {
			motor.set(output);
			Output.output(OutputLevel.PID, getName() + "-speed", output);		
		}
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
}