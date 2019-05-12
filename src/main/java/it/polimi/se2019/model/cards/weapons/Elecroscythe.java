package it.polimi.se2019.model.cards.weapons;


import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

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

	@Override
	public Pair handleFire(int choice) {
		incrementStep();
		Pair alternatePair = super.handleFire(choice);

		if (alternatePair == null) {
			if (isAlternateFireActive()) {
				return handleSecondaryFire(choice);
			} else {
				switch (getCurrentStep()) {
					case 2:

				}
				primaryFire();
			}
			return null;
		}
	}

	@Override
	Pair handleSecondaryFire(int choice) {
		secondaryFire();
		return null;
	}

	@Override
	public void primaryFire() {
		//Deal damage to everyone on your square
		dealDamage(currentTargets, getStandardDamagesAndMarks());
	}

	public void secondaryFire() {
		dealDamage(currentTargets, getSecondaryDamagesAndMarks());
	}

	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> targets = getGameMap().getPlayersFromCoordinates(getGameMap().getPlayerCoordinates(getOwner()));
		targets.remove(getOwner());
		currentTargets = targets;
		return currentTargets;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return getPrimaryTargets();
	}
}