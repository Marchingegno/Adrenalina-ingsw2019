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
		System.out.println(setColorString(CharacterColorType.RED, BackgroundColorType.BLUE) + "INFO:" + resetColorString() + " " + msg);
		//LOGGER.log(Level.INFO, msg);
	}

	public static Logger getGlobalLogger() {
		return LOGGER;
	}

	public static String getColoredString(String string, CharacterColorType characterColor, BackgroundColorType backgroundColor){
		return setColorString(characterColor, backgroundColor) + string + resetColorString();
	}

	public static String getColoredString(String string, CharacterColorType characterColor){
		return setColorString(characterColor, BackgroundColorType.DEFAULT) + string + resetColorString();
	}

	public static String getColoredCell(BackgroundColorType backgroundColor){
		return setColorString(CharacterColorType.DEFAULT, backgroundColor)+ " " + resetColorString();
	}

	private static String setColorString(CharacterColorType characterColor, BackgroundColorType backgroundColor) {
		return (char)27 + "[" + characterColor.getCharacterColor() + ";" + backgroundColor.getBackgroundColor() + "m";
	}

	private static String resetColorString() {
		return setColorString(CharacterColorType.DEFAULT, BackgroundColorType.DEFAULT);
	}

	@SuppressWarnings("unused")
	private static void testColors() {
		for(CharacterColorType characterColor : CharacterColorType.values()) {
			for(BackgroundColorType backgroundColor : BackgroundColorType.values()) {
				System.out.print(setColorString(characterColor, backgroundColor) + " TEST " + resetColorString());
			}
			System.out.print("\n");
		}
	}

	public static String fillWithSpaces(String inputString, int length) {
		return fillWithSpacesColored(inputString, length, CharacterColorType.DEFAULT);
	}

	public static String fillWithSpacesColored(String inputString, int length, Utils.CharacterColorType color) {
		if (inputString.length() >= length) {
			return inputString;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getColoredString(inputString, color));
		while (stringBuilder.length() < length - inputString.length()) {
			stringBuilder.append('0');
		}

		return stringBuilder.toString();
	}


	public enum CharacterColorType {
		// NOTE: the color BLACK may be rendered white in different terminals
		// NOTE: the color WHITE may be rendered grey in different terminals
		BLACK(30), RED(31), GREEN(32), YELLOW(33), BLUE(34), MAGENTA(35), CYAN(36), WHITE(37), DEFAULT(39);

		private int characterColor;

		CharacterColorType(int characterColor) {
			this.characterColor = characterColor;
		}

		public int getCharacterColor() {
			return characterColor;
		}

		public static BackgroundColorType convertBackgroundColor(CharacterColorType characterColorToConvert){
			switch (characterColorToConvert) {
				case BLACK: return BackgroundColorType.BLACK;
				case RED: return BackgroundColorType.RED;
				case GREEN: return BackgroundColorType.GREEN;
				case YELLOW: return BackgroundColorType.YELLOW;
				case BLUE: return BackgroundColorType.BLUE;
				case MAGENTA: return BackgroundColorType.MAGENTA;
				case CYAN: return BackgroundColorType.CYAN;
				case WHITE: return BackgroundColorType.WHITE;
					default: return BackgroundColorType.DEFAULT;
			}
		}
	}

	public enum BackgroundColorType {
		// NOTE: the color BLACK may be rendered white in different terminals
		// NOTE: the color WHITE may be rendered grey in different terminals
		BLACK(40), RED(41), GREEN(42), YELLOW(43), BLUE(44), MAGENTA(45), CYAN(46), WHITE(47), DEFAULT(49);

		private int backgroundColor;

		BackgroundColorType(int backgroundColor) {
			this.backgroundColor = backgroundColor;
		}

		public int getBackgroundColor() {
			return backgroundColor;
		}
	}


}
