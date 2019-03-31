package it.polimi.se2019.Model.Cards.Ammo;

import it.polimi.se2019.Model.Cards.Card;

import java.util.*;

/**
 *
 */
public class AmmoCard extends Card {

	/**
	 * Default constructor
	 */
	public AmmoCard() {
	}

	/**
	 *
	 */
	private ArrayList<AmmoType> ammos;

	/**
	 *
	 */
	private boolean hasPowerup;


	/**
	 * @param ammo1
	 * @param ammo2
	 * @param ammo3
	 * @param description
	 */
	public void AmmoCard(AmmoType ammo1, AmmoType ammo2, AmmoType ammo3, String description) {
		// TODO implement here
	}

	/**
	 * @param ammo1
	 * @param ammo2
	 * @param description
	 */
	public void AmmoCard(AmmoType ammo1, AmmoType ammo2, String description) {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public boolean hasPowerup() {
		// TODO implement here
		return false;
	}

	/**
	 * @return
	 */
	public ArrayList<AmmoType> getAmmos() {
		// TODO implement here
		return null;
	}

}