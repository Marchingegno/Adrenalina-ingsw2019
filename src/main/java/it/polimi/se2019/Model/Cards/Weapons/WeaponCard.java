package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

import java.util.ArrayList;

public abstract class WeaponCard extends Card {

	private boolean loaded;
	private ArrayList<AmmoType> grabPrice;
	private ArrayList<AmmoType> reloadPrice;


	public abstract void shoot();

	protected abstract void primaryFire();

	public boolean isLoaded() {
		return loaded;
	}

	public void load() {
	}

	public void getAvailableOptions() {
	}

	public ArrayList<Player> getTargettablePlayers() {
		return null;
	}

}