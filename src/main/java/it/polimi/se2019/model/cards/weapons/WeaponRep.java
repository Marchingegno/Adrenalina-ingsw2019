package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.utils.Utils;

public class WeaponRep {

	private String weaponName;
	private String weaponDescription;
	private String price;

	public WeaponRep(WeaponCard weaponCardToRepresent){
		this.weaponName = weaponCardToRepresent.getWeaponName();
		this.weaponDescription = weaponCardToRepresent.getDescription();
		price = Utils.getColoredString("◼", weaponCardToRepresent.getReloadPrice().get(0).getCharacterColorType()) +
				Utils.getColoredString("◼", weaponCardToRepresent.getReloadPrice().get(1).getCharacterColorType()) +
				Utils.getColoredString("◼", weaponCardToRepresent.getReloadPrice().get(2).getCharacterColorType());
	}

	public String getWeaponName() {
		return weaponName;
	}

	public String getWeaponDescription() {
		return weaponDescription;
	}

	public String getPrice() {
		return price;
	}
}
