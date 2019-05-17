package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Sledgehammer extends AlternateFire {
	private Player target;
	List<Coordinates> enemyMovingCoordinates;

	public Sledgehammer(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.maximumSteps = 3;

	}


	@Override
	Pair handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3){
			this.target = currentTargets.get(choice);
			primaryFire();
		}
		return null;
	}

	@Override
	Pair handleSecondaryFire(int choice) {
		if(getCurrentStep() == 2){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3){
			this.target = currentTargets.get(choice);
			enemyMovingCoordinates = getEnemyMovingCoordinates();
			return getMovingTargetEnemyCoordinatesQnO(target, enemyMovingCoordinates);
		}
		else if(getCurrentStep() == 4){
			relocateEnemy(target, enemyMovingCoordinates.get(choice));
			secondaryFire();
		}
		return null;
	}

	public void primaryFire() {
		unifiedFire();
	}

	private void unifiedFire(){
		List<DamageAndMarks> damageAndMarksList = isAlternateFireActive() ? secondaryDamagesAndMarks : standardDamagesAndMarks;
		List<Player> targetToShoot = new ArrayList<>();
		targetToShoot.add(target);
		dealDamage(targetToShoot, damageAndMarksList);
	}

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

	private List<Coordinates> getEnemyMovingCoordinates(){
		//TODO: implement
		return null;
	}


}