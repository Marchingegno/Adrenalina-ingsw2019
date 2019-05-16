package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

/**
 * This class implements the Teleporter powerup
 *
 * @author MarcerAndrea
 */
public class Teleporter extends PowerupCard {

	private static final String DESCRIPTION =
			"You may play this card on your turn before\n" +
					"or after any action. Pick up your figure and\n" +
					"set it down on any square of the board. (You\n" +
					"can't use this after you see where someone\n" +
					"respawns at the end of your turn. By then it is\n" +
					"too late.)";

	public Teleporter(AmmoType associatedAmmo) {
		super("Teleporter", associatedAmmo, DESCRIPTION);
	}

	/**
	 * Activates the powerup.
	 *
	 * @param activatingPlayer player who as activated the powerup.
	 */
	@Override
	public void activatePowerup(Player activatingPlayer) {
		// TODO can be activated during the client's turn (same as the Newton card).
		// TODO ask client where to move (can be moved anywhere).
		// TODO move activatingPlayer.
	}

	@Override
	public String toString() {
		return "Teleporter";
	}

}