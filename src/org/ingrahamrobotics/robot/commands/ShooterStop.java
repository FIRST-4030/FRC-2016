package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.subsystems.ShooterWheels;

public class ShooterStop extends ShooterPreset {
    public ShooterStop() {
    	super(ShooterWheels.kSTOP);
    }
}
