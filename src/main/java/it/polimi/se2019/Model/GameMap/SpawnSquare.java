package it.polimi.se2019.Model.GameMap;

import it.polimi.se2019.Model.Cards.Ammo.AmmoType;
import it.polimi.se2019.Model.Cards.Card;
import it.polimi.se2019.Model.Cards.Weapons.WeaponCard;

import java.util.*;

/**
 *
 */
public class SpawnSquare extends Square {

	/**
	 * Default constructor
	 */
	public SpawnSquare() {
	}

	/**
	 *
	 */
	private boolean[] possibleDirections;

	/**
	 *
	 */
	private int roomID;

	/**
	 *
	 */
	private AmmoType color;

	/**
	 *
	 */
	private WeaponCard weaponCard;

	/**
	 * @param card1
	 * @param card2
	 * @param card3
	 */
	public void SpawnSquare(Card card1, Card card2, Card card3) {
		// TODO implement here
	}

	/**
	 * @param cardToAdd
	 */
	public void addCard(Card cardToAdd) {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public Card grabCard() {
		// TODO implement here
		return null;
	}

	/**
	 * @return
	 */
	public AmmoType getAmmoType() {
		// TODO implement here
		return null;
	}

	/**
	 * @return
	 */
	public ArrayList<Card> listCards() {
		// TODO implement here
		return null;
	}

}