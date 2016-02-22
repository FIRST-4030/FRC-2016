
package org.ingrahamrobotics.robot;

import org.ingrahamrobotics.robot.subsystems.*;
import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final Arm arm = new Arm();
	public static final DriveBase drive = new DriveBase();
	public static final Kicker kicker = new Kicker();
	public static final ShooterWheels shooter = new ShooterWheels();
	public static final DriverCamera camDriver = new DriverCamera();
	public static final TargetCamera camTarget = new TargetCamera();
	public static final Sensors sensors = new Sensors();
	public static final Collector collector = new Collector();
	
	public static OI oi;

    Command autonomousCommand;
    SendableChooser chooser;

    public void robotInit() {
		oi = new OI();
		
		Output.initInstance();
        new Settings(Output.getRobotTables()).subscribeAndPublishDefaults();
    }
	
    public void disabledInit(){
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
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
