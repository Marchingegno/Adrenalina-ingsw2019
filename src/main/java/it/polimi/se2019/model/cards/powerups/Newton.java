package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements the Newton powerup.
 *
 * @author MarcerAndrea
 * @author Desno365
 */
public class Newton extends PowerupCard {

	private Player targetPlayer;
	private List<Player> targettablePlayers;
	private List<Coordinates> allowedCoordinates;

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


	// ####################################
	// OVERRIDDEN METHODS
	// ####################################

	/**
	 * Returns true if there is at least one not dead player different from the activating player.
	 * @return true if there is at least one not dead player different from the activating player.
	 */
	@Override
	public boolean canBeActivated() {
		return getGameBoard().getPlayers().stream().anyMatch(this::canActivateOnPlayer);
	}

	/**
	 * Returns the initial question and options.
	 *
	 * @return the initial question and options.
	 */
	@Override
	public QuestionContainer initialQuestion() {
		incrementCurrentStep();
		return firstStep();
	}

	/**
	 * Performs the powerup action according to the player choice.
	 * @param choice the choice of the player.
	 * @return question and options to send to the player.
	 */
	@Override
	public QuestionContainer doActivationStep(int choice) {
		incrementCurrentStep();
		if(getCurrentStep() == 2) {
			return secondStep(choice);
		} else if(getCurrentStep() == 3) {
			resetCurrentStep();
			lastStep(choice);
			return null;
		} else {
			resetCurrentStep();
			Utils.logError("Wrong progress.", new IllegalStateException());
			return null;
		}
	}


	// ####################################
	// PRIVATE METHODS
	// ####################################

	private QuestionContainer firstStep() {
		targettablePlayers = getGameBoard().getPlayers().stream()
				.filter(this::canActivateOnPlayer)
				.collect(Collectors.toList());

		List<String> playerNames = targettablePlayers.stream()
				.map(player -> Color.getColoredString(player.getPlayerName(), player.getPlayerColor()))
				.collect(Collectors.toList());

		return QuestionContainer.createStringQuestionContainer("Choose the player to move.", playerNames);
	}

	private QuestionContainer secondStep(int choice) {
		if (choice >= 0 && choice < targettablePlayers.size()) {
			targetPlayer = targettablePlayers.get(choice);
			allowedCoordinates = getGameBoard().getGameMap().reachablePerpendicularCoordinates(targetPlayer, 2);

			return QuestionContainer.createCoordinatesQuestionContainer("Enter where to move " + Color.getColoredString(targetPlayer.getPlayerName(), targetPlayer.getPlayerColor()) + ".", allowedCoordinates);
		} else {
			Utils.logError(getCardName() + " has received an illegal choice: " + choice + " and the size of targettable players is: " + targettablePlayers.size(), new IllegalArgumentException());
			concludeActivation();
			return null;
		}
	}

	private void lastStep(int choice) {
		if (choice >= 0 && choice < allowedCoordinates.size()) {
			getGameBoard().getGameMap().movePlayerTo(targetPlayer, allowedCoordinates.get(choice));
		} else {
			Utils.logError(getCardName() + " has received an illegal choice: " + choice + " and the size of targettable players is: " + targettablePlayers.size(), new IllegalArgumentException());
		}
		concludeActivation();
	}

	private boolean canActivateOnPlayer(Player player) {
		return player != getOwner() && !player.getPlayerBoard().isDead() && player.getTurnStatus() != TurnStatus.PRE_SPAWN;
	}

}