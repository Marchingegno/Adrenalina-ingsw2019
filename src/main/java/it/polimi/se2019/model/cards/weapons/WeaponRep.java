package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.CardRep;
import it.polimi.se2019.utils.Color;

public class WeaponRep extends CardRep {

	private String price;

	public WeaponRep(WeaponCard weaponCardToRepresent){
		super(weaponCardToRepresent);
		//TODO get price from weapon
		price = Color.getColoredString("◼", Color.CharacterColorType.RED) +
				Color.getColoredString("◼", Color.CharacterColorType.YELLOW ) +
				Color.getColoredString("◼", Color.CharacterColorType.DEFAULT );
	}

	public String getWeaponName() {
		return getCardName();
	}

	public String getWeaponDescription() {
		return getCardDescription();
	}

	public String getPrice() {
		return price;
	}
}
