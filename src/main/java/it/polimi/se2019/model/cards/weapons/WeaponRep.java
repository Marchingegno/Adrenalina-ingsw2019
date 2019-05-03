package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.utils.Color;

import java.io.Serializable;

public class WeaponRep implements Serializable {

	private String weaponName;
	private String weaponDescription;
	private String price;

	public WeaponRep(WeaponCard weaponCardToRepresent){
		this.weaponName = weaponCardToRepresent.getWeaponName();
		this.weaponDescription = weaponCardToRepresent.getDescription();
		price = Color.getColoredString("◼", Color.CharacterColorType.RED) +
				Color.getColoredString("◼", Color.CharacterColorType.YELLOW ) +
				Color.getColoredString("◼", Color.CharacterColorType.DEFAULT );
		/*
		price = Color.getColoredString("◼", weaponCardToRepresent.getReloadPrice().get(0).getCharacterColorType()) +
				Color.getColoredString("◼", weaponCardToRepresent.getReloadPrice().get(1).getCharacterColorType()) +
				Color.getColoredString("◼", weaponCardToRepresent.getReloadPrice().get(2).getCharacterColorType());*/
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
