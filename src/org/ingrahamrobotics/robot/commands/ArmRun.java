package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ArmRun extends Command {

    public ArmRun() {
        requires(Robot.arm);
    }

    protected void initialize() {
    	Robot.arm.start();
    }

    protected void execute() {
    	Robot.arm.updatePID();
    	int setpoint = Settings.Key.ARM_SETPOINT.getInt();
    	Robot.arm.set(setpoint);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.arm.stop();
    }

    protected void interrupted() {
    	this.end();
    }
}
