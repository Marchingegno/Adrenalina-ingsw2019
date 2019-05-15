package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Shotgun extends AlternateFire {
	Player enemy;

	public Shotgun(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.PRIMARY_DAMAGE = 3;
		this.PRIMARY_MARKS = 0;
		this.SECONDARY_DAMAGE = 2;
		this.SECONDARY_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE,PRIMARY_MARKS));
		//this.maximumSteps = 3; //TODO: Not true. Change this.
		this.maximumAlternateSteps = 3;
	}


	@Override
	Pair handlePrimaryFire(int choice) {
		//TODO:Implement moving of the enemy.
		if(getCurrentStep() == 2){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3){
			this.enemy = currentTargets.get(choice);
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
		//TODO: Implement with methods in GameMap
		return null;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		//TODO:Implement
		List<Coordinates> adjacentCoordinates = getGameMap().
	}


}