package org.ingrahamrobotics.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

import org.ingrahamrobotics.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class Arm extends PIDSubsystem {

	private Talon motor = new Talon(RobotMap.pwmArm);
	private Encoder encoder = new Encoder(RobotMap.dioArmA, RobotMap.dioArmB);
	private DigitalInput home = new DigitalInput(RobotMap.dioArmSwitch);

	public Arm() {
		super(1.0, 0.0, 0.0);
		this.disable();
	}

	public void setPID(double p, double i, double d) {
		this.setPID(p, i, d);
	}

	public void start() {
		this.enable();
	}
	
	public void stop() {
		this.disable();
		motor.disable();
	}
	
	public boolean isHome() {
		return home.get();
	}
	
	public void reset() {
		encoder.reset();
	}
	
	public void set(double setpoint) {
		this.setSetpoint(setpoint);
	}

	@Override
	protected double returnPIDInput() {
		double pidInput = (encoder.getRaw());
		return pidInput;
	}

	@Override
	protected void usePIDOutput(double output) {
        if (!this.getPIDController().isEnabled()) {
            motor.set(output);
        }
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
}