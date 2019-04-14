package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

/**
 * This class implements the Teleporter powerup
 * @author MarcerAndrea
 */
public class Teleporter extends PowerupCard {

	private static final String DESCRIPTION = "Teleport description";

	public Teleporter(AmmoType associatedAmmo){	super(associatedAmmo, DESCRIPTION);	}

	@Override
	public void activatePowerup(Player activatingPlayer) {
		// TODO can be activated during the client's turn (same as the Newton card).
		// TODO ask client where to move (can be moved anywhere).
		// TODO move activatingPlayer.
	}

}