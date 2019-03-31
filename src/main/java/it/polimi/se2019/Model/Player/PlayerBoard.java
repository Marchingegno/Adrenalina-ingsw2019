package it.polimi.se2019.Model.Player;

import it.polimi.se2019.Model.Cards.Ammo.AmmoContainer;
import it.polimi.se2019.Model.Cards.Powerups.PowerupCard;
import it.polimi.se2019.Model.Cards.Weapons.WeaponCard;

import java.util.*;

/**
 *
 */
public class PlayerBoard {

	/**
	 * Default constructor
	 */
	public PlayerBoard() {
	}

	/**
	 *
	 */
	private ArrayList<Player> damage;

	/**
	 *
	 */
	private ArrayList<Player> marks;

	/**
	 *
	 */
	private int numberOfDeaths;

	/**
	 *
	 */
	private int points;

	/**
	 *
	 */
	private AmmoContainer ammoContainer;

	/**
	 *
	 */
	private ArrayList<PowerupCard> powerupCards;

	/**
	 *
	 */
	private ArrayList<WeaponCard> weapons;

	/**
	 *
	 */
	public void PlayerBoard() {
		// TODO implement here
	}

	/**
	 * @param shootingPlayerID
	 * @param amountOfDamage
	 */
	public void addDamage(Player shootingPlayer, int amountOfDamage) {
		// TODO implement here
	}

	/**
	 * @param shootingPlayerID
	 * @param amountOfMarks
	 */
	public void addMarks(Player shootingPlayer, int amountOfMarks) {
		// TODO implement here
	}

	/**
	 *
	 */
	private void resetDamage() {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public boolean isDead() {
		// TODO implement here
		return false;
	}

	/**
	 * @param pointsToAdd
	 */
	public void addPoints(int pointsToAdd) {
		// TODO implement here
	}

	/**
	 *
	 */
	public void score() {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public int getNumOfDeaths() {
		// TODO implement here
		return 0;
	}

	/**
	 * @param weaponToAdd
	 */
	public void addWeapon(WeaponCard weaponToAdd) {
		// TODO implement here
	}

	/**
	 * @param powerupToAdd
	 */
	public void addPowerup(PowerupCard powerupToAdd) {
		// TODO implement here
	}

	/**
	 * @param weaponToGrab
	 * @param weaponToDrop
	 */
	public void swapWeapon(WeaponCard weaponToGrab, WeaponCard weaponToDrop) {
		// TODO implement here
	}

	/**
	 * @param powerupCardToRemove
	 */
	public void removePowerup(PowerupCard powerupCardToRemove) {
		// TODO implement here
	}

}