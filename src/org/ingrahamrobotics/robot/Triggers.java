package org.ingrahamrobotics.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class Triggers extends Trigger {
	
	private Hand hand;
	
	public Triggers(Hand hand) {
		this.hand = hand;
	}
    
    public boolean get() {
    	return Robot.oi.joyTest.getTrigger(hand);
    }
}
