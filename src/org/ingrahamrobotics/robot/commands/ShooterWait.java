package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterWait extends Command {

	public static final int kREADY_MIN = 3;
	private int ready = 0;

	public ShooterWait() {
	}

	protected void initialize() {
		ready = 0;
	}

	protected void execute() {
	}

	// Done when we're at or above the PID setpoint for kREADY_MIN samples
	protected boolean isFinished() {
		double actual = Sensors.Sensor.SHOOTER_ENCODER.getDouble();
		double setpoint = Robot.shooter.getSetpoint();

		// Waiting for 0 makes no sense
		// Presumably someone is about to set a valid setpoint so just wait
		if (setpoint < 1) {
			return false;
		}

		// Mark as ready if we're at or above the setpoint
		if (actual >= setpoint) {
			ready++;
		}

		// Done after the minimum number of ready samples
		if (ready > kREADY_MIN) {
			return true;
		}

		// Otherwise we're still waiting
		return false;
	}

	protected void end() {
	}

	protected void interrupted() {
		this.end();
	}
}
