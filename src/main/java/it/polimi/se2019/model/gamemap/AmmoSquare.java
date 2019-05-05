package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.Utils;

/**
 * Normal square associated with an ammo card
 *
 * @author MarcerAndrea
 */
public class AmmoSquare extends Square {

	public AmmoSquare(Color.CharacterColorType squareColor, boolean[] possibleDirections, Coordinates coordinates, GameBoard gameBoard) {
		super(possibleDirections, squareColor, coordinates);
		deck = gameBoard.getAmmoDeck();
		hasChanged = true;
		setNotFilled();
	}

	/**
	 * Refills the card slot with an ammo card.
	 */
	public void refillCards() {
		if (!isFilled())
			cards.add(deck.drawCard());
		setFilled();
		setChanged();
		Utils.logInfo("AmmoSquare -> refillCards(): The spawn square has been refilled");
	}

	/**
	 * Removes the ammo card from the square and returns it.
	 * @param index index of the card to grab.
	 * @return the ammo card in the square
	 */
	public Card grabCard(int index) {
		if (cards == null)
			throw new NullPointerException("Ammo Square without ammo card");
		if (index != 0)
			throw new IllegalArgumentException("This is an ammo square, index can be only 0 and you are asking " + index);
		setNotFilled();
		setChanged();
		return cards.remove(index);
	}

	/**
	 * Returns the ammo square's representation.
	 *
	 * @return the ammo square's representation.
	 */
	public SquareRep getRep() {
		if (hasChanged || squareRep == null) {
			squareRep = new AmmoSquareRep(this);
			setNotChanged();
			Utils.logInfo("AmmoSquare -> getRep(): Updated the square's representation");
		}
		return squareRep;
	}
}