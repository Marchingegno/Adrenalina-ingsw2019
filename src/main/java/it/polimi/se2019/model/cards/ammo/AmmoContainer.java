package it.polimi.se2019.model.cards.ammo;

import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

/**
 * Contains all the ammo of the player.
 *
 * @author Desno365
 */
public class AmmoContainer {

	private int[] ammos;

	/**
	 * Create the container with initial values of ammos.
	 */
	public AmmoContainer() {
		ammos = new int[AmmoType.values().length];
		for (AmmoType ammoType : AmmoType.values()) {
			ammos[ammoType.ordinal()] = GameConstants.INITIAL_AMMO_PER_AMMO_TYPE;
		}
	}


	/**
	 * Get the amount of ammo given the type of ammo.
	 *
	 * @param ammoType type of the ammo.
	 * @return number of ammo.
	 */
	public int getAmmo(AmmoType ammoType) {
		return ammos[ammoType.ordinal()];
	}

	/**
	 * Add the specified number of ammo of the specified type.
	 *
	 * @param ammoToAdd      type of ammo to add.
	 * @param numOfAmmoToAdd number of ammo to add, must be >= 0.
	 * @throws IllegalArgumentException if the amount of ammo to add is negative.
	 */
	public void addAmmo(AmmoType ammoToAdd, int numOfAmmoToAdd) {
		if (numOfAmmoToAdd < 0)
			throw new IllegalArgumentException("numOfAmmoToAdd cannot be negative.");

		ammos[ammoToAdd.ordinal()] += numOfAmmoToAdd;

		if (ammos[ammoToAdd.ordinal()] > GameConstants.MAX_AMMO_PER_AMMO_TYPE)
			ammos[ammoToAdd.ordinal()] = GameConstants.MAX_AMMO_PER_AMMO_TYPE;
		Utils.logInfo("AmmoContainer -> addAmmo(): Added" + numOfAmmoToAdd + " " + ammoToAdd.toString());
	}

	/**
	 * Add a single ammo of ammo type.
	 *
	 * @param ammoToAdd type of ammo to add.
	 */
	public void addAmmo(AmmoType ammoToAdd) {
		addAmmo(ammoToAdd, 1);
	}

	/**
	 * Remove ammos per ammo type.
	 *
	 * @param ammoToRemove      type of ammo to remove.
	 * @param numOfAmmoToRemove number of ammo remove, must be >= 0.
	 * @throws IllegalArgumentException if the amount of ammo to add is negative.
	 */
	public void removeAmmo(AmmoType ammoToRemove, int numOfAmmoToRemove) {
		if (numOfAmmoToRemove < 0)
			throw new IllegalArgumentException("numOfAmmoToRemove cannot be negative.");

		if (numOfAmmoToRemove > ammos[ammoToRemove.ordinal()])
			throw new IllegalArgumentException("Trying to remove more ammo than the player has.");

		ammos[ammoToRemove.ordinal()] -= numOfAmmoToRemove;

		Utils.logInfo("AmmoContainer -> removeAmmo(): Removed" + numOfAmmoToRemove + " " + ammoToRemove.toString());
	}

	/**
	 * Remove a single ammo of ammo type.
	 *
	 * @param ammoToRemove type of ammo to remove.
	 */
	public void removeAmmo(AmmoType ammoToRemove) {
		removeAmmo(ammoToRemove, 1);
	}

}