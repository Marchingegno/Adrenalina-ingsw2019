package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.io.Serializable;

public class PowerupCardRep implements Serializable {

	private AmmoType associatedAmmo;
	private String powerupName;
	private String description;

	public PowerupCardRep(PowerupCard powerupCard) {
		this.associatedAmmo = powerupCard.getAssociatedAmmo();
		this.powerupName = powerupCard.toString();
		this.description = powerupCard.getDescription();
	}

	/**
	 * Returns the ammo associated with the card.
	 *
	 * @return the ammo associated with the card.
	 */
	public AmmoType getAssociatedAmmo() {
		return associatedAmmo;
	}

	/**
	 * Returns the description of the card.
	 *
	 * @return the description of the card.
	 */
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return powerupName;
	}
}
