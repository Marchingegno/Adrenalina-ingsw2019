package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.List;
import java.util.stream.Collectors;

public class Heatseeker extends WeaponCard {

	public Heatseeker(JsonObject parameters) {
		super(parameters);
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
	}

	@Override
	public QuestionContainer doActivationStep(int choice) {
		incrementCurrentStep();
		return handlePrimaryFire(choice);
	}

	@Override
	public void primaryFire() {
		dealDamageAndConclude(standardDamagesAndMarks, target);
	}


	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> allPlayers = getAllPlayers();
		List<Player> nonVisiblePlayers = allPlayers.stream()
				.filter(player -> getGameMap().isInTheMap(player))
				.collect(Collectors.toList());
		nonVisiblePlayers.removeAll(getGameMap().getVisiblePlayers(getOwner()));
		nonVisiblePlayers.remove(getOwner());
		return nonVisiblePlayers;
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (getCurrentStep() == 1) {
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		} else if (getCurrentStep() == 2) {
			target = currentTargets.get(choice);
			primaryFire();
		}
		return null;
	}

	@Override
	public QuestionContainer initialQuestion() {
		return null;
	}

}