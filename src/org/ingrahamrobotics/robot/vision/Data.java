package org.ingrahamrobotics.robot.vision;

public class Data {
	public enum Confidence {
		kNONE(), kMINIMAL(), kNOMINAL();
	}

	public boolean valid = false;
	public Confidence confidence = Confidence.kNONE;
	public double distance = 0;
	public double plane = 0;
	public double azimuth = 0;
	public double altitude = 0;
	public int exposure = 0;
	public int brightness = 0;
	public int whitebalance = 0;
	public int h_min = 0;
	public int h_max = 0;
	public int s_min = 0;
	public int s_max = 0;
	public int l_min = 0;
	public int l_max = 0;
	public double x = 0;
	public double y = 0;
	public double left = 0;
	public double right = 0;
	public double top = 0;
	public double bottom = 0;
	public long start = 0;
	public long end = 0;

	@Override
	public String toString() {
		String str = new String();
		str += "V:" + valid + ";C:" + confidence + "\n";
		str += "D:" + distance + ";Z:" + azimuth + ";L:" + altitude + ";P:" + plane + "\n";
		str += "X:" + x + ";Y:" + y + ";L:" + left + ";R:" + right + ";T:" + top + ";B:" + bottom + "\n";
		str += "B" + brightness + ";E" + exposure + ";W" + whitebalance + "\n";
		str += "HL" + h_min + ";HH" + h_max + ";SL" + s_min + ";SH" + s_max + ";LL" + l_min + ";LH" + l_max + "\n";
		return str;
	}
}
