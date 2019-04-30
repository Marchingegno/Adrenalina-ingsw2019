package it.polimi.se2019.model.cards;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Desno365
 */
public class DeckTest {

	private static final ArrayList<PowerupCard> powerupCardsForTest = new ArrayList<>();

	private DeckTestConcrete deck;

	@BeforeClass
	public static void oneTimeSetUp() {
		powerupCardsForTest.add(new Newton(AmmoType.RED_AMMO));
		powerupCardsForTest.add(new TagbackGrenade(AmmoType.YELLOW_AMMO));
		powerupCardsForTest.add(new TargetingScope(AmmoType.BLUE_AMMO));
		powerupCardsForTest.add(new Teleporter(AmmoType.RED_AMMO));
	}

	@Before
	public void setUp() throws Exception {
		deck = new DeckTestConcrete();
	}

	@Test
	public void drawCard_initialState_correctOuput() {
		// Check if all the cards are contained.
		for (int i = 0; i < powerupCardsForTest.size(); i++)
			assertTrue(powerupCardsForTest.indexOf(deck.drawCard()) != -1);
	}

	@Test (expected =  NoSuchElementException.class)
	public void drawCard_moreRequestsThanCard_shouldThrowException() {
		for (int i = 0; i < powerupCardsForTest.size() + 1; i++)
			deck.drawCard();
	}

	@Test
	public void isEmpty_correctInput_correctOutput() {
		assertFalse(deck.isEmpty());
		for (int i = 0; i < powerupCardsForTest.size(); i++)
			deck.drawCard();
		assertTrue(deck.isEmpty());
	}

	@Test
	public void refillDeck_correctInput_correctOuput() {
		// Discard all cards.
		for (int i = 0; i < powerupCardsForTest.size(); i++)
			deck.discardCard(deck.drawCard());

		deck.refillDeck();

		// Check if all the cards are contained.
		for (int i = 0; i < powerupCardsForTest.size(); i++)
			assertTrue(powerupCardsForTest.indexOf(deck.drawCard()) != -1);
	}


	private class DeckTestConcrete extends Deck<PowerupCard> {
		@Override
		protected void initializeDeck() {
			// Add every card of the powerupCardsForTest list.
			for(PowerupCard card : DeckTest.powerupCardsForTest)
				addCard(card);
		}
	}

}