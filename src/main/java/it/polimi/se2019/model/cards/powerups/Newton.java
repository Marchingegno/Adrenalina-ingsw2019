package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

/**
 * This class implements the Newton powerup
 *
 * @author MarcerAndrea
 * @author Desno365
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
		super("Newton", associatedAmmo, DESCRIPTION, PowerupUseCaseType.ON_TURN);
	}


	@Override
	public void activatePowerup(Player activatingPlayer) {
		// TODO get targetPlayer possible movements (1 or 2 squares in one direction).
		// TODO ask client where to move giving possible movements.
		// TODO move targetPlayer.
	}

	/**
	 * Returns true if there is at least one not dead player different from the activating player.
	 * Needs gameBoard != null and activatingPlayer != null
	 * @return true if there is at least one not dead player different from the activating player.
	 */
	@Override
	public boolean canBeActivated() {
		for(Player player : getGameBoard().getPlayers()) {
			if(player != getOwnerPlayer() && !player.getPlayerBoard().isDead()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Newton";
	}

}