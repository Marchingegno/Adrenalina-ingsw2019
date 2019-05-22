package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.OwnableDeck;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gameboard.GameBoard;

import java.util.ArrayList;
import java.util.List;

public class WeaponDeck extends OwnableDeck<WeaponCard> {

	public WeaponDeck(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void initializeDeck(){
		String placeHolder = "placeHolder";

		for (int i = 0; i < 3; i++) {
			List<AmmoType> price = new ArrayList<>();
			price.add(AmmoType.BLUE_AMMO);
			addCard(new Elecroscythe(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.RED_AMMO);
			price.add(AmmoType.BLUE_AMMO);
			addCard(new Furnace(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.RED_AMMO);
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Hellion(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Shockwave(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.YELLOW_AMMO);
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Shotgun(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.BLUE_AMMO);
			addCard(new TractorBeam(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.BLUE_AMMO);
			price.add(AmmoType.BLUE_AMMO);
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Whisper(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.YELLOW_AMMO);
			price.add(AmmoType.RED_AMMO);
			addCard(new ZX_2(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Sledgehammer(placeHolder, price));
		}
	}

}