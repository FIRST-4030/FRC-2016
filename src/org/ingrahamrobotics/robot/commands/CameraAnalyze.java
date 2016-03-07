package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.Command;

public class CameraAnalyze extends Command {

	public static class CameraRunner implements Runnable {

		public static int kMIN_DELAY = 20;

		private static boolean done = true;

		public static boolean isFinished() {
			Output.output(OutputLevel.VISION, Robot.camTarget.getCurrentCommand() + "-running", !done);
			return done;
		}

		public static void stop() {
			done = true;
			isFinished();
		}

		public static void start() {
			done = false;
			(new Thread(new CameraRunner())).start();
			isFinished();
		}

		public void run() {
			while (!done) {

				// Schedule the next run
				long nextRun = System.currentTimeMillis() + Settings.Key.VISION_INTERVAL.getInt() + kMIN_DELAY;

				// Analyize
				Robot.camTarget.analyze();

				// Wait for the next run
				int nextDelay = (int) (nextRun - System.currentTimeMillis());
				if (nextDelay > 0) {
					try {
						Thread.sleep(nextDelay);
					} catch (InterruptedException e) {
					}
				}
			}
		}

		public static void main(String args[]) {
			start();
		}
	}

	public CameraAnalyze() {
		requires(Robot.camTarget);
	}

	protected void initialize() {
		CameraRunner.start();
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return CameraRunner.isFinished();
	}

	protected void end() {
		CameraRunner.stop();
	}

	protected void interrupted() {
		end();
	}
}
