package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cyberblade extends OptionalChoiceWeapon {
	//BASE: Hit one target.
	//EXTRA: Hit a different target.
	//MOVE: Move before, after or in between effects.
	private List<Coordinates> possibleMoveCoordinates;
	private Player secondTarget;

	public Cyberblade(JsonObject parameters) {
		super(parameters);
		secondTarget = null;
	}

	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> players = getGameMap().getPlayersFromCoordinates(getGameMap().getPlayerCoordinates(getOwner()));
		players.remove(getOwner());
		return players;
	}

	@Override
	protected QuestionContainer handleMoveRequest(int choice) {
		possibleMoveCoordinates = getCoordinateWithEnemies(getNumberOfHitRemaining());
		return getMoveCoordinatesQnO(possibleMoveCoordinates);
	}

	@Override
	protected QuestionContainer handleMoveAnswer(int choice) {
		relocateOwner(possibleMoveCoordinates.get(choice));
		return null;
	}

	@Override
	protected QuestionContainer handleBaseRequest(int choice) {
		currentTargets = getPrimaryTargets();
		if (secondTarget != null) {
			currentTargets.remove(secondTarget);
		}
		return getTargetPlayersQnO(currentTargets);
	}

	@Override
	protected QuestionContainer handleExtraRequest(int choice) {
		currentTargets = getPrimaryTargets();
		if (target != null) {
			currentTargets.remove(target);
		}
		return getTargetPlayersQnO(currentTargets);
	}

	@Override
	protected QuestionContainer handleExtraAnswer(int choice) {
		secondTarget = currentTargets.remove(choice);
		return null;
	}


	/**
	 * Returns coordinates with at least numberOfEnemies in it.
	 *
	 * @param numberOfEnemies the minimum number of the enemies in the coordinates.
	 * @return an array of coordinates with at least two players.
	 */
	private List<Coordinates> getCoordinateWithEnemies(int numberOfEnemies) {
		List<Coordinates> reachable = getGameMap().reachableCoordinates(getOwner(), getMoveDistance());
		List<Coordinates> reachableWithParamPlayers = new ArrayList<>();
		for (Coordinates coordinate : reachable) {
			List<Player> playersInThisCoordinate = getGameMap().getPlayersFromCoordinates(coordinate);
			playersInThisCoordinate.remove(getOwner());
			if (playersInThisCoordinate.size() >= numberOfEnemies) {
				reachableWithParamPlayers.add(coordinate);
			}
		}
		return reachableWithParamPlayers;
	}

	private int getNumberOfHitRemaining() {
		if (isExtraActive()) {
			if (baseCompleted && extraCompleted)
				return 0;
			if (baseCompleted && !extraCompleted)
				return 1;
			if (!baseCompleted && extraCompleted)
				return 1;
			return 2;
		} else {
			if (baseCompleted)
				return 0;
			return 1;
		}
	}

	@Override
	protected void updateBooleans() {
		canAddBase = !baseCompleted && !getPrimaryTargets().isEmpty();
		canAddExtra = !extraCompleted && !getPrimaryTargets().isEmpty();
		canAddMove = !moveCompleted && !getCoordinateWithEnemies(getNumberOfHitRemaining()).isEmpty();
	}

	private boolean isExtraActive() {
		return isOptionalActive(2);
	}

	@Override
	protected boolean canPrimaryBeActivated() {
		return !getPrimaryTargets().isEmpty() && !getCoordinateWithEnemies(1).isEmpty();
	}

	@Override
	protected boolean canFireOptionalEffect1() {
		//If the only player nearby is the one on your square, can't fire.
		List<Coordinates> nearbyPlayerCoordinatesList = getCoordinateWithEnemies(1).stream()
				.filter(coordinates -> !coordinates.equals(getGameMap().getPlayerCoordinates(getOwner())))
				.collect(Collectors.toList());
		return !nearbyPlayerCoordinatesList.isEmpty();
	}

	@Override
	protected boolean canFireOptionalEffect2() {
		//If there are two people on your square
		List<Player> playersOnMySquare = getGameMap().getPlayersFromCoordinates(getGameMap().getPlayerCoordinates(getOwner()));
		playersOnMySquare.remove(getOwner());
		return playersOnMySquare.size() >= 2;

	}

	@Override
	protected boolean canAddBothOptionalEffects() {
		//There are at least 2 people nearby.
		//Here i'm going to use the fact that reachableCoordinates in GameMap returns also the coordinates that you're in.
		List<Coordinates> nearbyCoordinates = getCoordinateWithEnemies(1);
		List<Player> nearbyPlayers = new ArrayList<>();
		nearbyCoordinates.forEach(coordinates -> nearbyPlayers.addAll(getGameMap().getPlayersFromCoordinates(coordinates)));
		//Since the owner is in the coordinates, i'm going to remove it one time.
		nearbyPlayers.remove(getOwner());
		return !nearbyPlayers.isEmpty();
	}

	@Override
	public void reset() {
		super.reset();
		secondTarget = null;
	}
}
