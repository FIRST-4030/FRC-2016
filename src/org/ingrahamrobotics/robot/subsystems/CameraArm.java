package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CameraArm extends Subsystem {

	private Talon servo = new Talon(RobotMap.pwmCameraArm);
	private boolean up = false;

	public void toggle() {
		if (up) {
			down();
		} else {
			up();
		}
	}

	public boolean isUp() {
		return up;
	}

	public void up() {
		up = true;
		set(Settings.Key.CAMERA_ARM_UP.getDouble());
	}

	public void down() {
		up = false;
		set(Settings.Key.CAMERA_ARM_DOWN.getDouble());
	}

	private void set(double setpoint) {
		servo.set(setpoint);
	}

	public void stop() {
		servo.disable();
	}

	public void initDefaultCommand() {
		// No default command
	}
}
