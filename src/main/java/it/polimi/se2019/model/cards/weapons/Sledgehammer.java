package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class Sledgehammer extends AlternateFireWeapon {
	private List<Coordinates> enemyMovingCoordinates;

	public Sledgehammer(String description, List<AmmoType> reloadPrice) {
		super("Sledgehammer", description, reloadPrice, 0, 2, 0);
		this.maximumSteps = 3;
		this.maximumAlternateSteps = 4;
		this.secondaryDamage = 3;
		this.secondaryMarks = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(secondaryDamage, secondaryMarks));

	}

	public Sledgehammer(JsonObject parameters) {
		super(parameters);
		this.maximumSteps = 3;
		this.maximumAlternateSteps = 4;
		this.secondaryDamage = parameters.get("secondaryDamage").getAsInt();
		this.secondaryMarks = parameters.get("secondaryMarks").getAsInt();
		this.standardDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(secondaryDamage, secondaryMarks));

	}


	@Override
	QuestionContainer handlePrimaryFire(int choice) {
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
	QuestionContainer handleSecondaryFire(int choice) {
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
		dealDamage(damageAndMarksList, target);
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

	private List<Coordinates> getEnemyMovingCoordinates() {
		List<Coordinates> coordinates = getGameMap().reachablePerpendicularCoordinatesWithDistance2(getOwner());
		coordinates.add(getGameMap().getPlayerCoordinates(getOwner()));
		return coordinates;
	}



	@Override
	public void reset() {
		super.reset();
		enemyMovingCoordinates = new ArrayList<>();
	}
}