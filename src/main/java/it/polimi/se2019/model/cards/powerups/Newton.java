package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

/**
 * This class implements the Newton powerup
 *
 * @author MarcerAndrea
 */
public class Newton extends PowerupCard {

	private static final String DESCRIPTION =
			"You may play this card on your turn before or\n" +
					"after any action. Choose any other player's\n" +
					"figure and move it 1 or 2 squares in one\n" +
					"direction. (You can't use this to move a figure\n" +
					"after it respawns at the end of your turn. That\n" +
					"would be too late.)";

	public Newton(AmmoType associatedAmmo) {
		super("Newton", associatedAmmo, DESCRIPTION);
	}

	/**
	 * Activates the powerup.
	 *
	 * @param activatingPlayer player who as activated the powerup.
	 */
	@Override
	public void activatePowerup(Player activatingPlayer) {
		// TODO can be activated during the client's turn (same as the Teleporter card).
		// TODO get targetPlayer possible movements (1 or 2 squares in one direction).
		// TODO ask client where to move giving possible movements.
		// TODO move targetPlayer.
	}

	@Override
	public String toString() {
		return "Newton";
	}

}