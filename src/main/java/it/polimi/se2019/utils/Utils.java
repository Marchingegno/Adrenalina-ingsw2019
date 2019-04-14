package it.polimi.se2019.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains general utilities for the program.
 * @author Desno365
 */
public class Utils {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


	/**
	 * Since it's an utility class it can't be instantiated.
	 */
	private Utils() {
		throw new IllegalStateException("Cannot create an instance of this utility class.");
	}


	public static void logError(String msg, Throwable e) {
		LOGGER.log(Level.SEVERE, msg, e);
	}

	public static void logInfo(String msg) {
		LOGGER.log(Level.INFO, msg);
	}

	public static Logger getGlobalLogger() {
		return LOGGER;
	}

}
