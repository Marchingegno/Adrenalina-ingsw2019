package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.Representation;

public final class Railgun extends DoubleFire {

	public Railgun(String description) {
		super(description);
	}


	protected void primaryFire() {
	}

	protected void secondaryFire() {
	}


	@Override
	public Representation getRep() {
		return null;
	}
}