package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.util.ArrayList;
import java.util.List;

public abstract class OptionalEffect extends WeaponCard {

	public OptionalEffect(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
	}




	public void chooseTarget(){

	}

	public abstract void optionalEffect1();

	public abstract void optionalEffect2();


}