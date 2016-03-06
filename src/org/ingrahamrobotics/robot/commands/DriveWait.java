package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.subsystems.DriveHalf;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class DriveWait extends Command {

	// Not configurable because these are programming features not runtime
	// features
	public static final int kTOLERANCE = 50;
	public static final int kMIN_SUCCESS = 2;

	private DriveHalf[] drives;
	private DriveWaitHalf[] waits;

	public class DriveWaitHalf extends WaitPID {
		public DriveWaitHalf(PIDSubsystem pid, String name) {
			super(pid, kTOLERANCE, Mode.kABS_RANGE, kMIN_SUCCESS, name);
		}
	}

	// Create a WaitPID command for each drive component
	@Override
	protected void initialize() {
		drives = Robot.drive.getDrives();
		waits = new DriveWaitHalf[drives.length];

		int i = 0;
		for (DriveHalf drive : drives) {
			waits[i++] = new DriveWaitHalf(drive, drive.fullName());
		}
		
		Output.output(OutputLevel.PID, getName() + "-ready", false);
	}

	@Override
	protected void execute() {
	}

	// Wait for all drive components to finish
	@Override
	protected boolean isFinished() {
		boolean done = true;
		for (DriveWaitHalf wait : waits) {
			if (wait.isRunning()) {
				done = false;
				break;
			}
		}
		
		Output.output(OutputLevel.PID, getName() + "-ready", done);
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
