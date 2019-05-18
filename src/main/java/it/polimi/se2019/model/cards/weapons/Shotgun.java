package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Shotgun extends AlternateFire {
	private Player enemy;
	private List<Coordinates> listEnemyMoveCoordinates;

	public Shotgun(String description, List<AmmoType> reloadPrice) {
		super("Shotgun", description, reloadPrice);
		this.PRIMARY_DAMAGE = 3;
		this.PRIMARY_MARKS = 0;
		this.SECONDARY_DAMAGE = 2;
		this.SECONDARY_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE,PRIMARY_MARKS));
		this.maximumSteps = 4;
		this.maximumAlternateSteps = 3;
	}


	@Override
	Pair handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3){
			this.enemy = currentTargets.get(choice);
			this.listEnemyMoveCoordinates = getEnemyMovingCoordinates();
			return getMovingTargetEnemyCoordinatesQnO(enemy, listEnemyMoveCoordinates);
		}
		else if(getCurrentStep() == 4){
			relocateEnemy(enemy, listEnemyMoveCoordinates.get(choice));
			primaryFire();
		}
		return null;
	}

	@Override
	Pair handleSecondaryFire(int choice) {
		if(getCurrentStep() == 2){
			currentTargets = getSecondaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3){
			this.enemy = currentTargets.get(choice);
			secondaryFire();
		}
		return null;
	}

	public void primaryFire() {
		unifiedFire();
	}

	public void secondaryFire() {
		unifiedFire();
	}

	private void unifiedFire(){
		List<DamageAndMarks> chosenDamagesAndMarks = isAlternateFireActive() ? secondaryDamagesAndMarks : standardDamagesAndMarks;
		List<Player> targetList = new ArrayList<>();
		targetList.add(enemy);
		dealDamage(targetList, chosenDamagesAndMarks);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		Coordinates ownCoordinates = getGameMap().getPlayerCoordinates(getOwner());
		List<Player> enemyPlayersInMySquare = getGameMap().getPlayersFromCoordinates(ownCoordinates);
		enemyPlayersInMySquare.remove(getOwner());
		return enemyPlayersInMySquare;
	}

	private List<Coordinates> getEnemyMovingCoordinates(){
		return getGameMap().reachableCoordinates(getOwner(), 1);
	}

	@Override
	public List<Player> getSecondaryTargets() {
		List<Coordinates> listAdjacentCoordinates = getGameMap().reachableCoordinates(getOwner(), 1);
		listAdjacentCoordinates.remove(getGameMap().getPlayerCoordinates(getOwner()));
		List<Player> adjacentPlayers = new ArrayList<>();
		for (Coordinates adjacentCoordinate: listAdjacentCoordinates) {
			adjacentPlayers.addAll(getGameMap().getPlayersFromCoordinates(adjacentCoordinate));
		}
		return adjacentPlayers;
	}

}