package it.polimi.se2019.model.cards.weapons;


import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TractorBeam extends AlternateFire {

	private List<Coordinates> enemyRelocationCoordinates;

	public TractorBeam(String description, List<AmmoType> reloadPrice) {
		super("Tractor Beam", description, reloadPrice);
		this.PRIMARY_DAMAGE = 1;
		this.PRIMARY_MARKS = 0;
		this.SECONDARY_DAMAGE = 3;
		this.SECONDARY_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		this.maximumAlternateSteps = 3;
		this.maximumSteps = 3;

	}


	@Override
	Pair handlePrimaryFire(int choice) {
		switch (getCurrentStep()){
			case 2:
				currentTargets = getPrimaryTargets();
				if(currentTargets == null)
					Utils.logError("currentTargets is null! See TractorBeam", new IllegalStateException());
				return getTargetPlayersQnO(currentTargets);
			case 3:
				target = currentTargets.get(choice);
				enemyRelocationCoordinates = getGameMap().getVisibleCoordinates(getOwner());
				return getMovingTargetEnemyCoordinatesQnO(target, enemyRelocationCoordinates);
			case 4:
				getGameMap().movePlayerTo(target, enemyRelocationCoordinates.get(choice));
				primaryFire();
				break;
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
				target = currentTargets.get(choice);
				getGameMap().movePlayerTo(target, getGameMap().getPlayerCoordinates(getOwner()));
				secondaryFire();
				break;
		}
		return null;
	}

	@Override
	public void primaryFire() {
		dealDamage(target, standardDamagesAndMarks);
	}

	@Override
	public void secondaryFire() {
		dealDamage(target, secondaryDamagesAndMarks);
	}


	@Override
	public List<Player> getPrimaryTargets() {
		List<Coordinates> visibleCoordinates = getGameMap().getVisibleCoordinates(getOwner());
		List<Player> players = getAllPlayers();
		List<Player> targettablePlayers = new ArrayList<>();
		for (Player player: players) {
			List<Coordinates> intersectionCoordinates = getGameMap().reachableCoordinates(player, 2);
			intersectionCoordinates.retainAll(visibleCoordinates);
			if(!intersectionCoordinates.isEmpty()){
				targettablePlayers.add(player);
			}
		}
		return targettablePlayers.isEmpty() ? null : targettablePlayers;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return getGameMap().reachablePlayers(getOwner(), 2);
	}
}