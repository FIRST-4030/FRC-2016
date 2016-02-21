package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.Command;

public class LowBarArm extends Command {

	public LowBarArm() {
		requires(Robot.arm);
	}

	@Override
	protected void initialize() {
		Robot.arm.start();
	}

	@Override
	protected void execute() {
		int position = Settings.Key.ARM_COLLECT_SETPOINT.getInt();
		Robot.arm.set(position);
	}

	@Override
	protected boolean isFinished() {
		return Robot.arm.onTarget();
	}

	@Override
	protected void end() {
		Robot.arm.stop();
	}

	@Override
	protected void interrupted() {
		this.end();
	}

}
