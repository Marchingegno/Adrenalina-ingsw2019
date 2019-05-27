package it.polimi.se2019.model.cards;

import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

public abstract class ActivableCard extends Card {

	private Player ownerPlayer;
	private GameBoard gameBoard;
	private int currentStep = 0;


	public ActivableCard(String cardName, String description) {
		super(cardName, description);
	}


	// ####################################
	// ABSTRACT METHODS
	// ####################################

	/**
	 * Returns true if this card can be activated in the current conditions.
	 * @return true if this card can be activated in the current conditions.
	 */
	public abstract boolean canBeActivated();


	/**
	 * Creates a {@link QuestionContainer} to be passed to the view.
	 * @return the {@link QuestionContainer}.
	 */
	public abstract QuestionContainer initialQuestion();


	/**
	 * Handles the activation process of the card based on the currentStep and the choice of the player.
	 * @return the {@link QuestionContainer} to be passed to the view, or **null** if the process has finished.
	 * @param choice the choice of the player.
	 */
	public abstract QuestionContainer doActivationStep(int choice);


	// ####################################
	// PUBLIC METHODS
	// ####################################

	public void setOwner(Player player) {
		ownerPlayer = player;
	}

	public void setGameBoard(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}


	// ####################################
	// PROTECTED METHODS (only for subclasses)
	// ####################################

	protected Player getOwner() {
//		Utils.logInfo("The weapon " + super.getCardName() + "has owner +" + ownerPlayer.getPlayerName());
		return ownerPlayer;
	}

	protected GameBoard getGameBoard() {
		return gameBoard;
	}

	protected int getCurrentStep() {
		return currentStep;
	}

	protected void incrementCurrentStep(){
		currentStep++;
	}

	protected void resetCurrentStep(){
		currentStep = 0;
	}
}
