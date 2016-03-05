package org.ingrahamrobotics.robot;

import org.ingrahamrobotics.robot.RobotMap;
import org.ingrahamrobotics.robot.commands.*;
import org.ingrahamrobotics.robot.output.Settings;

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
	
	// Arm/Shooter buttons
	//Hank wants there to be 2 shoot buttons: one for shoot from the outerworks line 
	//and one for shooting from next to the castle
	public Button collect = new JoystickButton(joyArm, 1);
	public Button shoot = new JoystickButton(joyArm, 2);
	public Button armHome = new JoystickButton(joyArm, 5);
	public Button armUp = new JoystickButton(joyArm, 6);
	public Button armShoot = new JoystickButton(joyArm, 7);
	public Button armDown = new JoystickButton(joyArm, 10);
	
	// Drive buttons
	public Button driverArmDownL = new JoystickButton(joyLeft, 1);
	public Button driverArmDownR = new JoystickButton(joyRight, 1);
	//The rest will be those ideally would be preset actions for the defenses
	
	// Test buttons
	public Button testArmDown = new JoystickButton(joyTest, 1); // A
	public Button testArmHome = new JoystickButton(joyTest, 2); // B
	public Button testArmShoot = new JoystickButton(joyTest, 3); // X
	public Button testArmUp = new JoystickButton(joyTest, 4); // Y	
	public Button testShooter = new JoystickButton(joyTest, 5); // LB
	public Button testFire = new JoystickButton(joyTest, 6); // RB
	public Button testDrive = new JoystickButton(joyTest, 7); // Back
	public Button testCollect = new JoystickButton(joyTest, 8); // Start
	public Button testArmInit = new JoystickButton(joyTest, 9); // L-Stick
	public Button bTestArmInit = new JoystickButton(joyArm, 4);
	public Button testEncoderDrive = new JoystickButton(joyTest, 10); // R-Stick

	/*
	 * These were having some trouble; they triggered on ABXY
	 * We don't need them so just kill them for now
	 * 
	public Trigger foo = new Triggers(joyTest, Hand.kLeft);
	public Trigger foo = new Triggers(joyTest, Hand.kRight);
	
	public Trigger foo = new Dpad(joyTest, 0);
	public Trigger foo = new Dpad(joyTest, 2);
	public Trigger foo = new Dpad(joyTest, 4);
	public Trigger foo = new Dpad(joyTest, 6);
	*/
	
	public OI() {
		
		// Arm/Shooter buttons
		shoot.whenReleased(new ShooterShoot());
		collect.toggleWhenPressed(new ShooterCollect());
		armUp.whenReleased(new ArmPreset_Up());
		armShoot.whenReleased(new ArmPreset_Shoot());
		armHome.whenReleased(new ArmPreset_Home());
		armDown.whenReleased(new ArmPreset_Down());
		
		// Drive buttons

		// Test buttons
		testDrive.whenReleased(new DriveTest());

		testArmUp.whenReleased(new ArmPreset_Up());
		testArmDown.whenReleased(new ArmPreset_Down());
		testArmHome.whenReleased(new ArmPreset_Home());
		testArmShoot.whenReleased(new ArmPreset_Shoot());
		
		testEncoderDrive.toggleWhenPressed(new DriveEncoderTest());
		testArmInit.whenReleased(new ArmInit());
		bTestArmInit.whenReleased(new ArmInit());

		testFire.whenPressed(new ShooterShoot());
		testShooter.whenReleased(new ShooterPreset(Settings.Key.SHOOTER_SPEED));
		testCollect.toggleWhenPressed(new ShooterCollect());
	}
}
