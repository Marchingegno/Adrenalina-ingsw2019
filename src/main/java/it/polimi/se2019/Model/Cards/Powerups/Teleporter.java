package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

public class Teleporter extends PowerupCard {

	public Teleporter(AmmoType associatedAmmo, String description) {
		super(associatedAmmo, description);
	}

	@Override
	public void activatePowerup(Player targetPlayer) {
	}

}