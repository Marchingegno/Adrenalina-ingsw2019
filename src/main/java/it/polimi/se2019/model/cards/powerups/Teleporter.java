package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.network.message.CoordinatesAnswerMessage;
import it.polimi.se2019.network.message.Message;
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
	public QuestionContainer doPowerupStep(Message answer) {
		incrementStep();
		if(getCurrentStep() == 1) {
			return firstStep();
		} else if(getCurrentStep() == 2) {
			resetCurrentStep();
			lastStep(answer);
			return null;
		} else {
			resetCurrentStep();
			Utils.logError("Wrong progress.", new IllegalStateException());
			return null;
		}
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


	private QuestionContainer firstStep() {
		List<Coordinates> coordinates = getGameBoard().getGameMap().getAllCoordinatesExceptPlayer(getOwner());
		return QuestionContainer.createCoordinatesQuestionContainer("Choose where to move.", coordinates);
	}

	private void lastStep(Message answer) {
		Coordinates targetCoordinates = ((CoordinatesAnswerMessage) answer).getSingleCoordinates();
		if(!targetCoordinates.equals(getGameBoard().getGameMap().getPlayerCoordinates(getOwner()))) {
			getGameBoard().getGameMap().movePlayerTo(getOwner(), targetCoordinates);
		}
	}

}