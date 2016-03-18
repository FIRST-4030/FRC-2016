package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.vision.Data;
import org.ingrahamrobotics.robot.vision.Log;

import edu.wpi.first.wpilibj.command.Command;

public class CameraTurn extends Command {

	private static int kMAX_AGE = 1500;
	private static int kMIN_WAIT = 100;

	private Data data;
	private boolean done;
	private DriveToTarget drive;
	private DriveWait wait;
	private Log log;
	private long delay;

	public CameraTurn() {
		log = new Log();
		
		// We use the drive system, but indirectly
		// requires(Robot.drive);
	}

	protected void initialize() {
		done = false;
		delay = 0;
		wait = null;
		Output.output(OutputLevel.VISION, getName() + "-start", System.currentTimeMillis());

		// Get and validate our most recent capture
		data = Robot.camTarget.get();
		if (data == null || !data.valid || data.end + kMAX_AGE <= System.currentTimeMillis()) {
			System.err.println("No valid analysis available");
			done = true;
			return;
		}

		// Start the turn and schedule something to wait on it
		Output.output(OutputLevel.VISION, getName() + "-azimuth", data.azimuth);
		drive = new DriveToTarget((int) data.azimuth);
		drive.start();
		delay = System.currentTimeMillis() + kMIN_WAIT;
	}

	protected void execute() {
		// Time-wait if there's a timer
		Output.output(OutputLevel.DRIVE_PID, "nextTS", delay);
		Output.output(OutputLevel.DRIVE_PID, "now", System.currentTimeMillis());
		Output.output(OutputLevel.DRIVE_PID, "timerDelay", delay - System.currentTimeMillis());
		if (System.currentTimeMillis() < delay) {
			Output.output(OutputLevel.DRIVE_PID, "timerWait", System.currentTimeMillis());
			return;
		}
		
		// Start the command and reset the timer
		if (wait == null) {
			Output.output(OutputLevel.DRIVE_PID, "driveWaitStart", System.currentTimeMillis());
			wait = new DriveWait();
			wait.start();
			delay = System.currentTimeMillis() + kMIN_WAIT;
			return;
		}
		
		// We're done when the turn is done
		Output.output(OutputLevel.DRIVE_PID, "driveWaitWait", System.currentTimeMillis());
		if (!wait.isRunning()) {
			Output.output(OutputLevel.DRIVE_PID, "driveWaitDone", System.currentTimeMillis());
			done = true;
		}
	}

	protected boolean isFinished() {
		Output.output(OutputLevel.VISION, getName() + "-done", done);
		return done;
	}

	protected void end() {
		Command cmd = new DriveStop();
		cmd.start();
		log.save("Complete: " + data.azimuth, System.currentTimeMillis() + "-turn.txt");
		Output.output(OutputLevel.VISION, getName() + "-end", System.currentTimeMillis());
	}

	protected void interrupted() {
		end();
	}
}
