package it.polimi.se2019.model.cards.weapons;

public abstract class DoubleFire extends WeaponCard {

	@Override
	public void shoot() {
	}

	protected abstract void primaryFire();

	protected abstract void secondaryFire();

}