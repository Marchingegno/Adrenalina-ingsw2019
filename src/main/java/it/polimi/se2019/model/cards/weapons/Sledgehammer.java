package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the weapon Sledgehammer.
 * @author Marchingeno
 */
public class Sledgehammer extends AlternateFireWeapon {
	private List<Coordinates> enemyMovingCoordinates;

	public Sledgehammer(JsonObject parameters) {
		super(parameters);
		this.getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.getSecondaryDamagesAndMarks().add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));

	}


	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (getCurrentStep() == 2) {
			setCurrentTargets(getPrimaryTargets());
			return getTargetPlayersQnO(getCurrentTargets());
		} else if (getCurrentStep() == 3) {
			this.setTarget(getCurrentTargets().get(choice));
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		if (getCurrentStep() == 2) {
			setCurrentTargets(getPrimaryTargets());
			return getTargetPlayersQnO(getCurrentTargets());
		} else if (getCurrentStep() == 3) {
			this.setTarget(getCurrentTargets().get(choice));
			enemyMovingCoordinates = getEnemyMovingCoordinates();
			return getMovingTargetEnemyCoordinatesQnO(getTarget(), enemyMovingCoordinates);
		} else if (getCurrentStep() == 4) {
			relocateEnemy(getTarget(), enemyMovingCoordinates.get(choice));
			secondaryFire();
		}
		return null;
	}

	@Override
	protected void primaryFire() {
		unifiedFire();
	}

	/**
	 * Unifies primary/secondary fire.
	 */
	private void unifiedFire() {
		List<DamageAndMarks> damageAndMarksList = isAlternateFireActive() ? getSecondaryDamagesAndMarks() : getStandardDamagesAndMarks();
		dealDamageAndConclude(damageAndMarksList, getTarget());
	}

	@Override
	public void secondaryFire() {
		unifiedFire();
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return getGameMap().reachablePlayers(getOwner(), 0);
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return getPrimaryTargets();
	}

	/**
	 * Finds the coordinate in which the target can be moved.
	 *
	 * @return the coordinates found.
	 */
	private List<Coordinates> getEnemyMovingCoordinates() {
		List<Coordinates> coordinates = getGameMap().reachablePerpendicularCoordinates(getOwner(), 2);
		coordinates.add(getGameMap().getPlayerCoordinates(getOwner()));
		return coordinates;
	}


	@Override
	public void reset() {
		super.reset();
		enemyMovingCoordinates = new ArrayList<>();
	}


}