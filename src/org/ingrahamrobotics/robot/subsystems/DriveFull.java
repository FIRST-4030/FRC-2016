package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.commands.DriveTank;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class DriveFull extends PIDSubsystem {

	private DriveHalf[] drives;
	private RobotDrive tank;
	private boolean tankEnable;

	public enum Side {
		kLEFT(0, "Left"), kRIGHT(1, "Right");
		public final int i;
		public final String name;

		private Side(int i, String name) {
			this.i = i;
			this.name = name;
		}
	}

	public class HalfTarget {
		public Side side;
		public double setpoint;
	}

	public DriveFull() {
		super(1.0, 0.0, 0.0);
		tankEnable = true;
		drives = new DriveHalf[Side.values().length];

		// Left-right tank drive
		drives[Side.kLEFT.i] = new DriveHalf(Side.kLEFT.name, RobotMap.pwmDriveLeft, true,
				Sensors.Sensor.DRIVE_ENCODER_LEFT);
		drives[Side.kRIGHT.i] = new DriveHalf(Side.kRIGHT.name, RobotMap.pwmDriveRight, true,
				Sensors.Sensor.DRIVE_ENCODER_RIGHT);
		tank = new RobotDrive(drives[Side.kLEFT.i].getMotor(), drives[Side.kRIGHT.i].getMotor());
	}

	public DriveHalf[] getDrives() {
		return drives;
	}

	public void tankDrive(double left, double right) {

		// Do not allow tank drive when we are in PID mode
		if (!tankEnable) {
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

	public void start() {
		getPIDController().enable();
		for (DriveHalf drive : drives) {
			drive.start();
		}

		tankEnable = false;
	}

	public void stop() {
		getPIDController().disable();
		for (DriveHalf drive : drives) {
			drive.stop();
		}

		tankEnable = true;
		new DriveTank();
	}

	public void updatePID() {
		double p = Settings.Key.DRIVE_PID_P.getDouble();
		double i = Settings.Key.DRIVE_PID_I.getDouble();
		double d = Settings.Key.DRIVE_PID_D.getDouble();
		this.getPIDController().setPID(p, i, d);

		for (DriveHalf drive : drives) {
			drive.getPIDController().setPID(p, i, d);
		}
	}

	public void set(HalfTarget targets[]) {
		if (targets.length == 0) {
			stop();
		} else {
			start();
			this.setSetpoint(targets[0].setpoint);
			for (HalfTarget target : targets) {
				drives[target.side.i].set(target.setpoint);
			}
		}
	}

	public double returnPIDInput(Side side) {
		return drives[side.i].returnPIDInput();
	}

	// Return something reasonable in case anyone asks
	protected double returnPIDInput() {
		return returnPIDInput(Side.kLEFT);
	}

	protected void usePIDOutput(double output) {
		// Handled by the DriveHalf subsystems
	}

	public void initDefaultCommand() {
		this.setDefaultCommand(new DriveTank());
	}
}
