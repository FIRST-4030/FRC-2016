package org.ingrahamrobotics.robot.pid;

import org.ingrahamrobotics.robot.dashboard.Settings.Key;

import edu.wpi.first.wpilibj.command.Command;

public class PIDPreset extends Command {

	private PIDPresetSubsystem pid;
	private PIDPresetCtrl ctrl;
	private Command cmd;
	
	private Key key;
	private double target;
	private boolean done;
	
	
	/**
	 * @param sub The PID subsystem to be controlled
	 * @param cmd The command running that PID subsystem
	 * @param ctrl The lock subsystem for PIDPreset contention
	 * @param key The Settings.Key dynamic setpoint for PID regulation
	 */
	public PIDPreset(PIDPresetSubsystem pid, Command cmd, PIDPresetCtrl ctrl, Key key) {
		this.pid = pid;
		this.cmd = cmd;
		this.ctrl = ctrl;
		init(key, 0);
	}

	/**
	 * @param sub The PID subsystem to be controlled
	 * @param cmd The command running that PID subsystem
	 * @param ctrl The lock subsystem for PIDPreset contention
	 * @param target The static setpoint for PID regulation
	 */
	public PIDPreset(PIDPresetSubsystem pid, Command cmd, PIDPresetCtrl ctrl, double target) {
		this.pid = pid;
		this.cmd = cmd;
		this.ctrl = ctrl;
		init(null, target);
	}

	private void init(Key key, double target) {
		requires(ctrl);
		done = false;
		this.key = key;
		this.target = target;
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		if (!cmd.isRunning()) {
			cmd.start();
			System.err.println("PIDPreset called while cmd not running");
			return;
		}

		double setpoint = target;
		if (key != null) {
			setpoint = key.getDouble();
		}
		pid.set(setpoint);
		done = true;
	}

	@Override
	protected boolean isFinished() {
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
