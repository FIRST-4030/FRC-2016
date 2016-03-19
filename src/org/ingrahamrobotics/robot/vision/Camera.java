package org.ingrahamrobotics.robot.vision;

import org.ingrahamrobotics.robot.output.Output;
import org.ingrahamrobotics.robot.output.OutputLevel;
import org.ingrahamrobotics.robot.output.Settings;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;

import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera {

	private String name;
	private USBCamera cam;

	// Camera settings - Static
	public static final int width = 640;
	public static final int height = 480;
	public static final double fovH = 59.7;
	public static final double fovV = fovH / 16 * 9;
	public static final int fps = 4;

	// Camera settings - Dynamic
	private static int whitebalance = 4700; // Color temperature in K, -1 is
											// auto
	private static int brightness = -1; // 0 - 100, -1 is "do not set"
	private static int exposure = 0; // 0 - 100, -1 is "auto"

	public Camera(String name) {
		this.name = name;
	}

	public void start() {
		if (isRunning()) {
			return;
		}

		cam = new USBCamera(name);
		cam.openCamera();
		update();

		cam.startCapture();
		Output.output(OutputLevel.VISION, "camera", name);
	}

	public void stop() {
		if (isRunning()) {
			cam.closeCamera();
		}
		cam = null;
		Output.output(OutputLevel.VISION, "camera", "<Disabled>");
	}

	public boolean isRunning() {
		return (cam != null);
	}

	public void updateSettings() {
		int e = Settings.Key.VISION_EXPOSURE.getInt();
		int b = Settings.Key.VISION_BRIGHTNESS.getInt();
		int w = Settings.Key.VISION_WHITETEMP.getInt();

		// Only reset the camera when something changes
		if (e != exposure || b != brightness || w != whitebalance) {
			exposure = e;
			brightness = b;
			whitebalance = w;
			update();
		}
	}

	private void update() {
		if (!isRunning()) {
			return;
		}

		cam.setSize(width, height);
		cam.setFPS(fps);
		if (exposure >= 0) {
			cam.setExposureManual(exposure);
		} else {
			cam.setExposureAuto();
		}
		if (whitebalance >= 0) {
			cam.setWhiteBalanceManual(whitebalance);
		} else {
			cam.setWhiteBalanceAuto();
		}
		if (brightness >= 0) {
			cam.setBrightness(brightness);
		}
		cam.updateSettings();
	}

	public Image capture() {
		start();
		Image image = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
		cam.getImage(image);
		return image;
	}

	public int getBrightness() {
		return brightness;
	}

	public int getExposure() {
		return exposure;
	}

	public int getWhitebalance() {
		return whitebalance;
	}
}
