package it.polimi.se2019.model.cards.ammo;

import it.polimi.se2019.model.cards.Card;

import java.util.ArrayList;

public class AmmoCard extends Card {

	private ArrayList<AmmoType> ammos;
	private boolean hasPowerup;


	public AmmoCard(AmmoType ammo1, AmmoType ammo2, AmmoType ammo3, String description) {
		super(description);
	}

	public AmmoCard(AmmoType ammo1, AmmoType ammo2, String description) {
		super(description);
	}


	public boolean hasPowerup() {
		return false;
	}

	public ArrayList<AmmoType> getAmmos() {
		return null;
	}

}