package it.polimi.se2019.Model.Cards.Weapons;

import it.polimi.se2019.Model.Cards.Ammo.AmmoType;
import it.polimi.se2019.Model.Cards.Card;
import it.polimi.se2019.Model.Player.Player;

import java.util.*;

/**
 * This class implements Strategy Pattern
 */
public abstract class WeaponCard extends Card {

	/**
	 * Default constructor
	 */
	public WeaponCard() {
	}

	/**
	 *
	 */
	private boolean loaded;

	/**
	 *
	 */
	private ArrayList<AmmoType> grabPrice;

	/**
	 *
	 */
	private ArrayList<AmmoType> reloadPrice;


	/**
	 *
	 */
	public abstract void shoot();

	/**
	 *
	 */
	protected abstract void primaryFire();

	/**
	 * @return
	 */
	public boolean isLoaded() {
		// TODO implement here
		return false;
	}

	/**
	 *
	 */
	public void load() {
		// TODO implement here
	}

	/**
	 *
	 */
	public void getAvailableOptions() {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public ArrayList<Player> getTargettablePlayers() {
		// TODO implement here
		return null;
	}

}