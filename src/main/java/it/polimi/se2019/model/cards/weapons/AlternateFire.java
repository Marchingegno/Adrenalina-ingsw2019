package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import java.util.List;
import static java.lang.Boolean.FALSE;

/**
 * Weapons with an alternate fire method. The player can choose one of the two firing modes the weapon has.
 * @author Marchingegno
 */
public abstract class AlternateFire extends WeaponCard {
	protected List<DamageAndMarks> secondaryDamagesAndMarks;

	public AlternateFire(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
	}


	/**
	 * Handling of flags: if the first flag is TRUE then the player chose secondary fire mode.
	 * @param flags which options the player has chosen.
	 */
	@Override
	protected void handleFire(Boolean[] flags) {
		List<Player> targetPlayers;
		List<DamageAndMarks> damageAndMarks;

		if(flags[0] == FALSE){
			targetPlayers = primaryFire();
			damageAndMarks = standardDamagesAndMarks;
		}
		else {
			targetPlayers = secondaryFire();
			damageAndMarks = secondaryDamagesAndMarks;
		}

		dealDamage(targetPlayers, damageAndMarks);
		reset();
	}

	/**
	 * Secondary mode of firing.
	 */
	public abstract List<Player> secondaryFire();

	/**
	 * Get the targets of the secondary mode of fire for this weapon.
	 * @return the targettable players.
	 */
	public abstract List<Player> getSecondaryTargets();



}