package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.Representation;

public final class Sledgehammer extends DoubleFire {

	public Sledgehammer(String description) {
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