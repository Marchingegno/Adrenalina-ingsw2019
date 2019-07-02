package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.List;

public class Whisper extends WeaponCard {
	/**
	 * Class of the weapon Whisper.
	 */
	public Whisper(JsonObject parameters) {
		super(parameters);
		getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
	}

	@Override
	public QuestionContainer doActivationStep(int choice) {
		incrementCurrentStep();
		return handlePrimaryFire(choice);
	}


	@Override
	public void primaryFire() {
		dealDamageAndConclude(getStandardDamagesAndMarks(), getTarget());
	}


	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> distantPlayers = getGameMap().getVisiblePlayers(getOwner());
		distantPlayers.removeAll(getGameMap().reachablePlayers(getOwner(), 1));
		return distantPlayers;
	}


	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (getCurrentStep() == 1) {
			setCurrentTargets(getPrimaryTargets());
			return getTargetPlayersQnO(getCurrentTargets());
		} else if (getCurrentStep() == 2) {
			setTarget(getCurrentTargets().get(choice));
			primaryFire();
		}
		return null;
	}

	@Override
	public QuestionContainer initialQuestion() {
		return null;
	}


}