package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CameraArm extends Subsystem {

	private Talon servo = new Talon(RobotMap.pwmCameraArm);

	public void toggle() {
		Output.output(OutputLevel.MOTORS, getName() + "-wasUp", isUp());
		if (isUp()) {
			down();
		} else {
			up();
		}
	}

	public boolean isUp() {
		return Robot.cameraArmUp;
	}

	public void up() {
		Robot.cameraArmUp = true;
		set(Settings.Key.CAMERA_ARM_UP.getDouble());
	}

	public void down() {
		Robot.cameraArmUp = false;
		set(Settings.Key.CAMERA_ARM_DOWN.getDouble());
	}

	private void set(double setpoint) {
		servo.set(setpoint);
		Output.output(OutputLevel.MOTORS, getName() + "-setpoint", setpoint);
		Output.output(OutputLevel.MOTORS, getName() + "-isUp", isUp());
	}

	public void stop() {
		servo.disable();
	}

	public void initDefaultCommand() {
		// No default command
	}
}
