package it.polimi.se2019.Model.Cards;

import java.util.*;

/**
 *
 */
public abstract class Deck {

	/**
	 * Default constructor
	 */
    public Deck(){
	}

	/**
	 *
	 */
	private ArrayList<Card> discardDeck;

	/**
	 *
	 */
	private ArrayList<Card> actualDeck;

	/**
	 * @param cardToAdd
	 */
	public void addCard(Card cardToAdd) {
		// TODO implement here
	}

	/**
	 * @param cardToDiscard
	 */
	public void discardCard(Card cardToDiscard) {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public Card drawCard() {
		// TODO implement here
		return null;
	}

	/**
	 *
	 */
	public void refillActualDeck(){

	}

	/**
	 *
	 */
	public void shuffleDeck() {
		// TODO implement here
	}

}