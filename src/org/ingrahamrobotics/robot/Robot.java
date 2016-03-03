
package org.ingrahamrobotics.robot;

import org.ingrahamrobotics.robot.subsystems.*;
import org.ingrahamrobotics.robot.commands.*;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final Arm arm = new Arm();
	public static final ArmPresetCtrl armPreset = new ArmPresetCtrl();
	public static final DriveBase drive = new DriveBase();
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
	private static Command autoCmd;

	public static OI oi;

	public void robotInit() {
		
		// Driver control
		oi = new OI();

		// Dashboard support
		Output.initInstance();
		new Settings(Output.getRobotTables()).subscribeAndPublishDefaults();

		// Start the driver camera at robot init
		// This is not controllable and therefore does not have a related command
		try {
			camDriver.start();
		} catch (Exception e) {
			System.err.println("Unable to start camera");
		}
		
		// Global commands
		armRun = new ArmRun();
		
		// Autonomous support
		autoCmd = new ArmInit();
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
	}

	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	public void testPeriodic() {
		LiveWindow.run();
	}
}
