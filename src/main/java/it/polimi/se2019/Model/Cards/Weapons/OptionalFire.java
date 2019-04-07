package it.polimi.se2019.model.cards.weapons;

public abstract class OptionalFire extends WeaponCard {

	private boolean hasOptionalFire2;


	public OptionalFire(String description) {
		super(description);
	}


	@Override
	public void shoot() {
	}

	protected abstract void optionalFire1();

	protected abstract void optionalFire2();

	protected boolean hasOptionalFire2() {
		return hasOptionalFire2;
	}

	protected abstract void primaryFire();

}