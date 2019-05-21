package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Railgun extends AlternateFireWeapon {
	private CardinalDirection chosenDirection;
	private Player secondTarget;

	public Railgun(String description, List<AmmoType> reloadPrice) {
		super("Railgun", description, reloadPrice);
		this.PRIMARY_DAMAGE = 3;
		this.PRIMARY_MARKS = 0;
		this.SECONDARY_DAMAGE = 2;
		this.SECONDARY_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		this.maximumSteps = 4;
	}

	@Override
	Pair handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2){
			return getCardinalQnO();
		}
		else if(getCurrentStep() == 3){
			chosenDirection = CardinalDirection.values()[choice];
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 4){
			target = currentTargets.get(choice);
			primaryFire();
		}
		return null;
	}

	@Override
	Pair handleSecondaryFire(int choice) {
		return null;
	}

	@Override
	public void primaryFire() {
		dealDamage(standardDamagesAndMarks, target);
	}

	public void secondaryFire() {
		dealDamage(secondaryDamagesAndMarks, target, secondTarget);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return null;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return null;
	}


}