package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the weapon Railgun.
 */
public class Railgun extends AlternateFireWeapon {
	private List<String> availableDirections;
	private CardinalDirection chosenDirection;
	private Player secondTarget;

	public Railgun(JsonObject parameters) {
		super(parameters);
		this.setSecondaryDamage(parameters.get("secondaryDamage").getAsInt());
		this.setSecondaryMarks(parameters.get("secondaryMarks").getAsInt());
		this.getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.getSecondaryDamagesAndMarks().add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));
		this.getSecondaryDamagesAndMarks().add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));
		this.availableDirections = new ArrayList<>();
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (getCurrentStep() == 2) {
			this.availableDirections = getAvailableDirections();
			return getCardinalQnO(availableDirections);
		} else if (getCurrentStep() == 3) {
			chosenDirection = Enum.valueOf(CardinalDirection.class, availableDirections.get(choice));
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		} else if (getCurrentStep() == 4) {
			setTarget(getCurrentTargets().get(choice));
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		if (getCurrentStep() == 2) {
			this.availableDirections = getAvailableDirections();
			return getCardinalQnO(availableDirections);
		} else if (getCurrentStep() == 3) {
			chosenDirection = Enum.valueOf(CardinalDirection.class, availableDirections.get(choice));
			return setSecondaryCurrentTargetsAndReturnTargetQnO();
		} else if (getCurrentStep() == 4) {
			setTarget(getCurrentTargets().get(choice));
			//With refusal.
			setCurrentTargets(getSecondaryTargets());
			return getTargetPlayersAndRefusalQnO(getCurrentTargets());
		} else if (getCurrentStep() == 5) {
			if (!isThisChoiceRefusal(getCurrentTargets(), choice)) {
				secondTarget = getCurrentTargets().get(choice);
			}
			secondaryFire();
		}

		return null;
	}

	@Override
	public void primaryFire() {
		dealDamageAndConclude(getStandardDamagesAndMarks(), getTarget());
	}

	public void secondaryFire() {
		dealDamageAndConclude(getSecondaryDamagesAndMarks(), getTarget(), secondTarget);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> playersInDirection = getGameMap().getPlayersInDirection(getGameMap().getPlayerCoordinates(getOwner()), chosenDirection);
		playersInDirection.remove(getOwner());
		return playersInDirection;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		List<Player> playersInDirection = getPrimaryTargets();
		if (getTarget() != null) {
			playersInDirection.remove(getTarget());
		}
		return playersInDirection;
	}

	@Override
	public void reset() {
		super.reset();
		secondTarget = null;
		chosenDirection = null;
	}

	@Override
	public boolean canPrimaryBeActivated() {
		//There's at least one direction with enemies in it.
		return !getAvailableDirections().isEmpty();
	}

	/**
	 * Finds the available directions the player can fire at.
	 *
	 * @return the directions found.
	 */
	private List<String> getAvailableDirections() {
		List<String> directionsFound = new ArrayList<>();
		for (CardinalDirection direction : CardinalDirection.values()) {
			List<Player> playersInDirection = getGameMap().getPlayersInDirection(getGameMap().getPlayerCoordinates(getOwner()), direction);
			playersInDirection.remove(getOwner());
			Utils.logWeapon("RAILGUN: found in direction " + direction.toString() + " these player(s)");
			playersInDirection.forEach(player -> Utils.logWeapon(player.getPlayerName()));
			if (!playersInDirection.isEmpty()) {
				directionsFound.add(direction.toString());
			}
		}
		return directionsFound;
	}

	@Override
	protected boolean canSecondaryBeFired() {
		return canPrimaryBeActivated();
	}


}