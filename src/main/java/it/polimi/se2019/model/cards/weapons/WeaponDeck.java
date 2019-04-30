package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.Deck;

public class WeaponDeck extends Deck<Card> {

	//TODO implement correctly
	protected void initializeDeck(){
		addCard(new Cyberblade("des"));
		addCard(new Elecroscythe("des"));
		addCard(new FlameThrower("des"));
		addCard(new Furnace("des"));
		addCard(new GrenadeLauncher("des"));
		addCard(new Railgun("des"));
		addCard(new Cyberblade("des"));
		addCard(new Shockwave("des"));
		addCard(new Shotgun("des"));
		addCard(new PlasmaGun("des"));
		addCard(new Thor("des"));
		addCard(new TractorBeam("des"));
		addCard(new Sledgehammer("des"));
	}

}