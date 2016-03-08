
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

	// Autonomous support
	private Command autoCmd;

	// User input
	public static OI oi;

	// Global state
	public static final boolean disableShooterPID = true;
	public static final Class<? extends Command> driveCmd = DriveTank.class;
	public static final boolean disableCamTarget = false;
	public static final boolean kDEBUG_CAMERA = (!disableCamTarget) & true;

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
		if (!disableCamTarget) {
			camAnalyze = new CameraAnalyze();
		}

		// Autonomous command
		autoCmd = null;

		// The driver camera is not controllable and does not have a command
		// Start it at init unless it would interfere with the target camera
		if (disableCamTarget || (RobotMap.usbCameraDriver != RobotMap.usbCameraTarget)) {
			camDriver.start();
		}

		// Analyze at boot so we can test without a driver station
		// Run more than once so we get valid timing data
		if (kDEBUG_CAMERA) {
			camTarget.analyze();
			camTarget.analyze();
			camTarget.analyze();
		}
	}

	// Code to run at init of each DS-controlled mode
	public void modeInit() {

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

		// Init the arm
		Command cmd = new ArmInit();
		cmd.start();

		// Start the auto program, if available
		if (autoCmd != null) {
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
