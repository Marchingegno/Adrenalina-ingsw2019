package it.polimi.se2019.model.cards.ammo;

import it.polimi.se2019.utils.Utils;

public enum AmmoType {
	RED_AMMO(Utils.CharacterColorType.RED),
	YELLOW_AMMO(Utils.CharacterColorType.YELLOW),
	BLUE_AMMO(Utils.CharacterColorType.BLUE);

	Utils.CharacterColorType colorType;

	AmmoType(Utils.CharacterColorType color){
		colorType = color;
	}

	public Utils.BackgroundColorType getBackgroundColorType() {
		return Utils.CharacterColorType.convertBackgroundColor(colorType);
	}

	public Utils.CharacterColorType getCharacterColorType() {
		return colorType;
	}
}