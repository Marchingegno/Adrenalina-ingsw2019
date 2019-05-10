package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.List;
import static java.lang.Boolean.FALSE;

/**
 * Weapons with an alternate fire method. The player can choose one of the two firing modes the weapon has.
 * @author Marchingegno
 */
public abstract class AlternateFire extends WeaponCard {
	int alternateSteps;
	List<DamageAndMarks> secondaryDamagesAndMarks;
	Boolean alternateFireActive;


	public AlternateFire(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		reset();

	}
	@Override
	public void registerChoice(int choice) {
		if (choice == 2)
			alternateFireActive = true;
	}

	@Override
	public Pair handleFire() {
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



	void secondaryReset(){
		this.alternateFireActive = FALSE;
	}

	@Override
	void reset() {
		super.reset();
		secondaryReset();
	}
}