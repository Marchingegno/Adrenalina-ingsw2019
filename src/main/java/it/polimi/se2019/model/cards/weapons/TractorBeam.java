package it.polimi.se2019.model.cards.weapons;


import it.polimi.se2019.model.Representation;

public final class TractorBeam extends DoubleFire {

	public TractorBeam(String description) {
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