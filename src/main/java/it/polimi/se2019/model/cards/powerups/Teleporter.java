package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.network.message.Message;

/**
 * This class implements the Teleporter powerup
 *
 * @author MarcerAndrea
 * @author Desno365
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
		super("Teleporter", associatedAmmo, DESCRIPTION, PowerupUseCaseType.ON_TURN);
	}


	@Override
	public PowerupInfo doPowerupStep(Message answer) {
		// TODO ask client where to move (can be moved anywhere).
		// TODO move activatingPlayer.
		return null;
	}

	/**
	 * Returns always true since this powerup can always be activated.
	 * @return true since this powerup can always be activated.
	 */
	@Override
	public boolean canBeActivated() {
		return true;
	}

	@Override
	public String toString() {
		return "Teleporter";
	}

}