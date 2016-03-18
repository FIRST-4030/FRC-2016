package org.ingrahamrobotics.robot.subsystems;

public class DriveSide {
	public enum Side {
		kLEFT("Left", false), kRIGHT("Right", true);
		public final String name;

		public boolean invertTurn;

		private Side(String name, boolean invertTurn) {
			this.name = name;
			this.invertTurn = invertTurn;
		}
	}
}
