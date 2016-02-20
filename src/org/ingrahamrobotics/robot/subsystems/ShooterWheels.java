package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class ShooterWheels extends PIDSubsystem {

	Talon motor;
	
	public ShooterWheels() {
		super(1.0, 0.0, 0.0);
		motor = new Talon(RobotMap.pwmShooter);
	}
		
	public void start() {
		this.getPIDController().enable();
		Output.output(OutputLevel.PID, getName() + "-enabled", this.isEnabled());
	}
	
	public void stop() {
		this.getPIDController().disable();
		motor.disable();
		Output.output(OutputLevel.PID, getName() + "-enabled", this.isEnabled());
	}

	public void set(double setpoint) {
		Output.output(OutputLevel.PID, getName() + "-setpoint", setpoint);
		this.setSetpoint(setpoint);
	}
	
	public boolean isEnabled() {
		return this.getPIDController().isEnabled();
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
		Output.output(OutputLevel.PID, getName() + "-speed", output);
		Output.output(OutputLevel.PID, getName() + "-enabled", this.isEnabled());
        if (this.isEnabled()) {
            motor.set(output);
        }
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
}