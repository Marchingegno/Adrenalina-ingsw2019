package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class of the weapon Rocket launcher.
 * @author Marchingeno
 */
public class RocketLauncher extends OptionalChoiceWeapon {
	//BASE: Shoot at someone
	//MOVE: Move the target shot
	//EXTRA: Move at most 2 steps.

	private List<Coordinates> possibleMoveCoordinates;

	public RocketLauncher(JsonObject parameters) {
		super(parameters);
		setBaseName("Basic effect.");
		setMoveName("Move the target.");
		setExtraName("Move two steps.");
		getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		for (int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
			getOptional2DamagesAndMarks().add(new DamageAndMarks(optional2Damage, optional2Marks));
		}
	}

	@Override
	protected void updateBooleans() {
		canAddBase = !baseCompleted && !getPrimaryTargets().isEmpty();
		canAddMove = !moveCompleted && baseCompleted;
		canAddExtra = !extraCompleted && (baseCompleted || !getBeforeBaseExtraCoordinates().isEmpty());
		canAddExtra = canAddExtra && isOptionalActive(1);
	}

	@Override
	protected QuestionContainer handleMoveRequest(int choice) {
		possibleMoveCoordinates = getGameMap().reachableCoordinates(getTarget(), 1);
		return getMovingTargetEnemyCoordinatesQnO(getTarget(), possibleMoveCoordinates);
	}

	@Override
	protected void handleMoveAnswer(int choice) {
		if (isOptionalActive(2)) {
			dealDamage(getOptional2DamagesAndMarks(), getGameMap().getPlayersFromCoordinates(getGameMap().getPlayerCoordinates(getTarget())));
		}
		relocateEnemy(getTarget(), possibleMoveCoordinates.get(choice));
	}

	@Override
	protected QuestionContainer handleExtraRequest(int choice) {
		possibleMoveCoordinates = baseCompleted ? getAfterBaseExtraCoordinates() : getBeforeBaseExtraCoordinates();
		return getMoveCoordinatesQnO(possibleMoveCoordinates);
	}

	@Override
	protected void handleExtraAnswer(int choice) {
		relocateOwner(possibleMoveCoordinates.get(choice));
	}

	@Override
	public List<Player> getPrimaryTargets() {
		//Visible players not in owner's square.
		List<Player> possibleTargets = getGameMap().getVisiblePlayers(getOwner());
		return possibleTargets.stream()
				.filter(player -> !getGameMap().getPlayerCoordinates(player).equals(getGameMap().getPlayerCoordinates(getOwner())))
				.collect(Collectors.toList());
	}

	/**
	 * Get the coordinates of the EXTRA choice before base is completed.
	 *
	 * @return the coordinates found.
	 */
	private List<Coordinates> getBeforeBaseExtraCoordinates() {
		//Only coordinates in which there are at least 1 visible player (except owner) not in the same coordinate..
		List<Coordinates> twoMoveAwayCoordinates = getGameMap().reachableCoordinates(getOwner(), getMoveDistance());
		Utils.logWeapon("Two move away coordinates");
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

	@Override
	protected boolean canPrimaryBeActivated() {
		return !getPrimaryTargets().isEmpty() || (!getBeforeBaseExtraCoordinates().isEmpty() && canAffordOptionalEffect1());
	}

	@Override
	public void primaryFire() {
		dealDamageAndConclude(getStandardDamagesAndMarks(), getTarget());
	}

	@Override
	protected boolean canFireOptionalEffect1() {
		return true;
	}

	@Override
	protected boolean canFireOptionalEffect2() {
		return canAddBaseWithoutEffects();
	}

	@Override
	protected boolean canFireBothOptionalEffects() {
		return true;
	}

	/**
	 * Get the coordinates of the EXTRA choice after base is completed.
	 *
	 * @return the coordinates found.
	 */
	private List<Coordinates> getAfterBaseExtraCoordinates() {
		return getGameMap().reachableCoordinates(getOwner(), getMoveDistance());
	}
}