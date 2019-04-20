package it.polimi.se2019.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains general utilities for the program.
 * @author Desno365
 */
public class Utils {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static final int BLACK = 30;//better not to use
	public static final int RED = 31;
	public static final int GREEN = 32;
	public static final int YELLOW = 33;
	public static final int BLUE = 34;
	public static final int MAGENTA = 35;
	public static final int CYAN = 36;
	public static final int WHITE = 37;//better not to use
	public static final int DEFAULT_COLOR = 39;

	public static final int BLACK_BACKGROUND = 40;//better not to use
	public static final int RED_BACKGROUND = 41;
	public static final int GREEN_BACKGROUND = 42;
	public static final int YELLOW_BACKGROUND = 43;
	public static final int BLUE_BACKGROUND = 44;
	public static final int MAGENTA_BACKGROUND = 45;
	public static final int CYAN_BACKGROUND = 46;
	public static final int WHITE_BACKGROUND = 47; //better not to use
	public static final int DEFAULT_BACKGROUND = 49;

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
		System.out.println(setColorString(31, 44) + "INFO:" + resetColorString() + " " + msg);
		//LOGGER.log(Level.INFO, msg);
	}

	public static Logger getGlobalLogger() {
		return LOGGER;
	}

	public static String getColoredString(String string, int characterColor, int backgroundColor){
		return setColorString(characterColor, backgroundColor) + string + resetColorString();
	}

	public static String getColoredCell(int backgroundColor){
		return setColorString(DEFAULT_COLOR, backgroundColor)+ " " + resetColorString();
	}

	public static String setColorString(int characterColor, int backgroundColor) {
		if(characterColor < 30 || characterColor > 39)
			throw new IllegalArgumentException("Character color must be between 30 and 39.");
		if(backgroundColor < 40 || backgroundColor > 49)
			throw new IllegalArgumentException("Background color must be between 40 and 49.");
		/*
		+~~~~~~+~~~~~~+~~~~~~~~~~~~~~~~~~~~~+
		|  fg  |  bg  |  color              |
		+~~~~~~+~~~~~~+~~~~~~~~~~~~~~~~~~~~~+
		|  30  |  40  |  black or white (!) |
		|  31  |  41  |  red                |
		|  32  |  42  |  green              |
		|  33  |  43  |  yellow             |
		|  34  |  44  |  blue               |
		|  35  |  45  |  magenta            |
		|  36  |  46  |  cyan               |
		|  37  |  47  |  white or grey      |
		|  39  |  49  |  default            |
		+~~~~~~+~~~~~~+~~~~~~~~~~~~~~~~~~~~~+
		*/
		return (char)27 + "[" + characterColor + ";" + backgroundColor + "m";
	}

	public static String resetColorString() {
		return setColorString(39, 49);
	}
	
	@SuppressWarnings("unused")
	private static void testColors() {
		for (int i = 30; i <= 39; i++) {
			for (int j = 40; j <= 49; j++) {
				System.out.print(setColorString(i, j) + " TEST " + resetColorString());
			}
			System.out.print("\n");
		}
	}

}
