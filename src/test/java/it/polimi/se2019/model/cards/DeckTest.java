package it.polimi.se2019.model.cards;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class DeckTest {

	public static ArrayList<PowerupCard> powerupCardsForTest;

	@Before
	public void setUp() throws Exception {
		powerupCardsForTest = new ArrayList<>();
		powerupCardsForTest.add(new Newton(AmmoType.RED_AMMO));
		powerupCardsForTest.add(new TagbackGrenade(AmmoType.YELLOW_AMMO));
		powerupCardsForTest.add(new TargetingScope(AmmoType.BLUE_AMMO));
		powerupCardsForTest.add(new Teleporter(AmmoType.RED_AMMO));
	}

	@Test
	public void drawCard_initialState_correctOuput() {
		DeckTestConcrete deck = new DeckTestConcrete();

		// Check if all the cards are contained.
		for (int i = 0; i < powerupCardsForTest.size(); i++)
			assertTrue(powerupCardsForTest.indexOf(deck.drawCard()) != -1);
	}

	@Test (expected =  NoSuchElementException.class)
	public void drawCard_moreRequestsThanCard_shouldThrowException() {
		DeckTestConcrete deck = new DeckTestConcrete();
		for (int i = 0; i < powerupCardsForTest.size() + 1; i++)
			deck.drawCard();
	}

	@Test
	public void isEmpty_correctInput_correctOutput() {
		DeckTestConcrete deck = new DeckTestConcrete();
		assertFalse(deck.isEmpty());
		for (int i = 0; i < powerupCardsForTest.size(); i++)
			deck.drawCard();
		assertTrue(deck.isEmpty());
	}

	@Test
	public void refillDeck_correctInput_correctOuput() {
		DeckTestConcrete deck = new DeckTestConcrete();

		// Discard all cards.
		for (int i = 0; i < powerupCardsForTest.size(); i++)
			deck.discardCard(deck.drawCard());

		deck.refillDeck();

		// Check if all the cards are contained.
		for (int i = 0; i < powerupCardsForTest.size(); i++)
			assertTrue(powerupCardsForTest.indexOf(deck.drawCard()) != -1);
	}
}

class DeckTestConcrete extends Deck<PowerupCard> {

	@Override
	protected void initializeDeck() {
		for(PowerupCard card : DeckTest.powerupCardsForTest)
			addCard(card);
	}
}