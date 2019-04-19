package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import java.util.List;


/**
 * Weapons with one or more optional effect. An optional effect is an enhancement the player can choose to give at the
 * primary fire mode. Some optional effect have an additional ammo cost.
 * @author  Marchingegno
 */
public abstract class OptionalEffect extends WeaponCard {

	public OptionalEffect(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
	}


	public abstract void optionalEffect1();

	public abstract void optionalEffect2();


}