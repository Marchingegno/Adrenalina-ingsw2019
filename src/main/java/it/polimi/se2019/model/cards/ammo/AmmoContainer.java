package it.polimi.se2019.model.cards.ammo;

/**
 * Contains all the ammos of the player.
 * @author Desno365
 */
public class AmmoContainer {

	public static final int MAX_AMMO = 3;
	public static final int INITIAL_AMMO = 1;

	private int[] ammos;


	/**
	 * Create the container with initial values of ammos.
	 */
	public AmmoContainer() {
		ammos = new int[AmmoType.values().length];
		for (AmmoType ammoType : AmmoType.values()) {
			ammos[ammoType.ordinal()] = INITIAL_AMMO;
		}
	}


	/**
	 * Get the amount of ammos given the type of ammo.
	 * @param ammoType type of the ammo.
	 * @return number of ammos.
	 */
	public int getAmmo(AmmoType ammoType) {
		return ammos[ammoType.ordinal()];
	}

	/**
	 * Add ammos per ammo type.
	 * @param ammoToAdd type of ammo that is added.
	 * @param numOfAmmoToAdd number of ammo added, must be >= 0.
	 */
	public void addAmmo(AmmoType ammoToAdd, int numOfAmmoToAdd) {
		if(numOfAmmoToAdd < 0)
			throw new IllegalArgumentException("numOfAmmoToAdd cannot be negative.");

		ammos[ammoToAdd.ordinal()] += numOfAmmoToAdd;

		if(ammos[ammoToAdd.ordinal()] > MAX_AMMO)
			ammos[ammoToAdd.ordinal()] = MAX_AMMO;
	}

	/**
	 * Add a single ammo of ammo type.
	 * @param ammoToAdd type of ammo that is added.
	 */
	public void addAmmo(AmmoType ammoToAdd) {
		addAmmo(ammoToAdd, 1);
	}

	/**
	 * Remove ammos per ammo type.
	 * @param ammoToRemove type of ammo that is removed.
	 * @param numOfAmmoToRemove number of ammo removed, must be >= 0.
	 */
	public void removeAmmo(AmmoType ammoToRemove, int numOfAmmoToRemove) {
		if(numOfAmmoToRemove < 0)
			throw new IllegalArgumentException("numOfAmmoToRemove cannot be negative.");

		ammos[ammoToRemove.ordinal()] -= numOfAmmoToRemove;

		if(ammos[ammoToRemove.ordinal()] < 0)
			ammos[ammoToRemove.ordinal()] = 0;
	}

}