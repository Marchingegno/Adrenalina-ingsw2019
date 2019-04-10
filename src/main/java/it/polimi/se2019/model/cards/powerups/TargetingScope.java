package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

/**
 * This class implements the Targeting scope powerup
 * @author MarcerAndrea
 */
public class TargetingScope extends PowerupCard {

	private static final String DESCRIPTION = "TargetingScope description";

	public TargetingScope(AmmoType associatedAmmo){	super(associatedAmmo, DESCRIPTION);	}

	@Override
	public void activatePowerup(Player targetPlayer) {
	}

}