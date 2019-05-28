package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

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


	// ####################################
	// OVERRIDDEN METHODS
	// ####################################

	/**
	 * Returns true if the activatingPlayer can see the shootingPlayer.
	 * Needs shootingPlayer != null
	 * @return true if the activatingPlayer can see the shootingPlayer.
	 */
	@Override
	public boolean canBeActivated() {
		if(getShootingPlayer() == null) {
			Utils.logError("No shooting player set.", new IllegalStateException());
			return false;
		}

		return getGameBoard().getGameMap().isVisible(getOwner(), getShootingPlayer());
	}

	@Override
	public QuestionContainer initialQuestion() {
		if(canBeActivated()) {
			Player targetPlayer = getShootingPlayer();
			targetPlayer.getPlayerBoard().addMarks(getOwner(), GIVEN_MARKS); // add marks to the target player.
		} else {
			throw new IllegalArgumentException(getCardName() + " was activated but it can't be activated.");
		}
		concludeActivation();
		return null;
	}

	@Override
	public QuestionContainer doActivationStep(int choice) {
		throw new UnsupportedOperationException("No step required for " + getCardName());
	}

}