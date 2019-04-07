package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

public class TagbackGrenade extends PowerupCard {

	public TagbackGrenade(AmmoType associatedAmmo, String description) {
		super(associatedAmmo, description);
	}


	@Override
	public void activatePowerup(Player targetPlayer) {
	}

}