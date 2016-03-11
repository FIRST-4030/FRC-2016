package org.ingrahamrobotics.robot.vision;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.ni.vision.NIVision.Image;

public class Log {
	public static boolean kENABLE_VISION_LOG = true;
	public static final int kNUM_VISION_LOGS = 3;
	public static final String kDEFAULT_HOME = "/home/lvuser";
	public static final String kVISION_DIR_NAME = "vision";
	public static final long kMIN_FREE_BYTES = 1024 * 1024 * 10; // 10 MB

	private static Path vision = null;
	private String home;
	private Transform trans;

	public Log() {
		if (!kENABLE_VISION_LOG || vision != null) {
			return;
		}
		home = System.getProperty("user.home", kDEFAULT_HOME);
		vision = Paths.get(home, kVISION_DIR_NAME);
		trans = new Transform();
	}

	private boolean checkFreeSpace() {
		if (vision.toFile().getFreeSpace() < kMIN_FREE_BYTES) {
			kENABLE_VISION_LOG = false;
			System.err.println("Disabled vision log due to limited free space");
			return false;
		}
		return true;
	}

	public static Path getPath() {
		return vision;
	}

	public void save(Image image, String name) {
		checkFreeSpace();
		if (!kENABLE_VISION_LOG) {
			return;
		}

		Path file = Paths.get(vision.toString(), name);
		trans.save(image, file);
	}

	public void save(String str, String name) {
		checkFreeSpace();
		if (!kENABLE_VISION_LOG) {
			return;
		}

		Path file = Paths.get(vision.toString(), name);
		try {
			PrintWriter out = new PrintWriter(file.toFile());
			out.print(str);
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("Unable to save log string file");
		}
	}

	public void reset() {
		reset(vision);
	}

	private void reset(Path dir) {
		if (!kENABLE_VISION_LOG) {
			return;
		}

		// Not fully recursive -- assumes only one layer of files
		for (File file : dir.toFile().listFiles()) {
			file.delete();
		}
	}

	public void rotate() {
		if (!kENABLE_VISION_LOG) {
			return;
		}

		for (int i = kNUM_VISION_LOGS; i > 0; i--) {
			Path dir = Paths.get(vision.toString() + "." + i);

			// Skip missing folders
			if (Files.notExists(dir)) {
				continue;
			}

			// Die if there is a naming error
			if (!Files.isDirectory(dir)) {
				System.err.println("Existing non-directory vision log: " + dir.toString());
				kENABLE_VISION_LOG = false;
				return;
			}

			// Rotate numbered logs, deleting the oldest
			if (i == kNUM_VISION_LOGS) {
				reset(dir);
				try {
					Files.delete(dir);
				} catch (IOException e) {
					System.err.println("Unable to move vision log directory: " + dir.toString());
					kENABLE_VISION_LOG = false;
					return;
				}
			} else {
				// Rename
				Path target = Paths.get(home, kVISION_DIR_NAME + "." + (i + 1));
				try {
					Files.move(dir, target);
				} catch (IOException e) {
					System.err.println("Unable to move vision log directory: " + dir.toString());
					kENABLE_VISION_LOG = false;
					return;
				}
			}
		}

		// Move the unnumbered log
		if (Files.isDirectory(vision)) {
			// Rename
			Path target = Paths.get(home, kVISION_DIR_NAME + ".1");
			try {
				Files.move(vision, target);
			} catch (IOException e) {
				System.err.println("Unable to move vision log directory: " + vision.toString());
				kENABLE_VISION_LOG = false;
				return;
			}
		}

		// Build our current directory
		try {
			Files.createDirectory(vision);
		} catch (IOException e) {
			System.err.println("Unable to create vision log directory");
			kENABLE_VISION_LOG = false;
			return;
		}
	}
}
