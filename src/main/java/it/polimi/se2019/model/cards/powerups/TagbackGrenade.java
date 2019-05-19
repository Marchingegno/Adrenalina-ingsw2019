package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.network.message.Message;

/**
 * This class implements the Tagback grenade powerup
 *
 * @author MarcerAndrea
 * @author Desno365
 */
public class TagbackGrenade extends PowerupCard {

	private static final String DESCRIPTION =
			"You may play this card\n" +
					"when you receive damage\n" +
					"from a player you can see.\n" +
					"Give that player 1 mark.";
	private static final int GIVEN_MARKS = 1;

	public TagbackGrenade(AmmoType associatedAmmo) {
		super("Tagback grenade", associatedAmmo, DESCRIPTION, PowerupUseCaseType.ON_DAMAGE);
	}


	@Override
	public PowerupInfo doPowerupStep(Message answer) {
		Player targetPlayer = getOwnerPlayer(); // TODO placeholder, must be targetPlayer.
		targetPlayer.getPlayerBoard().addMarks(getOwnerPlayer(), GIVEN_MARKS); // add marks to the target player.
		return null;
	}

	/**
	 * Returns true if the activatingPlayer can see the shootingPlayer.
	 * Needs gameBoard != null, activatingPlayer != null and shootingPlayer != null
	 * @return true if the activatingPlayer can see the shootingPlayer.
	 */
	@Override
	public boolean canBeActivated() {
		return getGameBoard().getGameMap().isVisible(getOwnerPlayer(), getShootingPlayer());
	}

	@Override
	public String toString() {
		return "Tagback Grenade";
	}

}