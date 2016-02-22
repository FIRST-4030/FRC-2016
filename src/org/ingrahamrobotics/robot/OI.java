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
	
	// Test buttons
	public Button testFire = new JoystickButton(joyTest, 6); // RB
	public Button testArm = new JoystickButton(joyTest, 4); // Y
	public Button testArmZero = new JoystickButton(joyTest, 1); // A
	public Button testShooter = new JoystickButton(joyTest, 5); // LB
	public Button testCollect = new JoystickButton(joyTest, 3); // X
	
	public OI() {

		// Test button commands
		testFire.whenPressed(new Shoot());
		testArm.toggleWhenPressed(new ArmManual());
		testArmZero.whenReleased(new ArmZero());
		testShooter.toggleWhenPressed(new ShooterManual());
		testCollect.toggleWhenPressed(new Collect());
	}
}

