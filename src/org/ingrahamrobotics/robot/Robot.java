
package org.ingrahamrobotics.robot;

import org.ingrahamrobotics.robot.subsystems.*;
import org.ingrahamrobotics.robot.commands.*;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class Robot extends IterativeRobot {

	public static final Arm arm = new Arm();
	public static final ArmPresetCtrl armPreset = new ArmPresetCtrl();
	public static final Drive2016 drive = new Drive2016();
	public static final Kicker kicker = new Kicker();
	public static final ShooterWheels shooter = new ShooterWheels();
	public static final ShooterPresetCtrl shooterPreset = new ShooterPresetCtrl();
	public static final CameraDriver camDriver = new CameraDriver();
	public static final CameraTarget camTarget = new CameraTarget();
	public static final Sensors sensors = new Sensors();
	public static final Collector collector = new Collector();

	// Global commands
	public static ArmRun armRun;
	public static ShooterRun shooterRun;

	// Autonomous support
	private Command autoCmd;

	// User input
	public static OI oi;
	
	// Global state
	public static final boolean disableShooterPID = true;
	public static final Class<? extends Command> driveCmd = DriveTank.class;

	public void robotInit() {

		// Driver control
		oi = new OI();

		// Dashboard support
		Output.initInstance();
		new Settings(Output.getRobotTables()).subscribeAndPublishDefaults();

		// Global commands
		armRun = new ArmRun();
		shooterRun = new ShooterRun();

		// Immediate init (no motion)
		new ShooterStop();

		// Autonomous init (first motion)
		autoCmd = new ArmInit();

		// The camera is not controllable and therefore does not have a related
		// command
		try {
			camDriver.start();
		} catch (Exception e) {
			System.err.println("Unable to start camera");
		}
	}

	public void disabledInit() {
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		if (autoCmd != null) {
			autoCmd.start();
		}
	}

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		try {
			Command drive = driveCmd.newInstance();
			drive.start();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	public void testPeriodic() {
		LiveWindow.run();
	}
}
