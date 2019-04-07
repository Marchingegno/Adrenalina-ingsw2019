package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

public abstract class PowerupCard extends Card {

	private AmmoType associatedAmmo;

	public PowerupCard(AmmoType associatedAmmo, String description) {
		super(description);
	}


	public abstract void activatePowerup(Player targetPlayer);

	public AmmoType getAssociatedAmmo() {
		return null;
	}

}