package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

/**
 * This class implements the Newton powerup
 * @author MarcerAndrea
 */
public class Newton extends PowerupCard {

	private static final String DESCRIPTION = "Newton description";

	public Newton(AmmoType associatedAmmo) {
		super(associatedAmmo, DESCRIPTION);
	}

	@Override
	public void activatePowerup(Player targetPlayer) {
	}

}