package it.polimi.se2019.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains general utilities for the program.
 * @author Desno365
 */
public class Utils {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public static final boolean DEBUG_BYPASS_CONFIGURATION = true;
	public static final boolean DEBUG_CLI = true;
	public static final boolean DEBUG_BYPASS_USE_GUI = false;
	private static final ServerConfig serverConfig = new ServerConfigParser().parseConfig();

	private static boolean logEnabled = true;

	/**
	 * Since it's an utility class it can't be instantiated.
	 */
	private Utils() {
		throw new IllegalStateException("Cannot create an instance of this utility class.");
	}

	public static void setLogEnabled(boolean logEnabled){
		Utils.logEnabled = logEnabled;
	}

	public static void logError(String msg, Throwable e) {
		if(logEnabled)
			LOGGER.log(Level.SEVERE, msg, e);
	}

	public static void logWarning(String msg) {
		if(logEnabled) {
			System.out.println(Color.setColorString(Color.CharacterColorType.RED, Color.BackgroundColorType.YELLOW) + "WARNING:" + Color.resetColorString() + " " + msg);
		}
	}

	public static void logInfo(String msg) {
		if(logEnabled) {
			System.out.println(Color.setColorString(Color.CharacterColorType.RED, Color.BackgroundColorType.BLUE) + "INFO:" + Color.resetColorString() + " " + msg);
		}
	}

	public static Logger getGlobalLogger() {
		return LOGGER;
	}

	public static String fillWithSpaces(String inputString, int length) {
		return fillWithSpacesColored(inputString, length, Color.CharacterColorType.DEFAULT);
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

class ServerConfigParser {

	private static final String FILE = "server-config.json";

	public ServerConfig parseConfig() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + FILE)));
		try {
			Gson gson = new com.google.gson.GsonBuilder().create();
			return gson.fromJson(reader, ServerConfig.class);
		} catch (com.google.gson.JsonParseException e) {
			Utils.logError("Cannot parse server-config.json.", e);
		}
		return null;
	}
}