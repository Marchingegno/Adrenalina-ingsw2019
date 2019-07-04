package it.polimi.se2019.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains general utilities for the program.
 *
 * @author Desno365
 */
public class Utils {

	public static final boolean DEBUG_BYPASS_CONFIGURATION = false;
	public static final boolean DEBUG_CLI = false;
	public static final boolean DEBUG_REPS = false;
	public static final boolean ENABLE_WEAPON_LOG = false;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static boolean logEnabled = false;
	private static final ServerConfig serverConfig = new ServerConfigParser().parseConfig();

	/**
	 * Since it's an utility class it can't be instantiated.
	 */
	private Utils() {
		throw new IllegalStateException("Cannot create an instance of this utility class.");
	}

	public static void setLogEnabled(boolean logEnabled) {
		Utils.logEnabled = logEnabled;
	}

	public static void logWeapon(String msg) {
		if (ENABLE_WEAPON_LOG) {
			System.out.println(Color.getColoredString("WEAPON:", Color.CharacterColorType.RED, Color.BackgroundColorType.MAGENTA) + " " + msg);
		}
	}

	public static void logRep(String msg) {
		if (DEBUG_REPS) {
			logInfo(msg);
		}
	}

	public static void logError(String msg, Throwable e) {
		if (logEnabled)
			LOGGER.log(Level.SEVERE, msg, e);
	}

	public static void logWarning(String msg) {
		if (logEnabled) {
			System.out.println(Color.getColoredString("WARNING:", Color.CharacterColorType.RED, Color.BackgroundColorType.YELLOW) + " " + msg);
		}
	}

	public static void logInfo(String msg) {
		if (logEnabled) {
			System.out.println(Color.getColoredString("INFO:", Color.CharacterColorType.RED, Color.BackgroundColorType.BLUE) + " " + msg);
		}
	}

	static Logger getGlobalLogger() {
		return LOGGER;
	}

	public static String fillWithSpaces(String inputString, int length) {
		return fillWithSpacesColored(inputString, length, Color.CharacterColorType.DEFAULT);
	}

	public static String fillWithSpaces(int length) {
		return fillWithSpacesColored("", length, Color.CharacterColorType.DEFAULT);
	}

	public static ServerConfig getServerConfig() {
		return serverConfig;
	}

	public static String fillWithSpacesColored(String inputString, int length, Color.CharacterColorType color) {
		if (inputString.length() >= length) {
			return inputString;
		}
		StringBuilder stringBuilder = new StringBuilder(inputString);
		while (stringBuilder.length() < length) {
			stringBuilder.append(" ");
		}
		return Color.getColoredString(stringBuilder.toString(), color);
	}

	public static boolean contains(String[] array, String element) {
		for (String str : array) {
			if (str.equals(element))
				return true;
		}
		return false;
	}
}

