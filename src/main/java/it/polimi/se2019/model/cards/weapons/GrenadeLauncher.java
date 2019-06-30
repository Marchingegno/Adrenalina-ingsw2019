package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the weapon Grenade launcher.
 */
public class GrenadeLauncher extends OptionalChoiceWeapon {
	private List<Coordinates> possibleCoordinates;

	public GrenadeLauncher(JsonObject parameters) {
		super(parameters);
		hasOptionalEffects[1] = false;
		standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		for (int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
			optional1DamagesAndMarks.add(new DamageAndMarks(optional1Damage, optional1Marks));
		}

		baseName = "Basic effect.";
		moveName = "Move the target.";
		extraName = "Extra grenade.";

	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (isOptionalActive(1)) {
			return handleChoices(choice);
		}

		//Only move and base.
		if (getCurrentStep() == 2) {
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		}
		if (getCurrentStep() == 3) {
			target = currentTargets.get(choice);
			possibleCoordinates = getEnemyMovingCoordinates();
			return getMovingTargetEnemyCoordinatesQnO(target, possibleCoordinates);
		}
		if (getCurrentStep() == 4) {
			relocateEnemy(target, possibleCoordinates.get(choice));
			primaryFire();
		}
		return null;
	}

	@Override
	protected QuestionContainer handleMoveRequest(int choice) {
		possibleCoordinates = getEnemyMovingCoordinates();
		return getMovingTargetEnemyCoordinatesQnO(target, possibleCoordinates);
	}

	@Override
	protected QuestionContainer handleMoveAnswer(int choice) {
		relocateEnemy(target, possibleCoordinates.get(choice));
		return null;
	}

	@Override
	protected QuestionContainer handleExtraRequest(int choice) {
		possibleCoordinates = getExtraCoordinates();
		return getTargetCoordinatesQnO(possibleCoordinates);
	}

	@Override
	protected QuestionContainer handleExtraAnswer(int choice) {
		List<Player> playersToShoot = getGameMap().getPlayersFromCoordinates(possibleCoordinates.get(choice));
		playersToShoot.remove(getOwner());
		dealDamage(optional1DamagesAndMarks, playersToShoot);
		return null;
	}

	@Override
	public void primaryFire() {
		dealDamageAndConclude(standardDamagesAndMarks, target);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return getGameMap().getVisiblePlayers(getOwner());
	}

	private List<Coordinates> getExtraCoordinates() {
		List<Coordinates> visibleCoordinates = getGameMap().getVisibleCoordinates(getOwner());
		List<Coordinates> coordinatesWithPlayer = new ArrayList<>();
		for (Coordinates coordinate : visibleCoordinates) {
			List<Player> playerInThisCoordinates = getGameMap().getPlayersFromCoordinates(coordinate);

			if (getGameMap().getPlayerCoordinates(getOwner()).equals(coordinate)) {
				//If the coordinates is the same as owner's
				//See if there are other players other than owner.
				playerInThisCoordinates.remove(getOwner());
				if (!playerInThisCoordinates.isEmpty()) {
					coordinatesWithPlayer.add(coordinate);
				}
			} else {
				if (!playerInThisCoordinates.isEmpty()) {
					coordinatesWithPlayer.add(coordinate);
				}
			}
		}

		return coordinatesWithPlayer;
	}

	/**
	 * Finds the coordinate in which the target can be moved.
	 *
	 * @return the coordinates in which the target can be moved.
	 */
	private List<Coordinates> getEnemyMovingCoordinates() {
		return getGameMap().reachableCoordinates(target, 1);
	}


	@Override
	protected void updateBooleans() {
		canAddBase = !baseCompleted && !getPrimaryTargets().isEmpty();
		canAddMove = !moveCompleted && baseCompleted;
		canAddExtra = !extraCompleted && !getExtraCoordinates().isEmpty();


	}

	@Override
	public void reset() {
		super.reset();
		possibleCoordinates = new ArrayList<>();
	}

	@Override
	protected boolean canFireOptionalEffect1() {
		return !getExtraCoordinates().isEmpty();
	}

}
