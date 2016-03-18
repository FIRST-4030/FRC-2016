package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.output.Settings;
import org.ingrahamrobotics.robot.subsystems.DriveSide.Side;
import org.ingrahamrobotics.robot.subsystems.Sensors.Sensor;
import org.ingrahamrobotics.robot.subsystems.Sensors.SensorType;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class DriveFull extends PIDSubsystem {

	protected DriveHalf[] drives;
	private boolean manualEnable;
	private Class<? extends Command> manualCtrl;

	public class HalfTarget {
		public Side side;
		public double setpoint;
		public Sensor sensor;
		public Side partner = null;
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
				cmd.start();
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
		updatePID();
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
		double p_encoder = Settings.Key.DRIVE_PID_P.getDouble();
		double i_encoder = Settings.Key.DRIVE_PID_I.getDouble();
		double d_encoder = Settings.Key.DRIVE_PID_D.getDouble();

		double p_gyro = Settings.Key.GYRO_PID_P.getDouble();
		double i_gyro = Settings.Key.GYRO_PID_I.getDouble();
		double d_gyro = Settings.Key.GYRO_PID_D.getDouble();

		// Allow different PID settings for Gyro vs Encoder
		for (DriveHalf drive : drives) {
			if (drive.isSensorType(SensorType.ENCODER)) {
				drive.getPIDController().setPID(p_encoder, i_encoder, d_encoder);
			} else {
				drive.getPIDController().setPID(p_gyro, i_gyro, d_gyro);
			}
		}
	}

	public void set(HalfTarget targets[]) {
		if (targets.length == 0) {
			stop();
		} else {
			start();
			this.setSetpoint(targets[0].setpoint);
			for (HalfTarget target : targets) {
				drives[target.side.ordinal()].set(target.setpoint, target.sensor);
				DriveHalf partner = null;
				if (target.partner != null) {
					partner = drives[target.partner.ordinal()];
				}
				drives[target.side.ordinal()].setPartner(partner);
			}
		}
	}

	public double returnPIDInput(Side side) {
		return drives[side.ordinal()].returnPIDInput();
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
