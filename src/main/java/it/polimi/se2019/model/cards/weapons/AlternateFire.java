package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.util.List;

import static java.lang.Boolean.TRUE;

/**
 * Weapons with an alternate fire method. The player can choose one of the two firing modes the weapon has.
 * @author Marchingegno
 */
public abstract class AlternateFire extends WeaponCard {

	public AlternateFire(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
	}


	/**
	 * Handling of flags: if the first flag is TRUE then the player chose secondary fire mode.
	 * @param flags which options the player has chosen.
	 */
	@Override
	protected void handleFire(Boolean[] flags) {
		if(flags[0] == TRUE){
			secondaryFire();
		}
		else primaryFire();
	}

	/**
	 * Secondary mode of firing.
	 */
	public abstract void secondaryFire();



}