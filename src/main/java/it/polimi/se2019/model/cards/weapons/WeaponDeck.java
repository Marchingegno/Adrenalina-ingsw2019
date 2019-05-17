package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.Deck;

public class WeaponDeck extends Deck<Card> {

	public void initializeDeck(){
		for (int i = 0; i < 40; i++) {
			addCard(new Cyberblade("Hello boy, this cyberblade is very nice"));
		}
	}

}