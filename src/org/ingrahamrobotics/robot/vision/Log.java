package org.ingrahamrobotics.robot.vision;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Log {
	public static boolean kENABLE_VISION_LOG = true;
	public static final int kNUM_VISION_LOGS = 3;
	public static final String kDEFAULT_HOME = "/home/lvuser";
	public static final String kVISION_DIR_NAME = "vision";
	
	private static String home;
	private static Path vision;
	
	public Log() {
		home = System.getProperty("user.home", kDEFAULT_HOME);
		vision = Paths.get(home, kVISION_DIR_NAME);
	}
	
	public static Path getPath() {
		return vision;
	}
	
	public static void reset() {
		reset(vision);
	}

	private static void reset(Path dir) {
		// Not fully recursive -- assumes only one layer of files
		for (File file : dir.toFile().listFiles()) {
			file.delete();
		}
	}

	public static void rotate() {
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
