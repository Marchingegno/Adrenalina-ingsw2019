package it.polimi.se2019.model.cards.weapons;


import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TractorBeam extends AlternateFireWeapon {

	private List<Coordinates> enemyRelocationCoordinates;

	public TractorBeam(JsonObject parameters) {
		super(parameters);
		this.secondaryDamage = parameters.get("secondaryDamage").getAsInt();
		this.secondaryMarks = parameters.get("secondaryMarks").getAsInt();
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(secondaryDamage, secondaryMarks));

	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		switch (getCurrentStep()) {
			case 2:
				currentTargets = getPrimaryTargets();
				if (currentTargets.isEmpty()) {
					Utils.logError("currentTargets is null! See TractorBeam", new IllegalStateException());
					return null;
				}
				return getTargetPlayersQnO(currentTargets);
			case 3:
				target = currentTargets.get(choice);
				enemyRelocationCoordinates = getEnemyRelocationCoordinates();
				return getMovingTargetEnemyCoordinatesQnO(target, enemyRelocationCoordinates);
			case 4:
				relocateEnemy(target, enemyRelocationCoordinates.get(choice));
				primaryFire();
				break;
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		switch (getCurrentStep()) {
			case 2:
				currentTargets = getSecondaryTargets();
				return getTargetPlayersQnO(currentTargets);
			case 3:
				target = currentTargets.get(choice);
				getGameMap().movePlayerTo(target, getGameMap().getPlayerCoordinates(getOwner()));
				secondaryFire();
				break;
		}
		return null;
	}

	@Override
	public void primaryFire() {
		dealDamageAndConclude(standardDamagesAndMarks, target);
	}

	@Override
	public void secondaryFire() {
		dealDamageAndConclude(secondaryDamagesAndMarks, target);
	}


	@Override
	public List<Player> getPrimaryTargets() {
		List<Coordinates> visibleCoordinates = getGameMap().getVisibleCoordinates(getOwner());
		//Only players in the map
		List<Player> players = getAllPlayers().stream().filter(player -> getGameMap().isInTheMap(player)).collect(Collectors.toList());
		players.remove(getOwner());
		List<Player> targettablePlayers = new ArrayList<>();
		for (Player player : players) {
			List<Coordinates> intersectionCoordinates = getGameMap().reachableCoordinates(player, 2);
			intersectionCoordinates.retainAll(visibleCoordinates);
			if (!intersectionCoordinates.isEmpty()) {
				targettablePlayers.add(player);
			}

		}
		return targettablePlayers;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return getGameMap().reachablePlayers(getOwner(), 2);
	}

	public List<Coordinates> getEnemyRelocationCoordinates() {
		List<Coordinates> intersectionCoordinates = getGameMap().reachableCoordinates(target, 2);
		intersectionCoordinates.retainAll(getGameMap().getVisibleCoordinates(getOwner()));
		return intersectionCoordinates;
	}

	@Override
	public void reset() {
		super.reset();
		enemyRelocationCoordinates = new ArrayList<>();
	}


}