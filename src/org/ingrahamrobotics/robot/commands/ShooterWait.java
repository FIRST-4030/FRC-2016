package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;
import org.ingrahamrobotics.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterWait extends Command {

	public static final int kREADY_MIN = 3;
	private int ready = 0;
	private long doneTS;

	public ShooterWait() {
	}

	protected void initialize() {
		ready = 0;
		
		// Allow PID to be disabled
		if (Robot.disableShooterPID) {
			doneTS = System.currentTimeMillis() + Settings.Key.SHOOTER_WAIT.getInt();
		}
	}

	protected void execute() {
    	Output.output(OutputLevel.PID, getName() + "-ready", ready);
	}

	// Done when we're at or above the PID setpoint for kREADY_MIN samples
	protected boolean isFinished() {
		
		// Allow PID to be disabled
		if (Robot.disableShooterPID) {
			if (System.currentTimeMillis() > doneTS) {
				return true;
			}
			return false;
		}
		
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
    	ready = 0;
    	Output.output(OutputLevel.PID, getName() + "-ready", ready);
	}

	protected void interrupted() {
		this.end();
	}
}
