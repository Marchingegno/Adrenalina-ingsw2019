package it.polimi.se2019.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains general utilities for the program.
 * @author Desno365
 */
public class Utils {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static boolean debug = true;
	public static final boolean BYPASS = false;
	private static final ServerConfig serverConfig = new ServerConfigParser().parseConfig();

	/**
	 * Since it's an utility class it can't be instantiated.
	 */
	private Utils() {
		throw new IllegalStateException("Cannot create an instance of this utility class.");
	}

	public static void setDebug(boolean debug){
		Utils.debug = debug;
	}

	public static void logError(String msg, Throwable e) {
		if(debug)
			LOGGER.log(Level.SEVERE, msg, e);
	}

	public static void logWarning(String msg) {
		if(debug) {
			System.out.println(Color.setColorString(Color.CharacterColorType.RED, Color.BackgroundColorType.YELLOW) + "WARNING:" + Color.resetColorString() + " " + msg);
		}
	}

	public static void logInfo(String msg) {
		if(debug) {
			System.out.println(Color.setColorString(Color.CharacterColorType.RED, Color.BackgroundColorType.BLUE) + "INFO:" + Color.resetColorString() + " " + msg);
			//LOGGER.log(Level.INFO, msg);
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
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Color.getColoredString(inputString, color));
		while (stringBuilder.length() < length - inputString.length()) {
			stringBuilder.append('0');
		}

		return stringBuilder.toString();
	}


}

class ServerConfigParser {

	private final String FILE = "server-config.json";

	public ServerConfig parseConfig() {
		InputStream in = getClass().getResourceAsStream("/" + FILE);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			Gson gson = new com.google.gson.GsonBuilder().create();
			return gson.fromJson(reader, ServerConfig.class);
		} catch (com.google.gson.JsonParseException e) {
			Utils.logError("Cannot parse server-config.json.", e);
		}
		return null;
	}
}