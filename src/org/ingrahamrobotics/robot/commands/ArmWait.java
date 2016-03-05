package org.ingrahamrobotics.robot.commands;

import org.ingrahamrobotics.robot.Robot;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj.command.Command;

public class ArmWait extends Command {
	
	// Not configurable because these are programming features not runtime features
	public static final int kTOLERANCE = 25;
	public static final int kREADY_MIN = 2;
	
	private int ready = 0;
	
    public ArmWait() {
    }

    protected void initialize() {
    	ready = 0;
    }

    protected void execute() {
    	Output.output(OutputLevel.PID, getName() + "-ready", ready);
    }

    // Done when we're on-target according to the PID subsystem
    protected boolean isFinished() {
		double actual = Sensors.Sensor.ARM_ENCODER.getDouble();
		double setpoint = Robot.arm.getSetpoint();

		// Waiting for 0 makes no sense
		// Presumably someone is about to set a valid setpoint so just wait
		if (setpoint < 1) {
			return false;
		}

		// Mark as ready if we're within kTOLERANCE of the setpoint
		if (actual >= setpoint) {
			ready++;
		}

    	// Done after the minimum number of ready samples
		if (ready > kREADY_MIN) {
			return true;
		}

		// Otherwise we're still waiting
		return false;
    }

    protected void end() {
    	ready = 0;
    	Output.output(OutputLevel.PID, getName() + "-ready", ready);
    }

    protected void interrupted() {
    	end();
    }
}
