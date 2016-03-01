package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
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
		Robot.drive.stop();
		Sensors.Sensor.DRIVE_ENCODER_LEFT.reset();
		Sensors.Sensor.DRIVE_ENCODER_RIGHT.reset();
	}

	protected void execute() {
		double speedLeft = calcSpeed(left, Sensors.Sensor.DRIVE_ENCODER_LEFT.getInt());
		double speedRight = calcSpeed(right, Sensors.Sensor.DRIVE_ENCODER_RIGHT.getInt());
		Robot.drive.set(speedLeft, speedRight);

		// Set the done flag when we stop moving
		if (speedLeft == 0 && speedRight == 0) {
			done = true;
		}
	}

	private double calcSpeed(int target, int current) {
		double retval = 0;
		int tolerance = Settings.Key.DRIVE_TOLERANCE.getInt();

		int err = target - current;
		int errAbs = Math.abs(err);

		// If we are off-target
		if (errAbs > tolerance) {

			// Run
			retval = speed;

			// Run backwards if we're beyond the target
			if (err < 0) {
				retval *= -1;
			}

			// Run slower near the target
			double scaledTolerance = tolerance * Settings.Key.DRIVE_TSCALE.getDouble();
			if (errAbs < scaledTolerance) {
				retval *= Settings.Key.DRIVE_SSCALE.getDouble();
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
		this.end();
	}
}
