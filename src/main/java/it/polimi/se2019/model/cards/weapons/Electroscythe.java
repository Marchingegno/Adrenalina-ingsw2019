package it.polimi.se2019.model.cards.weapons;


import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the weapon Electroscythe.
 */
public class Electroscythe extends AlternateFireWeapon {

	public Electroscythe(JsonObject parameters) {
		super(parameters);
		this.setSecondaryDamage(parameters.get("secondaryDamage").getAsInt());
		this.setSecondaryMarks(parameters.get("secondaryMarks").getAsInt());
		this.getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.getSecondaryDamagesAndMarks().add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (getCurrentStep() == 2) {
			setCurrentTargets(getPrimaryTargets());
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		if (getCurrentStep() == 2) {
			setCurrentTargets(getSecondaryTargets());
			secondaryFire();
		}
		return null;
	}

	@Override
	public void primaryFire() {
		//Deal damage to everyone on your square
		List<DamageAndMarks> damageAndMarksList = new ArrayList<>(getStandardDamagesAndMarks());
		for (int i = 0; i < getCurrentTargets().size() - 1; i++) {
			damageAndMarksList.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		}
		dealDamageAndConclude(damageAndMarksList, getCurrentTargets());
	}

	@Override
	public void secondaryFire() {
		List<DamageAndMarks> damageAndMarksList = new ArrayList<>(getSecondaryDamagesAndMarks());
		for (int i = 0; i < getCurrentTargets().size() - 1; i++) {
			damageAndMarksList.add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));
		}
		dealDamageAndConclude(damageAndMarksList, getCurrentTargets());
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