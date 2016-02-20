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
	
	// Command buttons
	public Button fire = new JoystickButton(joyArm, RobotMap.joyArmFire);

	// Test buttons
	private Button armTest = new JoystickButton(joyArm, 2);
	private Button armZeroTest = new JoystickButton(joyArm, 3);
	private Button shooterTest = new JoystickButton(joyArm, 4);
	private Button kickTest = new JoystickButton(joyArm, 5);
	private Button captureTest = new JoystickButton(joyArm, 6);
	
	public OI() {

		// Button-activated commands
		fire.whenPressed(new Shoot());
		
		// Test button commands
		armTest.toggleWhenPressed(new ArmManual());
		armZeroTest.whenReleased(new ArmZero());
		shooterTest.toggleWhenPressed(new ShooterManual());
		kickTest.whenReleased(new Kick());
		captureTest.whenReleased(new Capture());
	}
}

