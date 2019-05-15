package it.polimi.se2019.model.cards.weapons;


import it.polimi.se2019.model.Representation;

public final class Hellion extends DoubleFire {

	public Hellion(String description) {
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