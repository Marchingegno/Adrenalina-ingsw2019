package it.polimi.se2019.model.cards.weapons;


import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class VortexCannon extends OptionalEffectsWeapon {
	private Coordinates vortexCoordinate;
	private List<Coordinates> temporaryPossibleVortexCoordinates;
	private List<Player> chosenTargets;


	public VortexCannon(JsonObject parameters) {
		super(parameters);
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2){
			temporaryPossibleVortexCoordinates = getVortexCoordinates();
			return getVortexQnO(temporaryPossibleVortexCoordinates);
		}
		else if(getCurrentStep() == 3){
			vortexCoordinate = temporaryPossibleVortexCoordinates.get(choice);
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		}
		else if(getCurrentStep() == 4){
			chosenTargets = new ArrayList<>();
			chosenTargets.add(currentTargets.get(choice));
		}

		if(isOptionalActive(1))
		{
			return handleOptionalEffect1(choice);
		}
		else{
			primaryFire();
		}
		return null;
	}

	@Override
	protected QuestionContainer handleOptionalEffect1(int choice) {
		if(getCurrentStep() == 4){
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		}
		else if(getCurrentStep() == 5){
			chosenTargets.add(currentTargets.get(choice));
			currentTargets = getPrimaryTargets();
			return getTargetPlayersAndRefusalQnO(currentTargets);
		} else if (getCurrentStep() == 6 && !isThisChoiceRefusal(currentTargets, choice)) {
			//The player can refuse.
			chosenTargets.add(currentTargets.get(choice));
		}
		primaryFire();
		return null;
	}

	@Override
	public void primaryFire() {
		chosenTargets.forEach(item -> relocateEnemy(item, vortexCoordinate));
		dealDamage(standardDamagesAndMarks, chosenTargets);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		//Players at most 1 move away from the vortex that are not already targeted.
		List<Coordinates> coordinatesSurroundingVortex = getGameMap().reachableCoordinates(vortexCoordinate, 1);
		List<Player> playersOneMoveAwayNotTargeted = new ArrayList<>();
		coordinatesSurroundingVortex.forEach(item -> playersOneMoveAwayNotTargeted.addAll(getGameMap().getPlayersFromCoordinates(item)));
		playersOneMoveAwayNotTargeted.removeAll(chosenTargets);
		playersOneMoveAwayNotTargeted.remove(getOwner());
		return playersOneMoveAwayNotTargeted;
	}

	private List<Coordinates> getVortexCoordinates(){
		List<Coordinates> possibleVortexCoordinates = getGameMap().getVisibleCoordinates(getOwner());
		possibleVortexCoordinates.remove(getGameMap().getPlayerCoordinates(getOwner()));
		return possibleVortexCoordinates;
	}

	private static QuestionContainer getVortexQnO(List<Coordinates> possibleVortexCoordinates){
		String question = "Where do you want to place the vortex?";
		List<Coordinates> options = new ArrayList<>(possibleVortexCoordinates);
		return QuestionContainer.createCoordinatesQuestionContainer(question, options);
	}


	@Override
	public void reset() {
		super.reset();
		vortexCoordinate = null;
		temporaryPossibleVortexCoordinates = new ArrayList<>();
		chosenTargets = new ArrayList<>();
	}

	@Override
	public boolean canBeActivated() {
		//there's at least one place where the owner can place the vortex that has a player next or on top of it.
		List<Coordinates> possibleVortexCoordinates = getVortexCoordinates();
		for (Coordinates coordinates : possibleVortexCoordinates) {
			List<Coordinates> coordinatesSurroundingThisVortex = getGameMap().reachableCoordinates(coordinates, 1);
			List<Player> playersOneMoveAway = new ArrayList<>();
			coordinatesSurroundingThisVortex.forEach(item -> playersOneMoveAway.addAll(getGameMap().getPlayersFromCoordinates(item)));
			playersOneMoveAway.remove(getOwner());
			if (!playersOneMoveAway.isEmpty()) {
				return true;
			}
		}
		return false;
	}
}