package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class PowerGlove extends AlternateFire {

	public PowerGlove(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.PRIMARY_DAMAGE = 1;
		this.PRIMARY_MARKS = 2;
		this.SECONDARY_DAMAGE = 2;
		this.SECONDARY_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
	}


	@Override
	Pair handlePrimaryFire(int choice) {
		return null;
	}

	@Override
	Pair handleSecondaryFire(int choice) {
		return null;
	}

	public void primaryFire() {
	}

	public void secondaryFire() {
	}

	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> enemiesOneMoveAway = getGameMap().reachablePlayers(getOwner(), 1);
		enemiesOneMoveAway.removeAll(getGameMap().getPlayersFromCoordinates(getGameMap().getPlayerCoordinates(getOwner())));
		return enemiesOneMoveAway;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return null;
	}

}