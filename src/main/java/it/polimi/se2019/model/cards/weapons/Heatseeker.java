package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class Heatseeker extends WeaponCard {

	public Heatseeker(JsonObject parameters) {
		super(parameters);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.maximumSteps = 2;
	}

	@Override
	public QuestionContainer handleFire(int choice) {
		incrementCurrentStep();
		return handlePrimaryFire(choice);
	}

	@Override
	public void primaryFire() {
		dealDamage(standardDamagesAndMarks, target);
	}


	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> nonVisiblePlayers = getAllPlayers();
		nonVisiblePlayers.removeAll(getGameMap().reachableAndVisiblePlayers(getOwner(), 99));
		return nonVisiblePlayers;
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if(getCurrentStep() == 1){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 2){
			target = currentTargets.get(choice);
			primaryFire();
		}
		return null;
	}
}