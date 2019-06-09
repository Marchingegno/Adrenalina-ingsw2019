package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class Cyberblade extends OptionalMoveWeapon {

	public Cyberblade(JsonObject parameters) {
		super(parameters);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.optional2Damage = parameters.get("optional2Damage").getAsInt();
		this.optional2Marks = parameters.get("optional2Marks").getAsInt();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.standardDamagesAndMarks.add(new DamageAndMarks(optional2Damage, optional2Marks));
	}


	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> playersOnMySquare = getGameMap().getPlayersFromCoordinates(getGameMap().getPlayerCoordinates(getOwner()));
		playersOnMySquare.remove(getOwner());
		return playersOnMySquare;
	}

	@Override
	protected QuestionContainer handleOptionalEffect1(int choice) {
		return null; //TODO Implement
	}

	@Override
	protected QuestionContainer handleOptionalEffect2(int choice) {
		if (getCurrentStep() == 2) {
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		} else if (getCurrentStep() == 3) {
			target = currentTargets.remove(choice);
			return getTargetPlayersQnO(currentTargets);
		} else if (getCurrentStep() == 4) {
			dealDamageAndConclude(standardDamagesAndMarks, target, currentTargets.get(choice));
		}
		return null;
	}

	@Override
	protected QuestionContainer handleNoOptionalEffects(int choice) {
		if (getCurrentStep() == 2) {
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		} else if (getCurrentStep() == 3) {
			dealDamageAndConclude(standardDamagesAndMarks, currentTargets.get(choice));
		}
		return null;
	}

	@Override
	protected boolean canAddOptionalEffect2() {
		//There are at least 2 player on the same square.
		return getPrimaryTargets().size() >= 2;
	}

}