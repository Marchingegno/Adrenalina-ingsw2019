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
		this.maximumAlternateSteps = 2;
		this.maximumSteps = 2;
	}

	@Override
	public boolean doneFiring() {
		return super.doneFiring();
	}

	@Override
	public Pair handleFire(int choice) {
		incrementStep();
		Pair alternatePair = super.handleFire(choice); //Here the choice is registered.

		if (alternatePair == null) {
			if (isAlternateFireActive()) {
				return handleSecondaryFire(choice);
			} else {
				if(getCurrentStep() == 2) {
						currentTargets = getPrimaryTargets();
						primaryFire();
				}
			}
		}
		return null;
	}

	@Override
	Pair handleSecondaryFire(int choice) {
		currentTargets = getSecondaryTargets();
		secondaryFire();
		return null;
	}

	@Override
	public void primaryFire() {
		//Deal damage to everyone on your square
		List<DamageAndMarks> damageAndMarksList = new ArrayList<>(getStandardDamagesAndMarks());
		for (int i = 0; i < currentTargets.size() - 1; i++) {
			damageAndMarksList.add(getStandardDamagesAndMarks().get(0));
		}
		dealDamage(currentTargets, damageAndMarksList);
	}

	@Override
	public void secondaryFire() {
		List<DamageAndMarks> damageAndMarksList = new ArrayList<>(getSecondaryDamagesAndMarks());
		for (int i = 0; i < currentTargets.size() - 1; i++) {
			damageAndMarksList.add(getSecondaryDamagesAndMarks().get(0));
		}
		dealDamage(currentTargets, damageAndMarksList);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> targets = getGameMap().getPlayersFromCoordinates(getGameMap().getPlayerCoordinates(getOwner()));
		targets.remove(getOwner());
		return targets;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return getPrimaryTargets();
	}
}