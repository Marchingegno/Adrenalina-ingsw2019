package it.polimi.se2019.model.cards;

import it.polimi.se2019.model.gameboard.GameBoard;

public abstract class OwnableDeck<O extends OwnableCard> extends Deck<O> {

	private GameBoard gameBoard;


	public OwnableDeck(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}


	@Override
	public O drawCard() {
		O ownableCard = super.drawCard();
		ownableCard.setGameBoard(gameBoard);
		return ownableCard;
	}
}
