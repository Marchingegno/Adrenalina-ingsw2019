package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.util.List;

public abstract class AlternateFire extends WeaponCard {

	public AlternateFire(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
	}


	public abstract void secondaryFire();



}