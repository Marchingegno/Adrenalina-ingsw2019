package it.polimi.se2019.model.cards;

import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.player.Player;

public abstract class ActivableCard extends Card {

	private Player ownerPlayer;
	private GameBoard gameBoard;
	private int currentStep = 0;


	public ActivableCard(String cardName, String description) {
		super(cardName, description);
	}


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
