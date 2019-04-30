package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.GameBoard;
import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoCard;
import it.polimi.se2019.utils.Color;

/**
 * Normal square associated with an ammo card
 */
public class AmmoSquare extends MapSquare {

	public AmmoSquare(int roomID, boolean[] possibleDirections, Coordinates coordinates, GameBoard gameBoard) {
		super(possibleDirections, roomID, coordinates);
		deck = gameBoard.getAmmoDeck();
		isChanged = true;
		setNotFilled();
	}

	public void refillCards() {
		if (!isFilled())
			cards.add(deck.drawCard());
	}

	public Card grabCard(int index) {
		if (cards == null)
			throw new NullPointerException("Ammo Square without ammo card");
		return cards.remove(index);
	}

	public MapSquareRep getRep() {
		if (isChanged || squareRep == null) {
			squareRep = new AmmoSquareRep(this);
		}
		return squareRep;
	}

	/**
	 * @return
	 * @deprecated
	 */
	public String[] getElementsToPrint() {
		String[] elementsToPrint = new String[3];
		AmmoCard ammoCard = (AmmoCard) cards.get(0);
		elementsToPrint[0] = Color.getColoredCell(ammoCard.getAmmos().get(0).getBackgroundColorType());
		elementsToPrint[1] = Color.getColoredCell(ammoCard.getAmmos().get(1).getBackgroundColorType());
		elementsToPrint[2] = Color.getColoredCell(ammoCard.hasPowerup() ? Color.BackgroundColorType.WHITE : ammoCard.getAmmos().get(2).getBackgroundColorType());
		return elementsToPrint;
	}
}