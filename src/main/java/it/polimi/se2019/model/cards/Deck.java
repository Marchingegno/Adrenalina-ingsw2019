package it.polimi.se2019.model.cards;

import it.polimi.se2019.utils.Utils;

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


	// ####################################
	// PUBLIC METHODS
	// ####################################

	/**
	 * Returns the first card of the deck.
	 *
	 * @return the first card of the deck.
	 */
	public C drawCard() {
		if (actualDeck.isEmpty())
			refillDeck();
		if (actualDeck.isEmpty())
			Utils.logError("There are no more cards in the deck", new NoSuchElementException());
		Utils.logInfo("Deck -> drawCard(): Drawing " + actualDeck.getFirst());
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
	 * Refills the deck by using the discard deck and then shuffles it.
	 */
	public void refillDeck() {
		Utils.logInfo("Deck -> refillDeck(): Refilling actualDeck[" + actualDeck.size() + "] with the discardDeck[" + discardDeck.size() + "]");
		actualDeck.addAll(discardDeck);
		discardDeck.clear();
		shuffleDeck();
	}

	/**
	 * Adds the card to the discard deck. The discard deck will be used when refilling the deck with the method refillDeck().
	 *
	 * @param cardToDiscard the card to discard.
	 */
	public void discardCard(C cardToDiscard) {
		Utils.logInfo("Deck -> discardCard(): Adding to the discardDeck " + cardToDiscard);
		discardDeck.push(cardToDiscard);
	}


	// ####################################
	// PROTECTED METHODS
	// ####################################

	/**
	 * Adds the specified card to the deck. This method is protected because only at initialization cards are added.
	 *
	 * @param cardToAdd the card to add to the deck.
	 */
	protected void addCard(C cardToAdd) {
		Utils.logInfo("Deck -> addCard(): Adding to actualDeck " + cardToAdd);
		actualDeck.push(cardToAdd);
	}


	// ####################################
	// ABSTRACT METHODS
	// ####################################

	/**
	 * Should initialize the deck with all the cards.
	 */
	protected abstract void initializeDeck();


	// ####################################
	// PRIVATE METHODS
	// ####################################

	/**
	 * Shuffles the deck.
	 */
	private void shuffleDeck() {
		ArrayList<C> list = new ArrayList<>(actualDeck); // Create shuffleable list.
		Collections.shuffle(list); // Shuffle list.
		actualDeck = new ArrayDeque<>(list); // Re-create deque from list.
		Utils.logInfo("Deck -> shuffleDeck(): Deck shuffled");
	}

}