package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.Representation;

public final class MachineGun extends OptionalFire {

	public MachineGun(String description) {
		super(description);
	}


	protected void primaryFire() {
	}

	public void optionalFire1() {
	}

	protected void optionalFire2() {
	}

	@Override
	public Representation getRep() {
		return null;
	}
}