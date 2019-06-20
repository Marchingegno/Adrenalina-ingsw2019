package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.CardRep;
import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.util.List;

public class WeaponRep extends CardRep {

	private List<AmmoType> price;
	private boolean isLoaded;
	private String weaponPath;

	public WeaponRep(WeaponCard weaponCardToRepresent) {
		super(weaponCardToRepresent);
		price = weaponCardToRepresent.getReloadPrice();
		this.isLoaded = weaponCardToRepresent.isLoaded();
		this.weaponPath = weaponCardToRepresent.getImagePath();
	}

	public String getWeaponName() {
		return getCardName();
	}

	public String getImagePath() {
		return weaponPath;
	}

	public String getWeaponDescription() {
		return getCardDescription();
	}

	public List<AmmoType> getPrice() {
		return price;
	}

	public boolean isLoaded() {
		return isLoaded;
	}
}
