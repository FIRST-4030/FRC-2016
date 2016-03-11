package org.ingrahamrobotics.robot.vision;

import java.nio.file.Path;

import org.ingrahamrobotics.robot.dashboard.Settings;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.RGBValue;
import com.ni.vision.NIVision.Range;

public class Transform {

	public static final int kBINARY_COLOR = 255;

	private int[] thresholds;

	public enum ThresholdType {
		H_LOW(), H_HIGH(), S_LOW(), S_HIGH(), L_LOW(), L_HIGH();
	}

	public Transform() {
		thresholds = new int[ThresholdType.values().length];
	}

	public void updateSettings() {
		for (ThresholdType i : ThresholdType.values()) {
			switch (i) {
			case H_HIGH:
				thresholds[i.ordinal()] = Settings.Key.VISION_H_HIGH.getInt();
				break;
			case H_LOW:
				thresholds[i.ordinal()] = Settings.Key.VISION_H_LOW.getInt();
				break;
			case L_HIGH:
				thresholds[i.ordinal()] = Settings.Key.VISION_L_HIGH.getInt();
				break;
			case L_LOW:
				thresholds[i.ordinal()] = Settings.Key.VISION_L_LOW.getInt();
				break;
			case S_HIGH:
				thresholds[i.ordinal()] = Settings.Key.VISION_S_HIGH.getInt();
				break;
			case S_LOW:
				thresholds[i.ordinal()] = Settings.Key.VISION_S_LOW.getInt();
				break;
			}
		}
	}

	public int getThreshold(ThresholdType i) {
		return thresholds[i.ordinal()];
	}

	public void save(Image image, Path path) {
		RGBValue value = new NIVision.RGBValue();
		NIVision.imaqWriteFile(image, path.toString(), value);
		value.free();
	}

	public Image read(Path path) {
		Image image = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
		NIVision.imaqReadFile(image, path.toString());
		return image;
	}

	public Image thresholdHSL(Image image) {
		Image binary = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		Range range1 = new Range(thresholds[ThresholdType.H_LOW.ordinal()], thresholds[ThresholdType.H_HIGH.ordinal()]);
		Range range2 = new Range(thresholds[ThresholdType.S_LOW.ordinal()], thresholds[ThresholdType.S_HIGH.ordinal()]);
		Range range3 = new Range(thresholds[ThresholdType.L_LOW.ordinal()], thresholds[ThresholdType.L_HIGH.ordinal()]);
		NIVision.imaqColorThreshold(binary, image, kBINARY_COLOR, NIVision.ColorMode.HSL, range1, range2, range3);
		range1.free();
		range2.free();
		range3.free();
		return binary;
	}
}
