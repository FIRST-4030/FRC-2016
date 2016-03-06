package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.output.Settings;

public class ArmPreset_Up extends ArmPreset {
	public ArmPreset_Up() {
		super(Settings.Key.ARM_PRESET_DOWN);
	}
}
