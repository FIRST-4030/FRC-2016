package org.ingrahamrobotics.robot;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.commands.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	// Raw joysticks
	public Joystick joyLeft = new Joystick(RobotMap.joyDriveLeft);
	public Joystick joyRight = new Joystick(RobotMap.joyDriveRight);
	public Joystick joyArm = new Joystick(RobotMap.joyArm);
	public Joystick joyTest = new Joystick(RobotMap.joyTest);
	
	// Command buttons
	/*
	 * Ball stick:
	 * Collect - 2 (Hat Bottom)
	 * Shoot - 1 (Trigger)
	 * Preset @ 0
	 * Shooting @ 4, 3, 5
	 * Zero @ 6
	 * Negative Arm @ 11
	 * 
	 * Drive sticks:
	 * Drive stick has macros for crossing postures
	 * Access to all 8 postures, shortcuts for the 4 installed barriers (configured at runtime)
	 * 
	 * Other control notes:
	 * Arm-height drive speed limit
	 * Return to collect after shoot
	 */
	
	// Test buttons
	public Button testArmDown = new JoystickButton(joyTest, 1); // A
	public Button testArmHome = new JoystickButton(joyTest, 2); // B
	public Button testArmShoot = new JoystickButton(joyTest, 3); // X
	public Button testArmUp = new JoystickButton(joyTest, 4); // Y	
	public Button testShooter = new JoystickButton(joyTest, 5); // LB
	public Button testFire = new JoystickButton(joyTest, 6); // RB
	public Button testDrive = new JoystickButton(joyTest, 7); // Back
	public Button testCollect = new JoystickButton(joyTest, 8); // Start
	public Button testArmZero = new JoystickButton(joyTest, 9); // L-Stick
	public Button testArm = new JoystickButton(joyTest, 10); // R-Stick
	
	public OI() {

		// Test button commands
		testDrive.whenReleased(new DriveTest());

		testArmUp.whenReleased(new ArmPreset_Up());
		testArmDown.whenReleased(new ArmPreset_Down());
		testArmHome.whenReleased(new ArmPreset_Home());
		testArmShoot.whenReleased(new ArmPreset_Shoot());
		
		testArm.toggleWhenPressed(new ArmRun());
		testArmZero.whenReleased(new ArmZero());

		testFire.whenPressed(new ShooterShoot());
		testShooter.toggleWhenPressed(new ShooterRun());
		testCollect.toggleWhenPressed(new ShooterCollect());
	}
}
