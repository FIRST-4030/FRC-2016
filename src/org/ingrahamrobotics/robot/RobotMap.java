package org.ingrahamrobotics.robot;

public class RobotMap {
	
	// Joysticks
	public static final int joyDriveLeft = 0;
	public static final int joyDriveRight = 1;
	public static final int joyArm = 2;
	
	// Joystick Buttons
	public static final int joyArmFire = 1;
	
	// USB
	public static final String usbTargetCamera = "cam0";
	public static final String usbDriverCamera = "cam1";
	
	// PWM
	public static final int pwmDriveLeft = 0;
	public static final int pwmDriveRight = 1;
	public static final int pwmArm = 2;
	public static final int pwmShooter = 3;
	public static final int pwmKicker = 4;
	
	// DIO
	public static final int dioArmSwitch = 0;
	public static final int dioArmA = 1;
	public static final int dioArmB = 2;
	public static final int dioShooter = 3;

	// Speculative DIO
	public static final int dioDriveLeftA = 4;
	public static final int dioDriveLeftB = 5;
	public static final int dioDriveRightA = 6;
	public static final int dioDriveRightB = 7;
}
