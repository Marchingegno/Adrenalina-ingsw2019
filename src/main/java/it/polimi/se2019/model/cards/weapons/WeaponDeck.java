package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.Deck;
import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.util.ArrayList;
import java.util.List;

public class WeaponDeck extends Deck<Card> {

	public void initializeDeck(){

		for (int i = 0; i < 3; i++) {
			List<AmmoType> price = new ArrayList<>();
			price.add(AmmoType.BLUE_AMMO);
			addCard(new Elecroscythe("placeHolder", price));

			price = new ArrayList<>();
			price.add(AmmoType.RED_AMMO);
			price.add(AmmoType.BLUE_AMMO);
			addCard(new Furnace("placeHolder", price));

			price = new ArrayList<>();
			price.add(AmmoType.RED_AMMO);
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Hellion("placeHolder", price));

			price = new ArrayList<>();
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Shockwave("placeHolder", price));

			price = new ArrayList<>();
			price.add(AmmoType.YELLOW_AMMO);
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Shotgun("placeHolder", price));

			price = new ArrayList<>();
			price.add(AmmoType.BLUE_AMMO);
			addCard(new TractorBeam("placeHolder", price));

			price = new ArrayList<>();
			price.add(AmmoType.BLUE_AMMO);
			price.add(AmmoType.BLUE_AMMO);
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Whisper("placeHolder", price));

			price = new ArrayList<>();
			price.add(AmmoType.YELLOW_AMMO);
			price.add(AmmoType.RED_AMMO);
			addCard(new ZX_2("placeHolder", price));
		}
	}

}