package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Color;

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
	public void activatePowerup(Player activatingPlayer) {
		// TODO can be activated during the client's turn (same as the Teleporter card).
		// TODO get targetPlayer possible movements (1 or 2 squares in one direction).
		// TODO ask client where to move giving possible movements.
		// TODO move targetPlayer.
	}

	public String toString(){
		return Color.getColoredString("◼", this.getAssociatedAmmo().getCharacterColorType()) + "Newton";
	}

}