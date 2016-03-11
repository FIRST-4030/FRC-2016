package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.dashboard.Settings;
import org.ingrahamrobotics.robot.pid.Wait;

public class KickerWait extends Wait {

	public KickerWait() {
		super(Settings.Key.KICKER_SHOOT.getInt());
	}
}
