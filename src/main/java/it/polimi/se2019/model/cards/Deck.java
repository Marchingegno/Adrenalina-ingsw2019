package it.polimi.se2019.model.cards;

import java.util.*;

/**
 * Represents the abstract concept of a deck.
 *
 * @param <C> type of the elements of the deck, must be a Card.
 * @author Desno365
 */
public abstract class Deck<C extends Card> {

	private Deque<C> discardDeck;
	private Deque<C> actualDeck;


	/**
	 * Create an instance of a deck. The deck is automatically initialized using the method initializeDeck().
	 */
	public Deck() {
		discardDeck = new ArrayDeque<>();
		actualDeck = new ArrayDeque<>();
		initializeDeck();
		shuffleDeck();
	}


	/**
	 * Returns the first card on the deck.
	 *
	 * @return the first card on the deck.
	 */
	public C drawCard() {
		if (actualDeck.isEmpty())
			throw new NoSuchElementException("Cannot draw a card when the deck is empty.");
		return actualDeck.removeFirst();
	}

	/**
	 * Returns true if the deck doesn't contain any card.
	 *
	 * @return true if the deck doesn't contain any card.
	 */
	public boolean isEmpty() {
		return actualDeck.isEmpty();
	}

	/**
	 * Refill the deck by using the discard deck and then shuffles it.
	 */
	public void refillDeck() {
		actualDeck.addAll(discardDeck);
		discardDeck.clear();
		shuffleDeck();
	}

	/**
	 * Add the card to the discard deck. The discard deck will be used when refilling the deck with the method refillDeck().
	 *
	 * @param cardToDiscard the card to discard.
	 */
	public void discardCard(C cardToDiscard) {
		discardDeck.push(cardToDiscard);
	}

	/**
	 * Shuffle the deck.
	 */
	private void shuffleDeck() {
		ArrayList<C> list = new ArrayList<>(actualDeck); // Create shuffleable list.
		Collections.shuffle(list); // Shuffle list.
		actualDeck = new ArrayDeque<>(list); // Re-create deque from list.
	}

	/**
	 * Add the specified card to the deck. This method is protected because only at initialization cards are added.
	 *
	 * @param cardToAdd the card to add to the deck.
	 */
	protected void addCard(C cardToAdd) {
		actualDeck.push(cardToAdd);
	}

	/**
	 * Should initialize the deck with all the cards.
	 */
	protected abstract void initializeDeck();

}