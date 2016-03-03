package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.output.Settings;

public class KickerWait extends Wait {

    public KickerWait() {
    	super(Settings.Key.KICKER_SHOOT.getInt());
    }
}
