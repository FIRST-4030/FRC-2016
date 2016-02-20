package org.ingrahamrobotics.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class Arm extends PIDSubsystem {

	private Talon motor = new Talon(RobotMap.pwmArm);
	private Encoder encoder = new Encoder(RobotMap.dioArmA, RobotMap.dioArmB);
	private DigitalInput zeroSwitch = new DigitalInput(RobotMap.dioArmSwitch);	
	private boolean ready = false;

	public Arm() {
		super(1.0, 0.0, 0.0);
		ready = false;
	}

	public void start() {
		this.getPIDController().enable();
	}
	
	public void stop() {
		this.getPIDController().disable();
		motor.disable();
	}
	
	public boolean ready() {
		Output.output(OutputLevel.PID, getName() + "-ready", ready);
		return ready;
	}
	
	public void zero() {
		stop();
		ready = false;
		double speed = Settings.Key.ARM_ZERO_SPEED.getDouble();
		motor.set(speed);
		Output.output(OutputLevel.PID, getName() + "-zero", true);
	}

	private void atZero() {
		encoder.reset();
		ready = true;
		Output.output(OutputLevel.PID, getName() + "-zero", false);
	}
	
	public boolean checkZero() {
		boolean atZero = !zeroSwitch.get();
		Output.output(OutputLevel.PID, getName() + "-atZero", atZero);
		if (!ready && atZero) {
			atZero();
		}
		return atZero;
	}
	
	public void set(double setpoint) {
		Output.output(OutputLevel.PID, getName() + "-setpoint", setpoint);
		this.setSetpoint(setpoint);
	}
	
	public boolean isEnabled() {
		return this.getPIDController().isEnabled();
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
		double pidInput = (encoder.getRaw());
		checkZero();
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