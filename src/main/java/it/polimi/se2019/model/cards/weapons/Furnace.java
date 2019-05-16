package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Furnace extends AlternateFire {

	private List<Coordinates> targettableCoordinates;
	private Coordinates targetCoordinate;

	public Furnace(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.PRIMARY_DAMAGE = 1;
		this.PRIMARY_MARKS = 0;
		this.SECONDARY_DAMAGE = 1;
		this.SECONDARY_MARKS = 1;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		this.maximumAlternateSteps = 3;
		this.maximumSteps = 3;
	}


	@Override
	Pair handlePrimaryFire(int choice) {
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
	Pair handleSecondaryFire(int choice) {
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
			damageAndMarksList.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		}
		dealDamage(currentTargets, damageAndMarksList);
	}

	public void secondaryFire() {
		List<DamageAndMarks> damageAndMarksList = new ArrayList<>(getSecondaryDamagesAndMarks());
		for (int i = 0; i < currentTargets.size() - 1; i++) {
			damageAndMarksList.add(new DamageAndMarks(SECONDARY_DAMAGE,SECONDARY_MARKS));
		}
		dealDamage(currentTargets, damageAndMarksList);
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
		return getGameMap().getDoors(getOwner());
	}

	private List<Coordinates> getSecondaryCoordinates(){
		List<Coordinates> oneMoveCoordinates = getGameMap().reachableCoordinates(getOwner(), 1);
		oneMoveCoordinates.remove(getGameMap().getPlayerCoordinates(getOwner()));
		targettableCoordinates = oneMoveCoordinates;
		return targettableCoordinates;
	}

}