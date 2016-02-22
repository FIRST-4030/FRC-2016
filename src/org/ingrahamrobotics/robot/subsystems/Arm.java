package org.ingrahamrobotics.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.commands.TankDrive;
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
		Output.output(OutputLevel.PID, getName() + "-ready", ready);
	}

	private void atZero() {
		Sensors.Sensor.ARM_ENCODER.reset();
		ready = true;
		Output.output(OutputLevel.PID, getName() + "-ready", ready);
		this.set(Settings.Key.ARM_PRESET_COLLECT.getInt());
	}
	
	public boolean checkZero() {
		boolean atZero = Sensors.Sensor.ARM_SWITCH.getBoolean();
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
		checkZero();
		return Sensors.Sensor.ARM_ENCODER.getDouble();
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
		//setDefaultCommand(new Command());
	}
}