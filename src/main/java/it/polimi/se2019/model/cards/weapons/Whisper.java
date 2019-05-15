package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.Representation;

public final class Whisper extends WeaponCard {

	public Whisper(String description) {
		super("", description);
	}


	public void shoot() {
	}

	protected void primaryFire() {
	}


	@Override
	public Representation getRep() {
		return null;
	}
}