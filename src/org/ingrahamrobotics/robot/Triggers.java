package org.ingrahamrobotics.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class Triggers extends Trigger {

	private Joystick joy;
	private Hand hand;

	public Triggers(Joystick joy, Hand hand) {
		this.joy = joy;
		this.hand = hand;
	}

	public boolean get() {
		return joy.getTrigger(hand);
	}
}
