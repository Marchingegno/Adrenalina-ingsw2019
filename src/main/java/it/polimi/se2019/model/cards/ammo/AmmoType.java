package it.polimi.se2019.model.cards.ammo;

import it.polimi.se2019.utils.Color;

public enum AmmoType {
	RED_AMMO(Color.CharacterColorType.RED),
	YELLOW_AMMO(Color.CharacterColorType.YELLOW),
	BLUE_AMMO(Color.CharacterColorType.BLUE);

	Color.CharacterColorType colorType;

	AmmoType(Color.CharacterColorType color){
		colorType = color;
	}

	public Color.BackgroundColorType getBackgroundColorType() {
		return Color.CharacterColorType.convertBackgroundColor(colorType);
	}

	public Color.CharacterColorType getCharacterColorType() {
		return colorType;
	}
}