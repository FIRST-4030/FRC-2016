package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.vision.Data;

import edu.wpi.first.wpilibj.command.Command;

public class CameraTurn extends Command {

	private static int kMAX_AGE = 1500;

	private Data data;
	private boolean done;
	private DriveToTarget drive;
	private DriveWait wait;

	public CameraTurn() {
		// We use the drive system, but indirectly
		// requires(Robot.drive);
	}

	protected void initialize() {
		done = false;
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
		drive = new DriveToTarget((int)data.azimuth, true);
		drive.start();
		wait = new DriveWait();
		wait.start();
	}

	protected void execute() {
		// We're done when the turn is done
		if (!wait.isRunning()) {
			done = true;
		}
	}

	protected boolean isFinished() {
		Output.output(OutputLevel.VISION, getName() + "-done", done);
		return done;
	}

	protected void end() {
		Output.output(OutputLevel.VISION, getName() + "-end", System.currentTimeMillis());
	}

	protected void interrupted() {
		end();
	}
}
