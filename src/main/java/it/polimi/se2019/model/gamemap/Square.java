package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.Card;
import java.util.ArrayList;

/**
 * Class that represent a square in the map
 * @author MarcerAndrea
 */
public abstract class Square {

	private boolean[] possibleDirections;
	private int roomID;
	private ArrayList<Card> cards = new ArrayList<>();

	public Square(boolean[] possibleDirections, int roomID) {
		this.possibleDirections = possibleDirections;
		this.roomID = roomID;
	}

	/**
	 * Adds to the square a card
	 * @param cardToAdd card to add to the square
	 */
	public void addCard(Card cardToAdd){cards.add(cardToAdd);}

	/**
	 * Removes and returns a card from the square
	 * @return the removed card
	 */
	public abstract Card grabCard();

	/**
	 * @return the list of the cards in the square
	 */
	public ArrayList<Card> getCardList(){return (ArrayList<Card>) cards.clone();}

}