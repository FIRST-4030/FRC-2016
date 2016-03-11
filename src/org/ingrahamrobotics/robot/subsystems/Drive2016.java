package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.dashboard.Output;
import org.ingrahamrobotics.robot.dashboard.OutputLevel;
import org.ingrahamrobotics.robot.dashboard.Settings;
import org.ingrahamrobotics.robot.pid.DriveFull;
import org.ingrahamrobotics.robot.pid.DriveHalf;
import org.ingrahamrobotics.robot.sensors.Sensors;

import edu.wpi.first.wpilibj.RobotDrive;

public class Drive2016 extends DriveFull {

	private RobotDrive tank;

	public Drive2016() {
		super(Robot.driveCmd);

		// Left-right tank drive
		// Temporarily use the right encoder for both sides
		drives[Side.kLEFT.ordinal()] = new DriveHalf(Side.kLEFT.name, RobotMap.pwmDriveLeft, true,
				Sensors.Sensor.DRIVE_ENCODER_LEFT);
		drives[Side.kRIGHT.ordinal()] = new DriveHalf(Side.kRIGHT.name, RobotMap.pwmDriveRight, false,
				Sensors.Sensor.DRIVE_ENCODER_RIGHT);
		tank = new RobotDrive(drives[Side.kLEFT.ordinal()].getMotor(), drives[Side.kRIGHT.ordinal()].getMotor());
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

		targets[Side.kLEFT.ordinal()] = new HalfTarget();
		targets[Side.kLEFT.ordinal()].side = Side.kLEFT;
		targets[Side.kLEFT.ordinal()].setpoint = left;

		targets[Side.kRIGHT.ordinal()] = new HalfTarget();
		targets[Side.kRIGHT.ordinal()].side = Side.kRIGHT;
		targets[Side.kRIGHT.ordinal()].setpoint = right;

		set(targets);
	}
}
