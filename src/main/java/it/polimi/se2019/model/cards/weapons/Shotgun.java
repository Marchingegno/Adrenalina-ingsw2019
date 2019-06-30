package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the weapon Shotgun.
 */
public class Shotgun extends AlternateFireWeapon {
	private List<Coordinates> listEnemyMoveCoordinates;

	public Shotgun(JsonObject parameters) {
		super(parameters);
		this.secondaryDamage = parameters.get("secondaryDamage").getAsInt();
		this.secondaryMarks = parameters.get("secondaryMarks").getAsInt();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
	}


	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (getCurrentStep() == 2) {
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		} else if (getCurrentStep() == 3) {
			this.target = currentTargets.get(choice);
			this.listEnemyMoveCoordinates = getEnemyMovingCoordinates();
			return getMovingTargetEnemyCoordinatesQnO(target, listEnemyMoveCoordinates);
		} else if (getCurrentStep() == 4) {
			relocateEnemy(target, listEnemyMoveCoordinates.get(choice));
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		if (getCurrentStep() == 2) {
			currentTargets = getSecondaryTargets();
			return getTargetPlayersQnO(currentTargets);
		} else if (getCurrentStep() == 3) {
			this.target = currentTargets.get(choice);
			secondaryFire();
		}
		return null;
	}

	@Override
	protected void primaryFire() {
		unifiedFire();
	}

	@Override
	public void secondaryFire() {
		unifiedFire();
	}

	/**
	 * Unifies primary/secondary fire.
	 */
	private void unifiedFire() {
		List<DamageAndMarks> chosenDamagesAndMarks = isAlternateFireActive() ? secondaryDamagesAndMarks : standardDamagesAndMarks;
		dealDamageAndConclude(chosenDamagesAndMarks, target);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		Coordinates ownCoordinates = getGameMap().getPlayerCoordinates(getOwner());
		List<Player> enemyPlayersInMySquare = getGameMap().getPlayersFromCoordinates(ownCoordinates);
		enemyPlayersInMySquare.remove(getOwner());
		return enemyPlayersInMySquare;
	}

	/**
	 * Finds the coordinate in which the target can be moved.
	 * @return the coordinates found.
	 */
	private List<Coordinates> getEnemyMovingCoordinates() {
		return getGameMap().reachableCoordinates(getOwner(), 1);
	}

	@Override
	public List<Player> getSecondaryTargets() {
		List<Coordinates> listAdjacentCoordinates = getGameMap().reachableCoordinates(getOwner(), 1);
		listAdjacentCoordinates.remove(getGameMap().getPlayerCoordinates(getOwner()));
		List<Player> adjacentPlayers = new ArrayList<>();
		for (Coordinates adjacentCoordinate : listAdjacentCoordinates) {
			adjacentPlayers.addAll(getGameMap().getPlayersFromCoordinates(adjacentCoordinate));
		}
		return adjacentPlayers;
	}


	@Override
	public void reset() {
		super.reset();
		listEnemyMoveCoordinates = new ArrayList<>();
	}


}