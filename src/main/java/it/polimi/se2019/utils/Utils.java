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
	private static final boolean ENABLE_WEAPON_LOG = false;
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

	/**
	 * Method to log a weapon.
	 *
	 * @param msg the log.
	 */
	public static void logWeapon(String msg) {
		if (ENABLE_WEAPON_LOG) {
			System.out.println(Color.getColoredString("WEAPON:", Color.CharacterColorType.RED, Color.BackgroundColorType.MAGENTA) + " " + msg);
		}
	}

	/**
	 * Method to log a rep.
	 * @param msg the log.
	 */
	public static void logRep(String msg) {
		if (DEBUG_REPS) {
			logInfo(msg);
		}
	}

	/**
	 * Method to log an error.
	 * @param msg the log.
	 * @param e the exception.
	 */
	public static void logError(String msg, Throwable e) {
		if (logEnabled)
			LOGGER.log(Level.SEVERE, msg, e);
	}

	/**
	 * Method to log a warning.
	 * @param msg the log.
	 */
	public static void logWarning(String msg) {
		if (logEnabled) {
			System.out.println(Color.getColoredString("WARNING:", Color.CharacterColorType.RED, Color.BackgroundColorType.YELLOW) + " " + msg);
		}
	}

	/**
	 * Method to log an info.
	 * @param msg the log.
	 */
	public static void logInfo(String msg) {
		if (logEnabled) {
			System.out.println(Color.getColoredString("INFO:", Color.CharacterColorType.RED, Color.BackgroundColorType.BLUE) + " " + msg);
		}
	}

	static Logger getGlobalLogger() {
		return LOGGER;
	}

	/**
	 * Returns a sting created from the specified one with the specified length, if the string is shorter the remaining length is filled with spaces.
	 *
	 * @param inputString the starting string.
	 * @param length      length of the output string.
	 * @return a sting created from the specified one with the specified length, if the string is shorter the remaining length is filled with spaces.
	 */
	public static String fillWithSpaces(String inputString, int length) {
		return fillWithSpacesColored(inputString, length, Color.CharacterColorType.DEFAULT);
	}

	/**
	 * Returns a sting of spaces of the specified length.
	 * @param length length of the output string.
	 * @return a sting of spaces of the specified length.
	 */
	public static String fillWithSpaces(int length) {
		return fillWithSpacesColored("", length, Color.CharacterColorType.DEFAULT);
	}

	/**
	 * Returns the a class with all the server configs.
	 * @return the a class with all the server configs.
	 */
	public static ServerConfig getServerConfig() {
		return serverConfig;
	}

	/**
	 * Returns a sting created from the specified one with the specified length and with the specified color, if the string is shorter the remaining length is filled with spaces.
	 * @param inputString the starting string.
	 * @param length the length of the final string.
	 * @param color the colo of the string.
	 * @return a sting created from the specified one with the specified length and with the specified color, if the string is shorter the remaining length is filled with spaces.
	 */
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

	/**
	 * Checks if the array of strings contains a certain element.
	 *
	 * @param array   the array of strings.
	 * @param element the string.
	 * @return true if element is present in array.
	 */
	public static boolean contains(String[] array, String element) {
		for (String str : array) {
			if (str.equals(element))
				return true;
		}
		return false;
	}
}

