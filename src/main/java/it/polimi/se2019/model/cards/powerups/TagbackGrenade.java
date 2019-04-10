package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

/**
 * This class implements the Tagback grenade powerup
 * @author MarcerAndrea
 */
public class TagbackGrenade extends PowerupCard {

	private static final String DESCRIPTION = "TagbackGranade description";

	public TagbackGrenade(AmmoType associatedAmmo) {
		super(associatedAmmo, DESCRIPTION);
	}


	@Override
	public void activatePowerup(Player targetPlayer) {
	}

}