package it.polimi.se2019.model.cards.ammo;

import it.polimi.se2019.utils.Color;

/**
 * Enumeration of the possible ammo in the game.
 *
 * @author Desno365
 * @author MarcerAndrea
 */
public enum AmmoType {
	RED_AMMO(Color.CharacterColorType.RED),
	YELLOW_AMMO(Color.CharacterColorType.YELLOW),
	BLUE_AMMO(Color.CharacterColorType.BLUE);

	Color.CharacterColorType color;

	AmmoType(Color.CharacterColorType color) {
		this.color = color;
	}

	/**
	 * Returns the color for the background.
	 *
	 * @return the color for the background.
	 */
	public Color.BackgroundColorType getBackgroundColorType() {
		return Color.CharacterColorType.convertBackgroundColor(color);
	}

	/**
	 * Returns the color to color a string.
	 *
	 * @return the color to color a string.
	 */
	public Color.CharacterColorType getCharacterColorType() {
		return color;
	}
}