package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class PowerGlove extends AlternateFireWeapon {
	private CardinalDirection chosenDirection;

	public PowerGlove(String description, List<AmmoType> reloadPrice) {
		super("Power Glove", description, reloadPrice);
		this.PRIMARY_DAMAGE = 1;
		this.PRIMARY_MARKS = 2;
		this.SECONDARY_DAMAGE = 2;
		this.SECONDARY_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		this.maximumSteps = 3;
	}


	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		if(getCurrentStep() == 3){
			target = currentTargets.get(choice);
			relocateOwner(getGameMap().getPlayerCoordinates(target));
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		return null;
	}

	public void primaryFire() {
		dealDamage(standardDamagesAndMarks, target);
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