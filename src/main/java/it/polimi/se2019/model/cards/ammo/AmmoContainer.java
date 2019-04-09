package it.polimi.se2019.model.cards.ammo;

public class AmmoContainer {

	public static final int MAX_AMMO = 3;
	public static final int INITIAL_AMMO = 1;
	private int[] ammos;


	public AmmoContainer() {
		ammos = new int[AmmoType.values().length];
		for (AmmoType ammoType : AmmoType.values()) {
			ammos[ammoType.ordinal()] = INITIAL_AMMO;
		}
	}


	public int getAmmo(AmmoType ammoType) {
		return ammos[ammoType.ordinal()];
	}

	public void addAmmo(AmmoType ammoToAdd, int numOfAmmoToAdd) {
		if(numOfAmmoToAdd < 0)
			throw new IllegalArgumentException("numOfAmmoToAdd cannot be negative.");

		ammos[ammoToAdd.ordinal()] += numOfAmmoToAdd;

		if(ammos[ammoToAdd.ordinal()] > MAX_AMMO)
			ammos[ammoToAdd.ordinal()] = MAX_AMMO;
	}

	public void removeAmmo(AmmoType ammoToRemove, int numOfAmmoToRemove) {
		if(numOfAmmoToRemove < 0)
			throw new IllegalArgumentException("numOfAmmoToRemove cannot be negative.");

		ammos[ammoToRemove.ordinal()] -= numOfAmmoToRemove;

		if(ammos[ammoToRemove.ordinal()] < 0)
			ammos[ammoToRemove.ordinal()] = 0;
	}

}