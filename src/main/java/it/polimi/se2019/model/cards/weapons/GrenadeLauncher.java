package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.Representation;

public final class GrenadeLauncher extends OptionalFire {

	public GrenadeLauncher(String description) {
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