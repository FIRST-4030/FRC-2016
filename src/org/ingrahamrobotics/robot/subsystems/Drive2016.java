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
		tank = null;

		// Define our DriveHalf sub-subsystems
		drives[Side.kLEFT.ordinal()] = new DriveHalf(Side.kLEFT.name, RobotMap.pwmDriveLeft, true,
				Sensors.Sensor.DRIVE_ENCODER_LEFT);
		drives[Side.kRIGHT.ordinal()] = new DriveHalf(Side.kRIGHT.name, RobotMap.pwmDriveRight, false,
				Sensors.Sensor.DRIVE_ENCODER_RIGHT);
	}

	public void tankDrive(double left, double right) {

		// Do not allow tank drive when we are in PID mode
		if (!manualCtrlEnabled()) {
			if (tank != null) {
				System.err.println(getName() + ": Stopping tank drive");
				tank.free();
				tank = null;
			}
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
		if (tank == null) {
			// Ensure tank exists when we need it
			System.err.println(getName() + ": Starting tank drive");
			tank = new RobotDrive(drives[Side.kLEFT.ordinal()].getMotor(), drives[Side.kRIGHT.ordinal()].getMotor());
		}
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
