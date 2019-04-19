package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.util.ArrayList;
import java.util.List;

public final class Cyberblade extends OptionalEffect {
	private final int PRIMARY_DAMAGE = 2;
	private final int PRIMARY_MARKS = 0;
	private final int OPTIONAL2_DAMAGE = 2;
	private final int OPTIONAL2_MARKS = 0;


	public Cyberblade(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.standardDamagesAndMarks.add(new DamageAndMarks(OPTIONAL2_DAMAGE, OPTIONAL2_MARKS));

	}

	@Override
	protected void handleFire(Boolean[] flags) {

	}

	@Override
	public void getAvailableOptions() {
		super.getAvailableOptions();

	}

	@Override
	public void optionalEffect1() {
	}

	@Override
	public void primaryFire() {

		dealDamage( this.standardDamagesAndMarks);

		}

	}
}