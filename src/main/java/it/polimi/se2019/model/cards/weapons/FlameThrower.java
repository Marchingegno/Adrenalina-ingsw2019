package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public final class FlameThrower extends AlternateFire {
	private final int PRIMARY_DAMAGE = 1;
	private final int PRIMARY_MARKS = 0;
	private final int SECONDARY_DAMAGE = 2;
	private final int SECONDARY_MARKS = 0;
	private ArrayList<DamageAndMarks> secondaryDamagesAndMarks;

	public FlameThrower(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.standardDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		secondaryDamagesAndMarks = new ArrayList<>();
		secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		secondaryDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
	}


	/**
	 * Handles interaction with flags array.
	 *
	 * @param choice
	 * @return
	 */
	@Override
	public Pair handleFire(int choice) {
		incrementStep();
		if(getCurrentStep() == 1){
			return askingPair();
		}

		if(getCurrentStep() == 2){
			registerChoice(choice);
		}

		if (isAlternateFireActive()){
			return handleSecondaryFire(choice);
		}
		else{
			if(getCurrentStep() == 3){
				return new Pair(4, "Select a direction to fire. 1. NORD 2.EAST 3.SUD 4.WEST");
			}
			else if(getCurrentStep() == 4){
				primaryFire();
			}
		}
		return null;
	}

	public void primaryFire() {
	}

	/**
	 * Get the targets of the primary mode of fire for this weapon.
	 *
	 * @return the targettable players.
	 */
	@Override
	public List<Player> getPrimaryTargets() {
		return null;
	}

	@Override
	Pair handleSecondaryFire(int choice) {
		if(getCurrentStep() == 3){
			return new Pair(4, "Select a direction to fire. 1. NORD 2.EAST 3.SUD 4.WEST");
		}
		else if(getCurrentStep() == 4){
			//Fai qualcosa
		}
		else if(getCurrentStep() == 4){
			primaryFire();
		}
		return null;
	}

	public void secondaryFire() {

	}

	/**
	 * Get the targets of the secondary mode of fire for this weapon.
	 *
	 * @return the targettable players.
	 */
	@Override
	public List<Player> getSecondaryTargets() {
		return null;
	}


}