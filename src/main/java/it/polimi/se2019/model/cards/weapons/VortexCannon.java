package it.polimi.se2019.model.cards.weapons;


import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class VortexCannon extends OptionalEffectsWeapon {
	private Coordinates vortexCoordinate;
	private List<Coordinates> temporaryPossibleVortexCoordinates;
	private List<Player> chosenTargets;


	public VortexCannon(JsonObject parameters) {
		super(parameters);
		hasOptionalEffects[1] = false;
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.standardDamagesAndMarks.add(new DamageAndMarks(optional1Damage, optional1Marks));
		this.standardDamagesAndMarks.add(new DamageAndMarks(optional1Damage, optional1Marks));
	}

	private static QuestionContainer getVortexQnO(List<Coordinates> possibleVortexCoordinates) {
		String question = "Where do you want to place the vortex?";
		List<Coordinates> options = new ArrayList<>(possibleVortexCoordinates);
		return QuestionContainer.createCoordinatesQuestionContainer(question, options);
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (getCurrentStep() == 2) {
			temporaryPossibleVortexCoordinates = getVortexCoordinates();
			return getVortexQnO(temporaryPossibleVortexCoordinates);
		} else if (getCurrentStep() == 3) {
			vortexCoordinate = temporaryPossibleVortexCoordinates.get(choice);
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		} else if (getCurrentStep() == 4) {
			chosenTargets = new ArrayList<>();
			chosenTargets.add(currentTargets.get(choice));
		}

		if (isOptionalActive(1)) {
			return handleOptionalEffect1(choice);
		} else {
			primaryFire();
		}
		return null;
	}

	@Override
	public void primaryFire() {
		chosenTargets.forEach(item -> relocateEnemy(item, vortexCoordinate));
		dealDamageAndConclude(standardDamagesAndMarks, chosenTargets);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		//Players at most 1 move away from the vortex that are not already targeted.
		List<Coordinates> coordinatesSurroundingVortex = getGameMap().reachableCoordinates(vortexCoordinate, 1);
		List<Player> playersOneMoveAwayNotTargeted = new ArrayList<>();
		coordinatesSurroundingVortex.forEach(item -> playersOneMoveAwayNotTargeted.addAll(getGameMap().getPlayersFromCoordinates(item)));
		if (chosenTargets != null && !chosenTargets.isEmpty()) {
			playersOneMoveAwayNotTargeted.removeAll(chosenTargets);
		}
		playersOneMoveAwayNotTargeted.remove(getOwner());
		return playersOneMoveAwayNotTargeted;
	}

	@Override
	protected QuestionContainer handleOptionalEffect1(int choice) {
		if (getCurrentStep() == 4) {
			currentTargets = getPrimaryTargets();
			if (currentTargets.isEmpty()) {
				Utils.logWeapon("The player cannot choose further targets.");
				primaryFire();
				return null;
			} else {
				return getTargetPlayersQnO(currentTargets);
			}
		} else if (getCurrentStep() == 5) {
			chosenTargets.add(currentTargets.get(choice));
			currentTargets = getPrimaryTargets();
			if (currentTargets.isEmpty()) {
				Utils.logWeapon("The player cannot choose further targets.");
				primaryFire();
				return null;
			} else {
				return getTargetPlayersAndRefusalQnO(currentTargets);
			}
		} else if (getCurrentStep() == 6 && !isThisChoiceRefusal(currentTargets, choice)) {
			//The player can refuse.
			chosenTargets.add(currentTargets.get(choice));
		}
		primaryFire();
		return null;
	}

	private List<Coordinates> getVortexCoordinates() {
		//there's at least one place where the owner can place the vortex that has a player next or on top of it.
		List<Coordinates> possibleVortexCoordinates = getGameMap().getVisibleCoordinates(getOwner());
		possibleVortexCoordinates.remove(getGameMap().getPlayerCoordinates(getOwner()));
		int minimumNumberOfPlayerNearby = isOptionalActive(1) ? 2 : 1;

		List<Coordinates> accettableVortexCoordinates = new ArrayList<>();
		for (Coordinates coordinates : possibleVortexCoordinates) {
			List<Coordinates> coordinatesSurroundingThisVortex = getGameMap().reachableCoordinates(coordinates, 1);
			List<Player> playersOneMoveAway = new ArrayList<>();
			coordinatesSurroundingThisVortex.forEach(item -> playersOneMoveAway.addAll(getGameMap().getPlayersFromCoordinates(item)));
			playersOneMoveAway.remove(getOwner());
			if (playersOneMoveAway.size() >= minimumNumberOfPlayerNearby) {
				accettableVortexCoordinates.add(coordinates);
			}
		}
		return accettableVortexCoordinates;
	}


	@Override
	public void reset() {
		super.reset();
		vortexCoordinate = null;
		temporaryPossibleVortexCoordinates = new ArrayList<>();
		chosenTargets = new ArrayList<>();
	}

	@Override
	protected boolean canAddBaseWithoutEffects() {
		return canPrimaryBeActivated();
	}

	@Override
	protected boolean canPrimaryBeActivated() {
		return !getVortexCoordinates().isEmpty();
	}

	@Override
	protected boolean canFireOptionalEffect1() {
		//there's at least one place where the owner can place the vortex that has more than one player next or on top of it.
		List<Coordinates> possibleVortexCoordinates = getGameMap().getVisibleCoordinates(getOwner());
		possibleVortexCoordinates.remove(getGameMap().getPlayerCoordinates(getOwner()));

		List<Coordinates> accettableVortexCoordinates = new ArrayList<>();
		for (Coordinates coordinates : possibleVortexCoordinates) {
			List<Coordinates> coordinatesSurroundingThisVortex = getGameMap().reachableCoordinates(coordinates, 1);
			List<Player> playersOneMoveAway = new ArrayList<>();
			coordinatesSurroundingThisVortex.forEach(item -> playersOneMoveAway.addAll(getGameMap().getPlayersFromCoordinates(item)));
			playersOneMoveAway.remove(getOwner());
			if (playersOneMoveAway.size() > 1) {
				accettableVortexCoordinates.add(coordinates);
			}
		}
		return !accettableVortexCoordinates.isEmpty();
	}
}