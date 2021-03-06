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
	public static final Power power = new Power();

	// Global commands
	public static ArmRun armRun;
	public static ShooterRun shooterRun;
	public static CameraAnalyze camAnalyze;

	// User input
	public static OI oi;

	// Master switch
	private static final boolean production = true;

	// Global state
	public static boolean shooterLock = false;
	public static Class<? extends Command> driveCmd = null;
	public static final boolean disableShooterPID = true; // Encoder busted
	public static final boolean disableReadPower = true;
	public static final boolean disableProdControls = !production;
	public static final boolean disableTestControls = production;
	public static final boolean disableCamTarget = true;

	@SuppressWarnings("unused")
	public void robotInit() {

		// Driver control
		oi = new OI();

		// Dashboard support
		Output.initInstance();
		new Settings(Output.getRobotTables()).subscribeAndPublishDefaults();

		// Global commands
		armRun = new ArmRun();
		shooterRun = new ShooterRun();
		camAnalyze = null;

		// The driver camera is not controllable and does not have a command
		// Start it at init unless it would interfere with the target camera
		if (disableCamTarget || (RobotMap.usbCameraDriver != RobotMap.usbCameraTarget)) {
			camDriver.start();
		}

		// Get the target camera warmed up
		if (!disableCamTarget) {
			camAnalyze = new CameraAnalyze();
			camTarget.warmup();
		}
	}

	// Code to run at init of each DS-controlled mode
	public void modeInit() {

		// Calibrate sensors, if needed
		if (!sensors.calibrated()) {
			sensors.calibrate();
		}
		sensors.reset();

		// Init the arm, if needed
		if (!arm.ready()) {
			Command cmd = new ArmZero();
			cmd.start();
		}

		// Init the shooter
		Command cmd = new ShooterStop();
		cmd.start();

		// Start the target camera, if available
		if (camAnalyze != null) {
			camAnalyze.start();
		}
	}

	public void disabledInit() {
		modeInit();
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		modeInit();

		// Start the auto program if enabled
		if (Settings.Key.AUTO_ENABLE.getBoolean()) {
			Command autoCmd;
			if (Settings.Key.AUTO_LOW.getBoolean()) {
				autoCmd = new AutoLow();
			} else {
				autoCmd = new Auto();
			}
			autoCmd.start();
		}
	}

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		modeInit();

		// Start manual drive control
		try {
			if (driveCmd != null) {
				Command drive = driveCmd.newInstance();
				drive.start();
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		System.err.println("\n\n\nTeleop Ready\n\n\n");
	}

	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	public void testPeriodic() {
		LiveWindow.run();
	}
}
