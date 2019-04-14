package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.Card;

/**
 * Normal square associated with an ammo card
 */
public class AmmoSquare extends Square {

	public AmmoSquare(int roomID, boolean[] possibleDirections, Coordinates coordinates) {
		super(possibleDirections, roomID, coordinates);
	}

	@Override
	public Card grabCard() { return null;}

}