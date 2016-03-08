package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.Command;

public class CameraAnalyze extends Command {

	public static final int kMIN_DELAY = 20;

	public static class CameraRunner implements Runnable {

		private static Thread thread;
		private static boolean done;

		public CameraRunner() {
			done = true;
			thread = null;
		}

		public static boolean isFinished() {
			Output.output(OutputLevel.VISION, Robot.camTarget.getCurrentCommand() + "-running", !done);
			return done;
		}

		public static void stop() {
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

		public static void start() {
			done = false;
			if (thread != null) {
				thread = new Thread(new CameraRunner());
			}
			thread.setDaemon(true);
			if (!thread.isAlive()) {
				thread.start();
			}
			isFinished();
		}

		public void run() {
			while (!done) {

				// Allow cancellation
				if (Thread.interrupted()) {
					return;
				}

				// Schedule the next run
				long nextRun = System.currentTimeMillis() + Settings.Key.VISION_INTERVAL.getInt() + kMIN_DELAY;

				// Analyze
				Robot.camTarget.analyze();

				// Wait for the next run
				int nextDelay = (int) (nextRun - System.currentTimeMillis());
				if (nextDelay > 0) {
					try {
						Thread.sleep(nextDelay);
					} catch (InterruptedException e) {
						return;
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
