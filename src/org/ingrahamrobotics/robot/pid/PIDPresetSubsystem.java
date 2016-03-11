package org.ingrahamrobotics.robot.pid;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public abstract class PIDPresetSubsystem extends PIDSubsystem {

    public PIDPresetSubsystem(double p, double i, double d) {
    	super(p, i, d);
    }
    
    abstract public void set(double setpoint);
}
