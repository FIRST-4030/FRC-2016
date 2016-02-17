package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class ShooterWheels extends PIDSubsystem {

	Talon motor = new Talon(RobotMap.pwmShooter);
	Counter encoder = new Counter(RobotMap.dioShooter);
	
	public ShooterWheels() {
		super(1.0, 0.0, 0.0);
	}
	
	public void setPID(double p, double i, double d) {
		this.setPID(p, i, d);
	}
	
	public void start() {
		this.getPIDController().enable();
	}
	
	public void stop() {
		this.getPIDController().disable();
		motor.disable();
	}

	public void set(double setpoint) {
		Output.output(OutputLevel.PID, getName() + "-setpoint", setpoint);
		this.setSetpoint(setpoint);
	}
	
	public boolean isEnabled() {
		return this.getPIDController().isEnabled();
	}

	@Override
	protected double returnPIDInput() {
		double pidInput = (encoder.getRate());
		Output.output(OutputLevel.PID, getName() + "-encoder", pidInput);
		return pidInput;
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