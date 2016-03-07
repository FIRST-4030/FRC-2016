package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.RobotDrive;

public class Drive2016 extends DriveFull {

	private RobotDrive tank;

	public Drive2016() {
		super(Robot.driveCmd);

		// Left-right tank drive
		// Temporarily use the right encoder for both sides
		drives[Side.kLEFT.i] = new DriveHalf(Side.kLEFT.name, RobotMap.pwmDriveLeft, true,
				Sensors.Sensor.DRIVE_ENCODER_RIGHT);
		drives[Side.kRIGHT.i] = new DriveHalf(Side.kRIGHT.name, RobotMap.pwmDriveRight, false,
				Sensors.Sensor.DRIVE_ENCODER_RIGHT);
		tank = new RobotDrive(drives[Side.kLEFT.i].getMotor(), drives[Side.kRIGHT.i].getMotor());
	}

	public void tankDrive(double left, double right) {

		// Do not allow tank drive when we are in PID mode
		if (!manualCtrlEnabled()) {
			return;
		}

		// Scale tank drive speeds to half when the arm is high
		if (Sensors.Sensor.ARM_ENCODER.getInt() > Settings.Key.ARM_SPEED_HEIGHT.getInt()) {
			Output.output(OutputLevel.MOTORS, getName() + "-slow", true);
			left *= Settings.Key.ARM_SPEED_FACTOR.getDouble();
			right *= Settings.Key.ARM_SPEED_FACTOR.getDouble();
		} else {
			Output.output(OutputLevel.MOTORS, getName() + "-slow", false);
		}

		// Drive
		Output.output(OutputLevel.MOTORS, getName() + "-left", left);
		Output.output(OutputLevel.MOTORS, getName() + "-right", right);
		tank.tankDrive(left, right);
	}

	public void set(double left, double right) {
		HalfTarget[] targets = new HalfTarget[2];

		targets[Side.kLEFT.i] = new HalfTarget();
		targets[Side.kLEFT.i].side = Side.kLEFT;
		targets[Side.kLEFT.i].setpoint = left;

		targets[Side.kRIGHT.i] = new HalfTarget();
		targets[Side.kRIGHT.i].side = Side.kRIGHT;
		targets[Side.kRIGHT.i].setpoint = right;

		set(targets);
	}
}
