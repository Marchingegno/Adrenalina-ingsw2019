package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public final class Heatseeker extends WeaponCard {

	private Player targetPlayer;

	public Heatseeker(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
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
		List<Player> nonVisiblePlayers = getAllPlayers();
		nonVisiblePlayers.removeAll(getGameMap().reachableAndVisiblePlayers(getOwner(), 99));
		return nonVisiblePlayers;
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