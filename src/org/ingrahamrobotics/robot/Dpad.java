package org.ingrahamrobotics.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class Dpad extends Trigger {

	private Joystick joy;
	private int index;

	public Dpad(Joystick joy, int index) {
		this.joy = joy;
		this.index = index;
	}

	public boolean get() {
		return (joy.getPOV(1) == index);
	}
}
