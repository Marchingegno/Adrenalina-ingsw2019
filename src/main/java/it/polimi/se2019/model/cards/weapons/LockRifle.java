package it.polimi.se2019.model.cards.weapons;


import it.polimi.se2019.model.Representation;

public final class LockRifle extends OptionalFire {

	public LockRifle(String description) {
		super(description);
	}


	protected void primaryFire() {
	}

	protected void optionalFire1() {
	}

	protected void optionalFire2() {
	}

	@Override
	public Representation getRep() {
		return null;
	}
}