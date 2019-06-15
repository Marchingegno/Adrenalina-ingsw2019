package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RocketLauncher extends OptionalChoiceWeapon {
	//BASE: Shoot at someone
	//MOVE: Move the target shot
	//EXTRA: Move at most 2 steps.

	private List<Coordinates> possibleMoveCoordinates;

	public RocketLauncher(JsonObject parameters) {
		super(parameters);
		hasOptionalEffects[1] = false;
		baseName = "Basic effect.";
		moveName = "Move the target.";
		extraName = "Move two steps.";
	}

	@Override
	protected void updateBooleans() {
		canAddBase = !baseCompleted && !getPrimaryTargets().isEmpty();
		canAddMove = !moveCompleted && baseCompleted;
		canAddExtra = extraCompleted && (baseCompleted || !getBeforeBaseExtraCoordinates().isEmpty());
		canAddExtra = canAddExtra && isOptionalActive(1);
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		return handleActionSelect(choice);
	}

	@Override
	protected QuestionContainer handleMoveRequest(int choice) {
		possibleMoveCoordinates = getGameMap().reachableCoordinates(target, 1);
		return getMovingTargetEnemyCoordinatesQnO(target, possibleMoveCoordinates);
	}

	@Override
	protected QuestionContainer handleMoveAnswer(int choice) {
		relocateEnemy(target, possibleMoveCoordinates.get(choice));
		return null;
	}

	@Override
	protected QuestionContainer handleExtraRequest(int choice) {
		possibleMoveCoordinates = baseCompleted ? getAfterBaseExtraCoordinates() : getBeforeBaseExtraCoordinates();
		return getMoveCoordinatesQnO(possibleMoveCoordinates);
	}

	@Override
	protected QuestionContainer handleExtraAnswer(int choice) {
		relocateOwner(possibleMoveCoordinates.get(choice));
		return null;
	}

	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> possibleTargets = getGameMap().getVisiblePlayers(getOwner());
		return possibleTargets.stream()
				.filter(player -> !getGameMap().getPlayerCoordinates(player).equals(getGameMap().getPlayerCoordinates(getOwner())))
				.collect(Collectors.toList());
	}

	private List<Coordinates> getBeforeBaseExtraCoordinates() {
		//Only coordinates in which there are at least 1 visible player (except owner) not in the same coordinate..
		List<Coordinates> twoMoveAwayCoordinates = getGameMap().reachableCoordinates(getOwner(), getMoveDistance());
		List<Coordinates> coordWithVisiblePlayers = new ArrayList<>();
		for (Coordinates coordinates : twoMoveAwayCoordinates) {
			List<Player> visiblePlayers = getGameMap().getVisiblePlayers(coordinates);
			visiblePlayers.removeAll(getGameMap().getPlayersFromCoordinates(coordinates));
			visiblePlayers.remove(getOwner());
			if (!visiblePlayers.isEmpty()) {
				coordWithVisiblePlayers.add(coordinates);
			}
		}
		return coordWithVisiblePlayers;
	}

	private List<Coordinates> getAfterBaseExtraCoordinates() {
		return getGameMap().reachableCoordinates(getOwner(), getMoveDistance());
	}


}