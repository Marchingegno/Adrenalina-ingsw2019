package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class ZX_2 extends AlternateFire {
	private Player primaryTarget;
	private List<Player> secondaryTargets;

	public ZX_2(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.PRIMARY_DAMAGE = 1;
		this.PRIMARY_MARKS = 2;
		this.SECONDARY_DAMAGE = 0;
		this.SECONDARY_MARKS = 1;
		this.standardDamagesAndMarks = new ArrayList<>();
		standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		}
		this.maximumSteps = 3;
	}


	@Override
	Pair handlePrimaryFire(int choice) {
		if (getCurrentStep() == 2){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3){
			primaryTarget = currentTargets.get(choice);
			primaryFire();
		}
		return null;
	}

	@Override
	Pair handleSecondaryFire(int choice) {
		switch (getCurrentStep()){
			case 2:
				currentTargets = getSecondaryTargets();
				return getTargetPlayersQnO(currentTargets);
			case 3:
				secondaryTargets = new ArrayList<>();
				secondaryTargets.add(currentTargets.remove(choice));
				if (currentTargets.isEmpty()){
					terminateSecondaryFire();
					return null;
				}
				return getTargetPlayersQnO(currentTargets);
			case 4:
				if (currentTargets.isEmpty()){
					terminateSecondaryFire();
					return null;
				}
				secondaryTargets.add(currentTargets.remove(choice));
				return getTargetPlayersQnO(currentTargets);
			case 5:
				if (currentTargets.isEmpty()){
					terminateSecondaryFire();
					return null;
				}
				secondaryTargets.add(currentTargets.remove(choice));
				secondaryFire();
		}
		return null;
	}

	public void primaryFire() {
		List<Player> targetPlayer = new ArrayList<>();
		targetPlayer.add(primaryTarget);
		dealDamage(targetPlayer, standardDamagesAndMarks);
	}

	public void secondaryFire() {
		dealDamage(secondaryTargets, secondaryDamagesAndMarks);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return getGameMap().getVisiblePlayers(getOwner());
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return getPrimaryTargets();
	}

	/**
	 * Instantly fires the weapon. Called if there are no more target to choose from in secondary fire mode.
	 */
	private void terminateSecondaryFire(){
		while(getCurrentStep() < getMaximumSteps()){
			incrementStep();
		}
		secondaryFire();
	}

}