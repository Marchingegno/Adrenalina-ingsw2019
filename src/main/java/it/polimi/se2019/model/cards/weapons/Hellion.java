package it.polimi.se2019.model.cards.weapons;


import com.google.gson.JsonObject;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class Hellion extends AlternateFireWeapon {
	private final int primaryFollowingDamage;
	private final int secondaryFollowingDamage;
	private final int primaryFollowingMarks;
	private final int secondaryFollowingMarks;


	public Hellion(String description, List<AmmoType> reloadPrice) {
		super("Hellion", description, reloadPrice, 1, 1, 0);
		this.secondaryDamage = 1;
		this.secondaryMarks = 2;
		this.primaryFollowingDamage = 0;
		this.secondaryFollowingDamage = 1;
		this.primaryFollowingMarks = 0;
		this.secondaryFollowingMarks = 2;
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.standardDamagesAndMarks.add(new DamageAndMarks(primaryFollowingDamage, primaryFollowingMarks));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(secondaryDamage, secondaryMarks));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(secondaryFollowingDamage, secondaryFollowingMarks));
		this.maximumSteps = 3;
		this.maximumAlternateSteps = 3;
	}

	public Hellion(JsonObject parameters) {
		super(parameters);
		this.primaryFollowingDamage = parameters.get("primaryFollowingDamage").getAsInt();
		this.secondaryFollowingDamage = parameters.get("secondaryFollowingDamage").getAsInt();
		this.primaryFollowingMarks = parameters.get("primaryFollowingMarks").getAsInt();
		this.secondaryFollowingMarks = parameters.get("secondaryFollowingMarks").getAsInt();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.standardDamagesAndMarks.add(new DamageAndMarks(primaryFollowingDamage, primaryFollowingMarks));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(secondaryDamage, secondaryMarks));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(secondaryFollowingDamage, secondaryFollowingMarks));
		this.maximumSteps = 3;
		this.maximumAlternateSteps = 3;
	}



	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3){
			target = currentTargets.get(choice);
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		if(getCurrentStep() == 2){
			currentTargets = getSecondaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3){
			target = currentTargets.get(choice);
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
		//Get the right list of damages and marks to begin firing, depending on which fire mode is chosen.
		List<DamageAndMarks> damageAndMarksList = isAlternateFireActive() ? new ArrayList<>(secondaryDamagesAndMarks) : new ArrayList<>(standardDamagesAndMarks);
		//Get the player in the same square of the target, then add the target on top of the list.
		List<Player> finalTargets = getGameMap().getPlayersFromCoordinates(getGameMap().getPlayerCoordinates(target));
		finalTargets.add(0, target);
		//Add the additional marks for the other players in the square.
		List<DamageAndMarks> finalDamageAndMarks = new ArrayList<>(damageAndMarksList);
		for (int i = 0; i < finalTargets.size() - 1; i++) {
			finalDamageAndMarks.add(damageAndMarksList.get(1));
		}
		dealDamage(finalDamageAndMarks, finalTargets);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> distantVisiblePlayers = getGameMap().getVisiblePlayers(getOwner());
		distantVisiblePlayers.removeAll(getGameMap().reachablePlayers(getOwner(), 0));
		return distantVisiblePlayers;
	}


	@Override
	public List<Player> getSecondaryTargets() {
		return getPrimaryTargets();
	}

}