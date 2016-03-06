package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;
import org.ingrahamrobotics.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj.command.Command;

public class DriveToEncoder extends Command {

	private int left;
	private int right;
	private double speed;
	private boolean done;

	public DriveToEncoder(int left, int right, double speed) {
		requires(Robot.drive);
		done = false;

		this.left = left;
		this.right = right;
		this.speed = Math.abs(speed);
	}

	protected void initialize() {
		done = false;
		Robot.drive.stop();
		Sensors.Sensor.DRIVE_ENCODER_LEFT.reset();
		Sensors.Sensor.DRIVE_ENCODER_RIGHT.reset();
	}

	protected void execute() {

		// Bypass the broken left encoder
		// double speedLeft = calcSpeed(left,
		// Sensors.Sensor.DRIVE_ENCODER_LEFT.getInt(), "left");
		double speedLeft = calcSpeed(left,
				Sensors.Sensor.DRIVE_ENCODER_RIGHT.getInt(), "left");
		double speedRight = calcSpeed(right,
				Sensors.Sensor.DRIVE_ENCODER_RIGHT.getInt(), "right");
		Output.output(OutputLevel.PID, getName() + "-left", speedLeft);
		Output.output(OutputLevel.PID, getName() + "-right", speedRight);
		Robot.drive.set(speedLeft, speedRight);

		// Set the done flag when we stop moving
		if (speedLeft == 0 && speedRight == 0) {
			done = true;
		}
		Output.output(OutputLevel.PID, getName() + "-running", !done);
	}

	private double calcSpeed(int target, int current, String name) {
		double retval = 0;
		int tolerance = Settings.Key.DRIVE_TOLERANCE.getInt();

		int err = target - current;
		int errAbs = Math.abs(err);
		Output.output(OutputLevel.PID, getName() + name + "-err", err);

		// If we are off-target
		if (errAbs > tolerance) {

			// Run
			retval = speed;

			// Run backwards if we're beyond the target
			if (err < 0) {
				retval *= -1;
			}

			// Run slower near the target
			boolean scaled = false;
			double scaledTolerance = tolerance
					* Settings.Key.DRIVE_TSCALE.getDouble();
			if (errAbs < scaledTolerance) {
				scaled = true;
			}
			if (scaled) {
				retval *= Settings.Key.DRIVE_SSCALE.getDouble();
				Output.output(OutputLevel.PID, getName() + name + "-scaled",
						scaled);
			}
		}

		return retval;
	}

	protected boolean isFinished() {
		return done;
	}

	protected void end() {
		Robot.drive.stop();
	}

	protected void interrupted() {
		end();
	}
}
