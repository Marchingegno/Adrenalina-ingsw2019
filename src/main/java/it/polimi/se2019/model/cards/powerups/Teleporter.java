package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.List;

/**
 * This class implements the Teleporter powerup
 *
 * @author MarcerAndrea
 * @author Desno365
 */
public class Teleporter extends PowerupCard {

	private List<Coordinates> allowedCoordinates;

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


	// ####################################
	// OVERRIDDEN METHODS
	// ####################################

	/**
	 * Returns always true since this powerup can always be activated.
	 * @return true since this powerup can always be activated.
	 */
	@Override
	public boolean canBeActivated() {
		return true;
	}

	@Override
	public QuestionContainer initialQuestion() {
		incrementCurrentStep();
		return firstStep();
	}

	@Override
	public QuestionContainer doActivationStep(int choice) {
		incrementCurrentStep();
		if(getCurrentStep() == 2) {
			resetCurrentStep();
			lastStep(choice);
		} else {
			resetCurrentStep();
			Utils.logError("Wrong progress.", new IllegalStateException());
		}
		return null;
	}


	// ####################################
	// PRIVATE METHODS
	// ####################################

	private QuestionContainer firstStep() {
		allowedCoordinates = getGameBoard().getGameMap().getAllCoordinatesExceptPlayer(getOwner());
		return QuestionContainer.createCoordinatesQuestionContainer("Choose where to move.", allowedCoordinates);
	}

	private void lastStep(int choice) {
		if (choice >= 0 && choice < allowedCoordinates.size()) {
			getGameBoard().getGameMap().movePlayerTo(getOwner(), allowedCoordinates.get(choice));
		}
	}

}