package it.polimi.se2019.model.cards;

import java.util.*;

/**
 * Represents the abstract concept of a deck.
 * @param <C> type of the elements of the deck.
 */
public abstract class Deck<C extends Card> {

	private Deque<C> discardDeck;
	private Deque<C> actualDeck;


	public Deck() {
		discardDeck = new ArrayDeque<>();
		actualDeck = new ArrayDeque<>();
		initializeDeck();
	}


	public C drawCard() {
		if(actualDeck.isEmpty())
			throw new NoSuchElementException("Cannot draw a card when the deck is empty.");
		return actualDeck.pop();
	}

	public boolean isEmpty() {
		return actualDeck.isEmpty();
	}

	public void refillActualDeck() {
		actualDeck.addAll(discardDeck);
		discardDeck.clear();
		shuffleDeck();
	}

	public void discardCard(C cardToDiscard) {
		discardDeck.push(cardToDiscard);
	}

	public void shuffleDeck() {
		ArrayList<C> list = new ArrayList<>(actualDeck); // Create shuffleable list.
		Collections.shuffle(list); // Shuffle list.
		actualDeck = new ArrayDeque<>(list); // Re-create deque from list.
	}

	/**
	 * Add the specified card to the Deck. This method is protected because only at initialization cards are added.
	 * @param cardToAdd the card to add to the Deck.
	 */
	protected void addCard(C cardToAdd) {
		actualDeck.push(cardToAdd);
	}

	protected abstract void initializeDeck();

}