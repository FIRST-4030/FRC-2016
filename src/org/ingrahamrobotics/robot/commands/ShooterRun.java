package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.Command;

public class ShooterRun extends Command {
		
    public ShooterRun() {
        requires(Robot.shooter);
    }

    protected void initialize() {
    	Robot.shooter.start();
    }

    protected void execute() {
    	Robot.shooter.updatePID();
    	int setpoint = Settings.Key.SHOOTER_SPEED.getInt();
    	Robot.shooter.set(setpoint);
    }
    
    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.shooter.stop();
    }

    protected void interrupted() {
    	this.end();
    }
}
