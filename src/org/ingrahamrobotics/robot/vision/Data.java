package org.ingrahamrobotics.robot.vision;

public class Data {
	public enum Confidence {
		kNONE(), kMINIMAL(), kNOMINAL();
	}

	public boolean valid = false;
	public Confidence confidence = Confidence.kNONE;
	public int exposure = 0;
	public int brightness = 0;
	public int whitebalance = 0;
	public int h_min = 0;
	public int h_max = 0;
	public int s_min = 0;
	public int s_max = 0;
	public int l_min = 0;
	public int l_max = 0;
	public double distance = 0;
	public double plane = 0;
	public double azimuth = 0;
	public double altitude = 0;
	public double area = 0;
	public double x = 0;
	public double y = 0;
	public double height = 0;
	public double width = 0;
	public double rotation = 0;
	public long start = 0;
	
	@Override
	public String toString() {
		String str = new String();
		str += "V:" + valid + ";C:" + confidence + "\n";
		str += "B" + brightness + ";E" + exposure + ";W" + whitebalance + "\n";
		str += "HL:" + h_min + ";HH:" + h_max + ";SL:" + s_min + ";SH:" + s_max + ";LL:" + l_min + ";LH:" + l_max + "\n";
		str += "D:" + String.format("%.1f", distance) + ";Z:" + String.format("%.1f", azimuth) + ";L:"
				+ String.format("%.1f", altitude) + ";P:" + String.format("%.1f", plane) + "\n";
		str += "A:" + String.format("%.1f", area) + ";X:" + String.format("%.1f", x) + ";Y:" + String.format("%.1f", y) + ";H:"
				+ String.format("%.1f", height) + ";W:" + String.format("%.1f", width) + ";R:"
				+ String.format("%.1f", rotation) + "\n";
		return str;
	}
}
