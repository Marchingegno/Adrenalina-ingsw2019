package it.polimi.se2019.model.cards.ammo;

import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

import java.util.List;

/**
 * Contains all the ammo of the player.
 *
 * @author Desno365
 */
public class AmmoContainer {

	private int[] ammo;
	private boolean hasChanged;

	/**
	 * Create the container with initial values of ammo.
	 */
	public AmmoContainer() {
		ammo = new int[AmmoType.values().length];
		for (AmmoType ammoType : AmmoType.values()) {
			ammo[ammoType.ordinal()] = GameConstants.INITIAL_AMMO_PER_AMMO_TYPE;
		}
		setChanged();
	}

	public boolean hasEnoughAmmo(List<AmmoType> ammoToCheck){
		int[] convertedAmmosToCheck = convertAmmoListToArray(ammoToCheck);
		for (int i = 0; i < AmmoType.values().length; i++) {
			if(convertedAmmosToCheck[i] > ammo[i]){
				//Ammo requested is greater than ammo owned.
				return false;
			}
		}
		return true;
	}


	/**
	 * Get the amount of ammo given the type of ammo.
	 *
	 * @param ammoType type of the ammo.
	 * @return number of ammo.
	 */
	public int getAmmo(AmmoType ammoType) {
		return ammo[ammoType.ordinal()];
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

		ammo[ammoToAdd.ordinal()] += numOfAmmoToAdd;

		if (ammo[ammoToAdd.ordinal()] > GameConstants.MAX_AMMO_PER_AMMO_TYPE)
			ammo[ammoToAdd.ordinal()] = GameConstants.MAX_AMMO_PER_AMMO_TYPE;
		setChanged();
		Utils.logInfo("AmmoContainer -> addAmmo(): Added " + numOfAmmoToAdd + " " + ammoToAdd.toString());
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
	 * Remove ammo per ammo type.
	 *
	 * @param ammoToRemove      type of ammo to remove.
	 * @param numOfAmmoToRemove number of ammo remove, must be >= 0.
	 * @throws IllegalArgumentException if the amount of ammo to add is negative.
	 */
	public void removeAmmo(AmmoType ammoToRemove, int numOfAmmoToRemove) {
		if (numOfAmmoToRemove < 0)
			throw new IllegalArgumentException("numOfAmmoToRemove cannot be negative.");

		if (numOfAmmoToRemove > ammo[ammoToRemove.ordinal()])
			throw new IllegalArgumentException("Trying to remove more ammo than the player has.");

		ammo[ammoToRemove.ordinal()] -= numOfAmmoToRemove;
		setChanged();
		Utils.logInfo("AmmoContainer -> removeAmmo(): Removed " + numOfAmmoToRemove + " " + ammoToRemove.toString());
	}

	/**
	 * Remove a single ammo of ammo type.
	 *
	 * @param ammoToRemove type of ammo to remove.
	 */
	public void removeAmmo(AmmoType ammoToRemove) {
		removeAmmo(ammoToRemove, 1);
	}


	/**
	 * Sets the square as changed.
	 */
	public void setChanged() {
		hasChanged = true;
	}

	/**
	 * Sets the square as not changed.
	 */
	public void setNotChanged() {
		hasChanged = false;
	}

	/**
	 * Returns true if and only if the player board has changed.
	 *
	 * @return true if and only if the player board has changed.
	 */
	public boolean hasChanged() {
		return hasChanged;
	}

	private int[] convertAmmoListToArray(List<AmmoType> listToConvert){
		int[] convertedAmmos = new int[AmmoType.values().length];
		for (AmmoType ammoType : AmmoType.values()) {
			ammo[ammoType.ordinal()] = 0;
		}
		listToConvert.forEach(item -> convertedAmmos[item.ordinal()]++);
		return convertedAmmos;
	}

}