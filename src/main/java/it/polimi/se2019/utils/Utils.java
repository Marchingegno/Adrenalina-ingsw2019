package it.polimi.se2019.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains general utilities for the program.
 * @author Desno365
 */
public class Utils {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static boolean DEBUG = true;
	public static boolean BYPASS = true;
	/**
	 * Since it's an utility class it can't be instantiated.
	 */
	private Utils() {
		throw new IllegalStateException("Cannot create an instance of this utility class.");
	}

	public static void setDebug(boolean set){
		DEBUG = set;
	}

	public static void logError(String msg, Throwable e) {
		if(DEBUG)
			LOGGER.log(Level.SEVERE, msg, e);
	}

	public static void logWarning(String msg) {
		if(DEBUG) {
			System.out.println(Color.setColorString(Color.CharacterColorType.RED, Color.BackgroundColorType.YELLOW) + "WARNING:" + Color.resetColorString() + " " + msg);
		}
	}

	public static void logInfo(String msg) {
		if(DEBUG) {
			System.out.println(Color.setColorString(Color.CharacterColorType.RED, Color.BackgroundColorType.BLUE) + "INFO:" + Color.resetColorString() + " " + msg);
			//LOGGER.log(Level.INFO, msg);
		}
	}

	public static void printLine(String string) {
		System.out.println(string);
	}

	public static Logger getGlobalLogger() {
		return LOGGER;
	}

	public static String fillWithSpaces(String inputString, int length) {
		return fillWithSpacesColored(inputString, length, Color.CharacterColorType.DEFAULT);
	}

	public static String fillWithSpacesColored(String inputString, int length, Color.CharacterColorType color) {
		if (inputString.length() >= length) {
			return inputString;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Color.getColoredString(inputString, color));
		while (stringBuilder.length() < length - inputString.length()) {
			stringBuilder.append('0');
		}

		return stringBuilder.toString();
	}


}
