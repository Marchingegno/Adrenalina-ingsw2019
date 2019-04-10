package it.polimi.se2019.model.cards;

import java.util.ArrayList;

public abstract class Deck<E> {

	private ArrayList<E> discardDeck;
	private ArrayList<E> actualDeck;


	public E drawCard() {
		return null;
	}

	public void addCard(E cardToAdd) {
	}

	public void refillActualDeck() {
	}

	public void discardCard(E cardToDiscard) {
	}

	public void shuffleDeck() {
	}

	abstract public void initializeDeck();

}