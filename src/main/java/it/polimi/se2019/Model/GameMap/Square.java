package it.polimi.se2019.Model.GameMap;

import it.polimi.se2019.Model.Cards.Card;

import java.util.*;

/**
 *
 */
public abstract class Square {

	/**
	 * Default constructor
	 */
	public Square() {
	}

	/**
	 *
	 */
	private boolean[] possibleDirections;

	/**
	 *
	 */
	private int roomID;

	/**
	 * @param cardToAdd
	 */
	public abstract void addCard(Card cardToAdd);

	/**
	 * @return
	 */
	public abstract Card grabCard();

	/**
	 * @return
	 */
	public abstract ArrayList<Card> listCards();

}