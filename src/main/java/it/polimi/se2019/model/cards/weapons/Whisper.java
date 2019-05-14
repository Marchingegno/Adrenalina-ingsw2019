package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public final class Whisper extends WeaponCard {

	private Player targetPlayer;

	public Whisper(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.PRIMARY_DAMAGE = 3;
		this.PRIMARY_MARKS = 1;
		this.standardDamagesAndMarks = new ArrayList<>();
		standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));

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
		return handlePrimaryFire(choice);
	}

	/**
	 * Primary method of firing of the weapon. It interacts with the view and collects targeted players for its mode.
	 */
	@Override
	public void primaryFire() {
		List<Player> target = new ArrayList<>();
		target.add(targetPlayer);
		dealDamage(target, standardDamagesAndMarks);
	}

	/**
	 * Get the targets of the primary mode of fire for this weapon.
	 *
	 * @return the targettable players.
	 */
	@Override
	public List<Player> getPrimaryTargets() {
		//TODO: implement with getVisiblePlayers
		return null;
	}

	/**
	 * Advances the weapon.
	 * This will be called if currentStep is at least 2.
	 *
	 * @param choice the choice of the player.
	 * @return the asking pair.
	 */
	@Override
	Pair handlePrimaryFire(int choice) {
		if(getCurrentStep() == 1){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 2){
			targetPlayer = currentTargets.get(choice);
			primaryFire();
		}
		return null;
	}
}