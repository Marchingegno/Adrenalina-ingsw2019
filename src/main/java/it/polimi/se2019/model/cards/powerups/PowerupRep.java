package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.CardRep;
import it.polimi.se2019.model.cards.ammo.AmmoType;

public class PowerupRep extends CardRep {

	private AmmoType associatedAmmo;

	public PowerupRep(PowerupCard powerupCard) {
		super(powerupCard);
		this.associatedAmmo = powerupCard.getAssociatedAmmo();
	}

	/**
	 * Returns the ammo associated with the card.
	 *
	 * @return the ammo associated with the card.
	 */
	public AmmoType getAssociatedAmmo() {
		return associatedAmmo;
	}
}
