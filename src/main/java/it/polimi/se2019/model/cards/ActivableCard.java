package it.polimi.se2019.model.cards;

import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.player.Player;

public abstract class ActivableCard extends Card {

	private Player ownerPlayer;
	private GameBoard gameBoard;


	public ActivableCard(String cardName, String description) {
		super(cardName, description);
	}


	// ####################################
	// PUBLIC METHODS
	// ####################################

	protected Player getOwner() {
		return ownerPlayer;
	}

	public void setGameBoard(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}


	// ####################################
	// PROTECTED METHODS
	// ####################################

	public void setOwner(Player player) {
		ownerPlayer = player;
	}

	protected GameBoard getGameBoard() {
		return gameBoard;
	}
}
