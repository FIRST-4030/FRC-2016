package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj.command.Command;

public class ArmZero extends Command {

	// Not configurable because these are safety features not a runtime features
	public static final int kMIN_TICKS = -8000;
	public static final int kTIMEOUT = 4000;

	private long timeout;

	public ArmZero() {
		requires(Robot.arm);
	}

	protected void initialize() {
		timeout = System.currentTimeMillis() + kTIMEOUT;
		Robot.arm.zero();
	}

	protected void execute() {
		Robot.arm.checkReady();

		// Give up if we run down too far
		if (Sensors.Sensor.ARM_ENCODER.getInt() < kMIN_TICKS) {
			abort();
		}

		// Give up if we run for too long
		if (System.currentTimeMillis() > timeout) {
			abort();
		}
	}

	private void abort() {
		end();
		this.cancel();
	}

	protected boolean isFinished() {
		return Robot.arm.ready();
	}

	protected void end() {
		Robot.arm.stop();
	}

	protected void interrupted() {
		end();
	}
}
