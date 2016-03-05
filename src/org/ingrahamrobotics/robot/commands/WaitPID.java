package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class WaitPID extends Command {

	private PIDSubsystem pid;
	private double tolerance;
	private int minCount;
	private Mode mode;
	private int count;

	public enum Mode {
		kREL_MIN(), kREL_MAX(), kREL_RANGE(), kABS_MIN(), kABS_MAX, kABS_RANGE();
	}

	public WaitPID(PIDSubsystem pid, double tolerance, Mode mode) {
		this(pid, tolerance, mode, 1);
	}

	public WaitPID(PIDSubsystem pid, double tolerance, Mode mode, int minCount) {
		this.pid = pid;
		this.tolerance = tolerance;
		this.minCount = minCount;
		this.mode = mode;
	}

	protected void initialize() {
		count = 0;
	}

	protected void execute() {
		double actual = pid.getPosition();
		double setpoint = pid.getSetpoint();

		// Ignore 0 setpoints (for some reason)
		if (setpoint == 0) {
			// Does this actually happen?
			Output.output(OutputLevel.PID, getName() + "-wasZero", true);
			return;
		}

		switch (mode) {
		case kREL_MAX:
		case kABS_MAX:
			if (actual <= max(setpoint)) {
				count++;
			}
			break;
		case kREL_MIN:
		case kABS_MIN:
			if (actual >= min(setpoint)) {
				count++;
			}
			break;
		case kREL_RANGE:
		case kABS_RANGE:
			if (actual <= max(setpoint) && actual >= min(setpoint)) {
				count++;
			}
			break;
		}
	}

	private double range(double setpoint, boolean max) {
		double retval = 0;

		switch (mode) {
		case kABS_MIN:
		case kABS_MAX:
		case kABS_RANGE:
			if (max) {
				retval = setpoint + tolerance;
			} else {
				retval = setpoint - tolerance;
			}
			break;
		case kREL_MIN:
		case kREL_MAX:
		case kREL_RANGE:
			if (max) {
				retval = setpoint * (1.0 + tolerance);
			} else {
				retval = setpoint * (1.0 - tolerance);
			}
			break;
		}

		return retval;
	}

	private double min(double setpoint) {
		return range(setpoint, false);
	}
	
	private double max(double setpoint) {
		return range(setpoint, true);
	}

	private boolean isReady() {
		boolean ready = false;
		if (count >= minCount) {
			ready = true;
		}

		Output.output(OutputLevel.PID, getName() + "-ready", isReady());
		return ready;
	}

	protected boolean isFinished() {
		return isReady();
	}

	protected void end() {
		count = 0;
		isReady();
	}

	protected void interrupted() {
		end();
	}
}
