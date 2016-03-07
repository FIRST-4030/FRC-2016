package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.subsystems.DriveHalf;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class DriveWait extends Command {

	// Not configurable because these are programming features
	public static final int kTOLERANCE = 50;
	public static final int kMIN_SUCCESS = 3;

	private DriveHalf[] drives;
	private DriveWaitHalf[] waits;
	private int count;

	public class DriveWaitHalf extends WaitPID {
		public DriveWaitHalf(PIDSubsystem pid, String name) {
			super(pid, kTOLERANCE, Mode.kABS_RANGE, kMIN_SUCCESS, name);
		}
	}

	// Create a WaitPID command for each drive component
	@Override
	protected void initialize() {
		count = 0;
		drives = Robot.drive.getDrives();
		waits = new DriveWaitHalf[drives.length];

		int i = 0;
		for (DriveHalf drive : drives) {
			waits[i] = new DriveWaitHalf(drive, drive.fullName());
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
		boolean done = (count > kMIN_SUCCESS);
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
