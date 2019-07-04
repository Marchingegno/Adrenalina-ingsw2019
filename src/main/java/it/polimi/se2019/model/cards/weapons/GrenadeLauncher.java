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
 * @author Marchingeno
 */
public class GrenadeLauncher extends OptionalChoiceWeapon {
	private List<Coordinates> possibleCoordinates;

	public GrenadeLauncher(JsonObject parameters) {
		super(parameters);
		getHasOptionalEffects()[1] = false;
		getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		for (int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
			getOptional1DamagesAndMarks().add(new DamageAndMarks(optional1Damage, optional1Marks));
		}

		setBaseName("Basic effect.");
		setMoveName("Move the target.");
		setExtraName("Extra grenade.");

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
			setTarget(getCurrentTargets().get(choice));
			possibleCoordinates = getEnemyMovingCoordinates();
			return getMovingTargetEnemyCoordinatesQnO(getTarget(), possibleCoordinates);
		}
		if (getCurrentStep() == 4) {
			relocateEnemy(getTarget(), possibleCoordinates.get(choice));
			primaryFire();
		}
		return null;
	}

	@Override
	protected QuestionContainer handleMoveRequest(int choice) {
		possibleCoordinates = getEnemyMovingCoordinates();
		return getMovingTargetEnemyCoordinatesQnO(getTarget(), possibleCoordinates);
	}

	@Override
	protected void handleMoveAnswer(int choice) {
		relocateEnemy(getTarget(), possibleCoordinates.get(choice));
	}

	@Override
	protected QuestionContainer handleExtraRequest(int choice) {
		possibleCoordinates = getExtraCoordinates();
		return getTargetCoordinatesQnO(possibleCoordinates);
	}

	@Override
	protected void handleExtraAnswer(int choice) {
		List<Player> playersToShoot = getGameMap().getPlayersFromCoordinates(possibleCoordinates.get(choice));
		playersToShoot.remove(getOwner());
		dealDamage(getOptional1DamagesAndMarks(), playersToShoot);
	}

	@Override
	public void primaryFire() {
		dealDamageAndConclude(getStandardDamagesAndMarks(), getTarget());
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
		return getGameMap().reachableCoordinates(getTarget(), 1);
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
