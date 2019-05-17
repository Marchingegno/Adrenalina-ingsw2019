package it.polimi.se2019.model.cards.weapons;


import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Hellion extends AlternateFire {
	private Player target;
	private int PRIMARY_FOLLOWING_DAMAGE;
	private int SECONDARY_FOLLOWING_DAMAGE;
	private int PRIMARY_FOLLOWING_MARKS;
	private int SECONDARY_FOLLOWING_MARKS;


	public Hellion(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.PRIMARY_DAMAGE = 1;
		this.PRIMARY_MARKS = 1;
		this.SECONDARY_DAMAGE = 1;
		this.SECONDARY_MARKS = 2;
		this.PRIMARY_FOLLOWING_DAMAGE = 0;
		this.PRIMARY_FOLLOWING_MARKS = 1;
		this.SECONDARY_FOLLOWING_DAMAGE = 0;
		this.SECONDARY_FOLLOWING_MARKS = 2;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_FOLLOWING_DAMAGE, PRIMARY_FOLLOWING_MARKS));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_FOLLOWING_DAMAGE, SECONDARY_FOLLOWING_MARKS));
		this.maximumSteps = 3;
		this.maximumAlternateSteps = 3;
	}



	@Override
	Pair handlePrimaryFire(int choice) {
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
	Pair handleSecondaryFire(int choice) {
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
		dealDamage(finalTargets, finalDamageAndMarks);
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