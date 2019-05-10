package it.polimi.se2019.model.cards.weapons;


import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public final class Elecroscythe extends AlternateFire {
	private final int PRIMARY_DAMAGE = 1;
	private final int PRIMARY_MARKS = 0;
	private final int SECONDARY_DAMAGE = 2;
	private final int SECONDARY_MARKS = 0;


	public Elecroscythe(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.standardDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
	}

	h

	public List<Player> primaryFire() {
		//Deal damage to everyone on your square
		return getPrimaryTargets();

	}

	public List<Player> secondaryFire() {
		return getSecondaryTargets();
	}

	@Override
	public List<Player> getPrimaryTargets() {

	}

	@Override
	public List<Player> getSecondaryTargets() {
		return getPrimaryTargets();
	}
}