package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class of the weapon Furnace.
 * @author Marchingeno
 */
public class Furnace extends AlternateFireWeapon {

	private List<Coordinates> targettableCoordinates;
	private Coordinates targetCoordinate;

	public Furnace(JsonObject parameters) {
		super(parameters);
		this.setSecondaryDamage(parameters.get("secondaryDamage").getAsInt());
		this.setSecondaryMarks(parameters.get("secondaryMarks").getAsInt());
		this.getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.getSecondaryDamagesAndMarks().add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (getCurrentStep() == 2) {
			targettableCoordinates = getPrimaryCoordinates();
			targettableCoordinates.forEach(coordinates -> Utils.logWeapon(coordinates.toString()));
			return getTargetCoordinatesQnO(targettableCoordinates);
		} else if (getCurrentStep() == 3) {
			targetCoordinate = targettableCoordinates.get(choice);
			setCurrentTargets(getPrimaryTargets());
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		if (getCurrentStep() == 2) {
			targettableCoordinates = getSecondaryCoordinates();
			return getTargetCoordinatesQnO(targettableCoordinates);
		} else if (getCurrentStep() == 3) {
			targetCoordinate = targettableCoordinates.get(choice);
			setCurrentTargets(getSecondaryTargets());
			secondaryFire();
		}
		return null;
	}

	@Override
	protected void primaryFire() {
		List<DamageAndMarks> damageAndMarksList = new ArrayList<>(getStandardDamagesAndMarks());
		for (int i = 0; i < getCurrentTargets().size() - 1; i++) {
			damageAndMarksList.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		}
		dealDamageAndConclude(damageAndMarksList, getCurrentTargets());
	}

	@Override
	public void secondaryFire() {
		List<DamageAndMarks> damageAndMarksList = new ArrayList<>(getSecondaryDamagesAndMarks());
		for (int i = 0; i < getCurrentTargets().size() - 1; i++) {
			damageAndMarksList.add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));
		}
		dealDamageAndConclude(damageAndMarksList, getCurrentTargets());
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

	private List<Coordinates> getPrimaryCoordinates() {
		List<Coordinates> doorsOfNearbyRooms = getGameMap().getDoors(getOwner());
		List<Coordinates> availableDoors = new ArrayList<>(doorsOfNearbyRooms);
		for (Coordinates coordinates : doorsOfNearbyRooms) {
			List<Coordinates> roomCoordinates = getGameMap().getRoomCoordinates(coordinates);
			List<Player> playersInTheRoom = new ArrayList<>();
			for (Coordinates item : roomCoordinates) {
				playersInTheRoom.addAll(getGameMap().getPlayersFromCoordinates(item));
			}
			//If there is not player in the room, this door can't be a target.
			if (playersInTheRoom.isEmpty()) {
				availableDoors.remove(coordinates);
			}
		}
		return availableDoors;
	}

	/**
	 * Get the coordinates associated with the secondary mode of firing.
	 *
	 * @return the coordinates associated with the secondary mode of firing.
	 */
	private List<Coordinates> getSecondaryCoordinates() {
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
	boolean canSecondaryBeFired() {
		//There's at least one square next to the player with enemies in it.
		return !getSecondaryCoordinates().isEmpty();
	}

	@Override
	protected boolean canPrimaryBeActivated() {
		//There's at least one door with players in the other room
		return !getPrimaryCoordinates().isEmpty();
	}
}