package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Furnace extends AlternateFireWeapon {

	private List<Coordinates> targettableCoordinates;
	private Coordinates targetCoordinate;

	public Furnace(JsonObject parameters) {
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
		if(getCurrentStep() == 2){
			targettableCoordinates = getPrimaryCoordinates();
			return getTargetCoordinatesQnO(targettableCoordinates);
		}
		else if(getCurrentStep() == 3){
			targetCoordinate = targettableCoordinates.get(choice);
			currentTargets = getPrimaryTargets();
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		if(getCurrentStep() == 2){
			targettableCoordinates = getSecondaryCoordinates();
			return getTargetCoordinatesQnO(targettableCoordinates);
		}
		else if(getCurrentStep() == 3){
			targetCoordinate = targettableCoordinates.get(choice);
			currentTargets = getSecondaryTargets();
			secondaryFire();
		}
		return null;
	}

	public void primaryFire() {
		List<DamageAndMarks> damageAndMarksList = new ArrayList<>(getStandardDamagesAndMarks());
		for (int i = 0; i < currentTargets.size() - 1; i++) {
			damageAndMarksList.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		}
		dealDamage(damageAndMarksList, currentTargets);
	}

	public void secondaryFire() {
		List<DamageAndMarks> damageAndMarksList = new ArrayList<>(getSecondaryDamagesAndMarks());
		for (int i = 0; i < currentTargets.size() - 1; i++) {
			damageAndMarksList.add(new DamageAndMarks(secondaryDamage, secondaryMarks));
		}
		dealDamage(damageAndMarksList, currentTargets);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		List<Coordinates> roomCoordinates = getGameMap().getRoomCoordinates(targetCoordinate);
		List<Player> playersInTheRoom = new ArrayList<>();
		for (Coordinates coordinates : roomCoordinates) {
			playersInTheRoom.addAll(getGameMap().getPlayersFromCoordinates(coordinates));
		}
		return playersInTheRoom;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return getGameMap().getPlayersFromCoordinates(targetCoordinate);
	}

	private List<Coordinates> getPrimaryCoordinates(){
		List<Coordinates> doorsOfNearbyRooms = getGameMap().getDoors(getOwner());
		for (Coordinates coordinates : doorsOfNearbyRooms) {
			List<Coordinates> roomCoordinates = getGameMap().getRoomCoordinates(coordinates);
			List<Player> playersInTheRoom = new ArrayList<>();
			for (Coordinates item : roomCoordinates) {
				playersInTheRoom.addAll(getGameMap().getPlayersFromCoordinates(item));
			}
			//If there is not player in the room, this door can't be a target.
			if (playersInTheRoom.isEmpty()) {
				doorsOfNearbyRooms.remove(coordinates);
			}
		}
		return doorsOfNearbyRooms;
	}

	private List<Coordinates> getSecondaryCoordinates(){
		List<Coordinates> oneMoveCoordinates = getGameMap().reachableCoordinates(getOwner(), 1);
		oneMoveCoordinates.remove(getGameMap().getPlayerCoordinates(getOwner()));
		//Remove all coordinates that don't contain players.
		List<Coordinates> coordinatesWithEnemies = oneMoveCoordinates.stream()
				.filter(item -> !getGameMap().getPlayersFromCoordinates(item).isEmpty())
				.collect(Collectors.toList());
		targettableCoordinates = coordinatesWithEnemies;
		return targettableCoordinates;
	}


	@Override
	public void reset() {
		super.reset();
		targetCoordinate = null;
		targettableCoordinates = new ArrayList<>();
	}

	@Override
	protected boolean canSecondaryBeActivated() {
		//There's at least one square next to the player with enemies in it.
		return !getSecondaryCoordinates().isEmpty();
	}

	@Override
	protected boolean canPrimaryBeActivated() {
		//There's at least one door with players in the other room
		return !getPrimaryCoordinates().isEmpty();
	}
}