package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;
import org.ingrahamrobotics.robot.vision.Data;
import org.ingrahamrobotics.robot.vision.Log;

import edu.wpi.first.wpilibj.command.Command;

public class CameraTurn extends Command {

	private static int kMAX_AGE = 1500;

	private Data data;
	private boolean done;
	private DriveToGyro drive;
	private DriveWait wait;
	private Log log;

	public CameraTurn() {
		log = new Log();
		
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
		double azimuth = data.azimuth + Settings.Key.VISION_AZIMUTH_OFFSET.getDouble();
		Output.output(OutputLevel.VISION, getName() + "-azimuth", azimuth);
		drive = new DriveToGyro(azimuth);
		drive.start();
		wait = new DriveWait();
		wait.start();
	}

	protected void execute() {
		Output.output(OutputLevel.VISION, getName() + "-wait", System.currentTimeMillis());
		if (wait.isFinished()) {
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
		log.save("Complete", System.currentTimeMillis() + "-turn.txt");
		Output.output(OutputLevel.VISION, getName() + "-end", System.currentTimeMillis());
	}

	protected void interrupted() {
		end();
	}
}
