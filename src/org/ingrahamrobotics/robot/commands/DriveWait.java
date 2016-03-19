package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.subsystems.DriveHalf;
import org.ingrahamrobotics.robot.subsystems.Sensors.SensorType;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class DriveWait extends Command {

	// Not configurable because these are programming features
	public static final int kTOLERANCE_ENCODER = 50;
	public static final int kTOLERANCE_GYRO = 5;
	public static final int kMIN_SUCCESS = 3;

	private DriveHalf[] drives;
	private DriveWaitHalf[] waits;
	private int count;
	private boolean done;

	public class DriveWaitHalf extends WaitPID {
		public DriveWaitHalf(PIDSubsystem pid, String name, double tolerance) {
			super(pid, tolerance, Mode.kABS_RANGE, kMIN_SUCCESS, name);
		}
	}

	public DriveWait() {
		done = false;
	}

	// Create a WaitPID command for each drive component
	@Override
	protected void initialize() {
		done = false;
		count = 0;

		drives = Robot.drive.getDrives();
		waits = new DriveWaitHalf[drives.length];
		int i = 0;
		for (DriveHalf drive : drives) {
			double tolerance;
			if (drive.isSensorType(SensorType.ENCODER)) {
				tolerance = kTOLERANCE_ENCODER;
			} else {
				tolerance = kTOLERANCE_GYRO;
			}
			waits[i] = new DriveWaitHalf(drive, drive.fullName(), tolerance);
			waits[i].start();
			i++;
		}

		Output.output(OutputLevel.DRIVE_PID, getName() + "-ready", false);
	}

	@Override
	protected void execute() {
		Robot.drive.updatePID();
		
		boolean done = true;
		for (DriveWaitHalf wait : waits) {
			if (wait.isRunning()) {
				done = false;
			}
		}
		if (done) {
			count++;
		}
	}
	
	// Wait for all drive components to finish
	@Override
	protected boolean isFinished() {
		if (!done && count > kMIN_SUCCESS) {
			done = true;
		}
		Output.output(OutputLevel.DRIVE_PID, getName() + "-ready", done);
		return done;	
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
		end();
	}
}
