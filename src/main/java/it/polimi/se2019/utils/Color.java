package it.polimi.se2019.utils;

public class Color {

	/**
	 * Since it's an utility class it can't be instantiated.
	 */
	private Color() {
		throw new IllegalStateException("Cannot create an instance of this utility class.");
	}


	public static String getColoredString(String string, CharacterColorType characterColor, BackgroundColorType backgroundColor){
		return setColorString(characterColor, backgroundColor) + string + resetColorString();
	}

	public static String getColoredString(String string, CharacterColorType characterColor){
		return getColoredString(string, characterColor, BackgroundColorType.DEFAULT);
	}

	public static String getColoredCell(BackgroundColorType backgroundColor){
		return setColorString(CharacterColorType.DEFAULT, backgroundColor)+ " " + resetColorString();
	}

	static String setColorString(CharacterColorType characterColor, BackgroundColorType backgroundColor) {
		return (char)27 + "[" + characterColor.getCharacterColor() + ";" + backgroundColor.getBackgroundColor() + "m";
	}

	static String resetColorString() {
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
