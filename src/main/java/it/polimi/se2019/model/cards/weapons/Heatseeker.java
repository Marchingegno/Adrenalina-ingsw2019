package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Heatseeker extends WeaponCard {

	public Heatseeker(String description, List<AmmoType> reloadPrice) {
		super("Heatseeker", description, reloadPrice);
		this.PRIMARY_DAMAGE = 3;
		this.PRIMARY_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.maximumSteps = 2;
		reset();
	}

	@Override
	public Pair handleFire(int choice) {
		incrementStep();
		return handlePrimaryFire(choice);
	}

	@Override
	public void primaryFire() {
		dealDamage(standardDamagesAndMarks, target);
	}


	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> nonVisiblePlayers = getAllPlayers();
		nonVisiblePlayers.removeAll(getGameMap().reachableAndVisiblePlayers(getOwner(), 99));
		return nonVisiblePlayers;
	}

	@Override
	Pair handlePrimaryFire(int choice) {
		if(getCurrentStep() == 1){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 2){
			target = currentTargets.get(choice);
			primaryFire();
		}
		return null;
	}
}