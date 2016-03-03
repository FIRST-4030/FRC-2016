package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj.command.Command;

public class ArmZero extends Command {

	// Not configurable because this is a safety feature not a runtime feature
	public static final int kMIN_TICKS = -5000;

	public ArmZero() {
		requires(Robot.arm);
	}

	protected void initialize() {
		Robot.arm.zero();
	}

	protected void execute() {
		Robot.arm.checkReady();
		
		// Give up if we run down too far
		if (Sensors.Sensor.ARM_ENCODER.getInt() < kMIN_TICKS) {
			this.end();
			this.cancel();
		}
	}

	protected boolean isFinished() {
		return Robot.arm.ready();
	}

	protected void end() {
		Robot.arm.stop();
	}

	protected void interrupted() {
		this.end();
	}
}
