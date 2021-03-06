package org.ingrahamrobotics.robot;

import edu.wpi.first.wpilibj.SPI;

public class RobotMap {

	// Joysticks
	public static final int joyDriveLeft = 0;
	public static final int joyDriveRight = 1;
	public static final int joyArm = 2;
	public static final int joyTest = 3;

	// Joystick Buttons
	public static final int joyArmFire = 1;

	// USB
	public static final String usbCameraDriver = "cam0";
	public static final String usbCameraTarget = "cam0";

	// PWM
	public static final int pwmDriveLeft = 0;
	public static final int pwmDriveRight = 1;
	public static final int pwmArm = 2;
	public static final int pwmShooter = 3;
	public static final int pwmKicker = 4;
	public static final int pwmCollector = 5;
	public static final int pwmCameraArm = 6;

	// DIO
	public static final int dioArmSwitch = 0;
	public static final int dioArmA = 1;
	public static final int dioArmB = 2;
	public static final int dioShooter = 3;
	public static final int dioDriveLeftA = 4;
	public static final int dioDriveLeftB = 5;
	public static final int dioDriveRightA = 6;
	public static final int dioDriveRightB = 7;

	// SPI
	public static final SPI.Port spiGyro = SPI.Port.kOnboardCS0;
}
