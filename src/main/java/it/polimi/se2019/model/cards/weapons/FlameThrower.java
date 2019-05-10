package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.util.ArrayList;

public final class FlameThrower extends AlternateFire {
	private final int PRIMARY_DAMAGE = 1;
	private final int PRIMARY_MARKS = 0;
	private final int SECONDARY_DAMAGE = 2;
	private final int SECONDARY_MARKS = 0;
	private ArrayList<DamageAndMarks> secondaryDamagesAndMarks;

	public FlameThrower(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.standardDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		secondaryDamagesAndMarks = new ArrayList<>();
		secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		secondaryDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
	}


	public void primaryFire() {
	}

	public void secondaryFire() {

	}


}