package it.polimi.se2019.model.cards.weapons;


import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class Electroscythe extends AlternateFireWeapon {

	public Electroscythe(JsonObject parameters) {
		super(parameters);
		this.secondaryDamage = parameters.get("secondaryDamage").getAsInt();
		this.secondaryMarks = parameters.get("secondaryMarks").getAsInt();
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(secondaryDamage, secondaryMarks));
		this.maximumAlternateSteps = 2;
		this.maximumSteps = 2;
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (getCurrentStep() == 2) {
			currentTargets = getPrimaryTargets();
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		if (getCurrentStep() == 2) {
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
			damageAndMarksList.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		}
		dealDamageAndConclude(damageAndMarksList, currentTargets);
	}

	@Override
	public void secondaryFire() {
		List<DamageAndMarks> damageAndMarksList = new ArrayList<>(getSecondaryDamagesAndMarks());
		for (int i = 0; i < currentTargets.size() - 1; i++) {
			damageAndMarksList.add(new DamageAndMarks(secondaryDamage, secondaryMarks));
		}
		dealDamageAndConclude(damageAndMarksList, currentTargets);
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