package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.utils.Color;

public class PowerupCardRep {

	private AmmoType associatedAmmo;
	private String powerupString;
	private String description;

	public PowerupCardRep(PowerupCard powerupCard){
		this.associatedAmmo = powerupCard.getAssociatedAmmo();
		this.powerupString = powerupCard.toString();
		this.description = powerupCard.getDescription();
	}

	/**
	 * @return the ammo associated with the card
	 */
	public AmmoType getAssociatedAmmo() {return associatedAmmo;}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return powerupString;
	}
}
