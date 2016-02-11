package org.usfirst.frc.team4030.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class KickBall extends Command {
	
	long startTime;
	long goalMillis;

	public KickBall(long goalSeconds) {
		goalMillis = goalSeconds * 1000;
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
	}

	@Override
	protected void execute() {
		//set kicker motor to 100
	}

	@Override
	protected boolean isFinished() {
		return System.currentTimeMillis() - startTime >= goalMillis;
	}

	@Override
	protected void end() {
		//set kicker motor to 0
	}

	@Override
	protected void interrupted() {
		this.end();
	}

}
