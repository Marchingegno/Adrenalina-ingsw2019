package it.polimi.se2019.model.cards.weapons;


import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Elecroscythe extends AlternateFireWeapon {

	public Elecroscythe(String description, List<AmmoType> reloadPrice) {
		super("Electroscythe", description, reloadPrice);
		this.PRIMARY_DAMAGE = 1;
		this.PRIMARY_MARKS = 0;
		this.SECONDARY_DAMAGE = 2;
		this.SECONDARY_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		this.maximumAlternateSteps = 2;
		this.maximumSteps = 2;
	}

	@Override
	Pair handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2) {
			currentTargets = getPrimaryTargets();
			primaryFire();
		}
		return null;
	}

	@Override
	Pair handleSecondaryFire(int choice) {
		if(getCurrentStep() == 2) {
			currentTargets = getSecondaryTargets();
			secondaryFire();
		}
		return null;
	}

	@Override
	public void primaryFire() {
		//Deal damage to everyone on your square
		List<DamageAndMarks> damageAndMarksList = new ArrayList<>(getStandardDamagesAndMarks());
		for (int i = 0; i < currentTargets.size() - 1; i++) {
			damageAndMarksList.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		}
		dealDamage(damageAndMarksList, currentTargets);
	}

	@Override
	public void secondaryFire() {
		List<DamageAndMarks> damageAndMarksList = new ArrayList<>(getSecondaryDamagesAndMarks());
		for (int i = 0; i < currentTargets.size() - 1; i++) {
			damageAndMarksList.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		}
		dealDamage(damageAndMarksList, currentTargets);
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