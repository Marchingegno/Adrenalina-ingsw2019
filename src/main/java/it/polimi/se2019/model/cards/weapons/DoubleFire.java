package it.polimi.se2019.model.cards.weapons;

public abstract class DoubleFire extends WeaponCard {

	public DoubleFire(String description) {
		super("",  description);
	}


	@Override
	public void shoot() {
	}

	protected abstract void primaryFire();

	protected abstract void secondaryFire();

}