package it.polimi.se2019.Model.Cards.Weapons;

import java.util.*;

/**
 *
 */
public abstract class OptionalFire extends WeaponCard {

	/**
	 * Default constructor
	 */
	public OptionalFire() {
	}

	/**
	 *
	 */
	private boolean hasOptionalFire2;

	/**
	 *
	 */
	public void shoot() {
		// TODO implement here
	}

	/**
	 *
	 */
	protected abstract void primaryFire();

	/**
	 *
	 */
	protected abstract void optionalFire1();

	/**
	 *
	 */
	protected abstract void optionalFire2();

	/**
	 * @return
	 */
	protected boolean hasOptionalFire2() {
		// TODO implement here
		return false;
	}

}