package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.Command;

public class CameraAnalyze extends Command {

	public static final int kMIN_DELAY = 50;

	public static class CameraRunner implements Runnable {

		private static Thread thread;
		private static boolean done;

		public CameraRunner() {
			done = true;
			thread = null;
		}

		public static boolean isFinished() {
			Output.output(OutputLevel.VISION, "running", !done);
			return false;
		}

		protected static void stop() {
			done = true;
			if (thread != null) {
				try {
					thread.interrupt();
					thread.join();
				} catch (InterruptedException e) {
				}
			}
			thread = null;
			isFinished();
		}

		protected static void start() {
			if (thread != null) {
				System.err.println("CameraRunner started while still active");
				stop();
			}

			thread = new Thread(new CameraRunner());
			thread.setDaemon(true);
			thread.start();

			done = false;
			isFinished();
		}

		public void run() {
			while (!done) {

				// Allow cancellation
				if (Thread.interrupted()) {
					return;
				}

				// Schedule the next run
				long nextRun = System.currentTimeMillis() + Settings.Key.VISION_INTERVAL.getInt();

				// Analyze
				Robot.camTarget.analyze();

				// Wait for the next run
				int nextDelay = (int) (nextRun - System.currentTimeMillis());
				if (nextDelay < kMIN_DELAY) {
					nextDelay = kMIN_DELAY;
				}
				if (nextDelay > 0) {
					try {
						Thread.sleep(nextDelay);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
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
