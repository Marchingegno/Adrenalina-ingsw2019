package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cyberblade extends OptionalMoveWeapon {
	Player secondTarget;

	public Cyberblade(JsonObject parameters) {
		super(parameters);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.optional2Damage = parameters.get("optional2Damage").getAsInt();
		this.optional2Marks = parameters.get("optional2Marks").getAsInt();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.standardDamagesAndMarks.add(new DamageAndMarks(optional2Damage, optional2Marks));
	}

	@Override
	protected List<Coordinates> getMoveCoordinates() {
		return getGameMap().reachableCoordinates(getOwner(), getMoveDistance());
	}

	@Override
	protected List<Coordinates> getMoveBeforeFiringCoordinates() {
		List<Coordinates> adjacentCoordinates = getMoveCoordinates();
		return adjacentCoordinates.stream()
				.filter(coordinates -> !getGameMap().getPlayersFromCoordinates(coordinates).isEmpty())
				.collect(Collectors.toList());
	}


	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> playersOnMySquare = getGameMap().getPlayersFromCoordinates(getGameMap().getPlayerCoordinates(getOwner()));
		playersOnMySquare.remove(getOwner());
		return playersOnMySquare;
	}

	@Override
	protected QuestionContainer handleOptionalEffect1(int choice) {
		if (getCurrentStep() == 2) {
			return getPossibleActionTypeQnO();
		}
		if (getCurrentStep() == 3) {
			actionTypeInExecution = ActionType.values()[choice];
			if (actionTypeInExecution == ActionType.MOVE) {
				return handleMoveRequestAnswer(choice);
			} else {
				return doActivationStep(choice);
			}
		}
		if (getCurrentStep() == 4) {
			if (actionTypeInExecution == ActionType.MOVE) {
				handleMoveRequestAnswer(choice);
			}
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		}
		if (getCurrentStep() == 5) {
			target = currentTargets.get(choice);
			if (!relocationDone) {
				return handleMoveRequestAnswer(choice);
			}
		}
		if (getCurrentStep() == 6) {
			handleMoveRequestAnswer(choice);
		}

		primaryFire();
		return null;
	}

	@Override
	protected QuestionContainer handleBothOptionalEffects(int choice) {
		if (getCurrentStep() == 6) {
			handleMoveRequestAnswer(choice);
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		}
		if (getCurrentStep() == 7) {
			secondTarget = currentTargets.get(choice);
			return handleMoveRequestAnswer(choice);
		}
		if (getCurrentStep() == 8) {
			handleMoveRequestAnswer(choice);
		} else {
			return handleOptionalEffect1(choice);
		}
		primaryFire();
		return null;
	}

	@Override
	protected QuestionContainer handleOptionalEffect2(int choice) {
		if (getCurrentStep() == 2) {
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		} else if (getCurrentStep() == 3) {
			target = currentTargets.remove(choice);
			return getTargetPlayersQnO(currentTargets);
		} else if (getCurrentStep() == 4) {
			secondTarget = currentTargets.get(choice);
		}
		primaryFire();
		return null;
	}

	@Override
	protected QuestionContainer handleNoOptionalEffects(int choice) {
		if (getCurrentStep() == 2) {
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		} else if (getCurrentStep() == 3) {
			primaryFire();
		}
		return null;
	}

	@Override
	protected boolean canAddOptionalEffect2() {
		//There are at least 2 player on the same square.
		return getPrimaryTargets().size() >= 2;
	}

	@Override
	protected boolean canAddBothOptionalEffects() {
		//At least 2 targets in adjacent squares.
		List<Coordinates> possibleCoordinates = getMoveBeforeFiringCoordinates();
		List<Player> possibleTargets = new ArrayList<>();
		possibleCoordinates.stream()
				.map(coordinates -> getGameMap().getPlayersFromCoordinates(coordinates))
				.forEach(possibleTargets::addAll);

		return possibleTargets.size() >= 2;
	}

	@Override
	public void primaryFire() {
		if (isOptionalActive(2)) {
			dealDamageAndConclude(standardDamagesAndMarks, target, secondTarget);
		} else {
			dealDamageAndConclude(standardDamagesAndMarks, target);
		}
	}
}