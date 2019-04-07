package it.polimi.se2019.model.player;

import it.polimi.se2019.model.cards.ammo.AmmoContainer;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.model.cards.weapons.WeaponCard;

import java.util.ArrayList;

public class PlayerBoard {

	private ArrayList<Player> damage;
	private ArrayList<Player> marks;
	private int numberOfDeaths;
	private int points;
	private AmmoContainer ammoContainer;
	private PowerupCard powerupCards;
	private WeaponCard weapons;


	public PlayerBoard() {
	}


	public void addDamage(Player shootingPlayer, int amountOfDamage) {
	}

	public void addMarks(Player shootingPlayer, int amountOfMarks) {
	}

	private void resetDamage() {
	}

	public boolean isDead() {
		return false;
	}

	public void addPoints(int pointsToAdd) {
	}

	public void score() {
	}

	public int getNumOfDeaths() {
		return 0;
	}

	public void addWeapon(WeaponCard weaponToAdd) {
	}

	public void addPowerup(PowerupCard powerupToAdd) {
	}

	public void swapWeapon(WeaponCard weaponToGrab, WeaponCard weaponToDrop) {
	}

	public void removePowerup(PowerupCard powerupCardToRemove) {
	}

}