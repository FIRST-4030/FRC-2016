package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class DriveFull extends PIDSubsystem {

	protected DriveHalf[] drives;
	private boolean manualEnable;
	private Class<? extends Command> manualCtrl;

	public enum Side {
		kLEFT(0, "Left"), kRIGHT(1, "Right");
		public final int i;
		public final String name;

		private Side(int i, String name) {
			this.i = i;
			this.name = name;
		}
	}

	public class HalfTarget {
		public Side side;
		public double setpoint;
	}

	public DriveFull(Class<? extends Command> manualCtrl) {
		super(1.0, 0.0, 0.0);
		manualEnable = true;
		drives = new DriveHalf[Side.values().length];
		this.manualCtrl = manualCtrl;
	}

	public DriveHalf[] getDrives() {
		return drives;
	}

	public boolean manualCtrlEnabled() {
		return manualEnable;
	}

	private Command enableManualCtrl() {
		manualEnable = true;
		Command cmd = null;
		if (manualCtrl != null) {
			try {
				cmd = manualCtrl.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return cmd;
	}

	private void disableManualCtrl() {
		manualEnable = false;
	}

	public void start() {
		getPIDController().enable();
		for (DriveHalf drive : drives) {
			drive.start();
		}

		disableManualCtrl();
	}

	public void stop() {
		getPIDController().disable();
		for (DriveHalf drive : drives) {
			drive.stop();
		}

		enableManualCtrl();
	}

	public void updatePID() {
		double p = Settings.Key.DRIVE_PID_P.getDouble();
		double i = Settings.Key.DRIVE_PID_I.getDouble();
		double d = Settings.Key.DRIVE_PID_D.getDouble();
		this.getPIDController().setPID(p, i, d);

		for (DriveHalf drive : drives) {
			drive.getPIDController().setPID(p, i, d);
		}
	}

	public void set(HalfTarget targets[]) {
		if (targets.length == 0) {
			stop();
		} else {
			start();
			this.setSetpoint(targets[0].setpoint);
			for (HalfTarget target : targets) {
				drives[target.side.i].set(target.setpoint);
			}
		}
	}

	public double returnPIDInput(Side side) {
		return drives[side.i].returnPIDInput();
	}

	// Return something reasonable in case anyone asks
	protected double returnPIDInput() {
		return returnPIDInput(Side.kLEFT);
	}

	protected void usePIDOutput(double output) {
		// Handled by the DriveHalf subsystems
	}

	public void initDefaultCommand() {
		this.setDefaultCommand(enableManualCtrl());
	}
}
