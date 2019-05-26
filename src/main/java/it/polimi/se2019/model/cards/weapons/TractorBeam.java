package it.polimi.se2019.model.cards.weapons;


import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

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
		this.maximumAlternateSteps = 3;
		this.maximumSteps = 3;

	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		switch (getCurrentStep()){
			case 2:
				currentTargets = getPrimaryTargets();
				if(currentTargets.isEmpty()) {
					Utils.logError("currentTargets is null! See TractorBeam", new IllegalStateException());
					return null;
				}
				return getTargetPlayersQnO(currentTargets);
			case 3:
				target = currentTargets.get(choice);
				enemyRelocationCoordinates = getGameMap().getVisibleCoordinates(getOwner());
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
		switch (getCurrentStep()){
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
		dealDamage(standardDamagesAndMarks, target);
	}

	@Override
	public void secondaryFire() {
		dealDamage(secondaryDamagesAndMarks, target);
	}


	@Override
	public List<Player> getPrimaryTargets() {
		List<Coordinates> visibleCoordinates = getGameMap().getVisibleCoordinates(getOwner());
		List<Player> players = getAllPlayers();
		List<Player> targettablePlayers = new ArrayList<>();
		for (Player player: players) {
			if (player != getOwner() && getGameMap().isInTheMap(player)) {
				List<Coordinates> intersectionCoordinates = getGameMap().reachableCoordinates(player, 2);
				intersectionCoordinates.retainAll(visibleCoordinates);
				if(!intersectionCoordinates.isEmpty()){
					targettablePlayers.add(player);
				}
			}
		}
		return targettablePlayers;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return getGameMap().reachablePlayers(getOwner(), 2);
	}

	@Override
	public void reset() {
		super.reset();
		enemyRelocationCoordinates = new ArrayList<>();
	}


}