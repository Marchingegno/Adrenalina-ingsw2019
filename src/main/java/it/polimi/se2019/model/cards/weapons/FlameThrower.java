package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public final class FlameThrower extends AlternateFire {
	private int SECONDARY_FOLLOWING_DAMAGE;
	private int SECONDARY_FOLLOWING_MARKS;

	public FlameThrower(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.PRIMARY_DAMAGE = 1;
		this.PRIMARY_MARKS = 0;
		this.SECONDARY_DAMAGE = 2;
		this.SECONDARY_MARKS = 0;
		this.SECONDARY_FOLLOWING_DAMAGE = 1;
		this.SECONDARY_FOLLOWING_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		secondaryDamagesAndMarks = new ArrayList<>();
		secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_FOLLOWING_DAMAGE, SECONDARY_FOLLOWING_MARKS));
	}


	/**
	 * Advances the weapon.
	 * This will be called if currentStep is at least 2.
	 *
	 * @param choice the choice of the player.
	 * @return the asking pair.
	 */
	@Override
	Pair handlePrimaryFire(int choice) {
		return null;
	}

	/**
	 * Advances the weapon.
	 * This will be called if currentStep is at least 2.
	 *
	 * @param choice the choice of the player.
	 * @return the asking pair.
	 */
	@Override
	Pair handleSecondaryFire(int choice) {
		return null;
	}

	/**
	 * Primary method of firing of the weapon. It interacts with the view and collects targeted players for its mode.
	 */
	@Override
	void primaryFire() {

	}

	/**
	 * Secondary mode of firing.
	 */
	@Override
	public void secondaryFire() {

	}

	/**
	 * Get the targets of the primary mode of fire for this weapon.
	 *
	 * @return the targettable players.
	 */
	@Override
	public List<Player> getPrimaryTargets() {
		return null;
	}

	/**
	 * Get the targets of the secondary mode of fire for this weapon.
	 *
	 * @return the targettable players.
	 */
	@Override
	public List<Player> getSecondaryTargets() {
		return null;
	}
}