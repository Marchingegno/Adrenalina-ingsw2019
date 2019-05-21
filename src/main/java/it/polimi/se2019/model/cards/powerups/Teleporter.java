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

	private int progress = 0;

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
		progress++;
		if(progress == 1) {
			return firstStep();
		} else if(progress == 2) {
			progress = 0;
			lastStep(answer);
			return null;
		} else {
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
		Coordinates start = getGameBoard().getGameMap().getPlayerCoordinates(getOwnerPlayer());
		List<Coordinates> allCoordinates = getGameBoard().getGameMap().reachableCoordinates(start, 10);

		return QuestionContainer.createCoordinatesQuestionContainer("Choose where to move.", allCoordinates);
	}

	private void lastStep(Message answer) {
		Coordinates targetCoordinates = ((CoordinatesAnswerMessage) answer).getSingleCoordinates();
		if(!targetCoordinates.equals(getGameBoard().getGameMap().getPlayerCoordinates(getOwnerPlayer()))) {
			getGameBoard().getGameMap().movePlayerTo(getOwnerPlayer(), targetCoordinates);
			getGameBoard().getGameMap().updateRep();
			getGameBoard().getGameMap().notifyObservers();
		}
	}

}