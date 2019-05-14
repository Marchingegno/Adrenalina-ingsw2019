package it.polimi.se2019.model.cards.weapons;


import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;
import java.util.ArrayList;
import java.util.List;

public final class TractorBeam extends AlternateFire {

	public TractorBeam(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.PRIMARY_DAMAGE = 1;
		this.PRIMARY_MARKS = 0;
		this.SECONDARY_DAMAGE = 3;
		this.SECONDARY_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
//		this.maximumAlternateSteps = ;
//		this.maximumSteps = ;

	}


	@Override
	Pair handleSecondaryFire(int choice) {
		return null;
	}

	/**
	 * Secondary mode of firing.
	 */
	@Override
	public void secondaryFire() {

	}

	/**
	 * Get the targets of the secondary mode of fire for this weapon.
	 *
	 * @return the targettable players.
	 */
	@Override
	public List<Player> getSecondaryTargets() {
		return getGameMap().reachablePlayers(getOwner(), 2);
	}

	/**
	 * Primary method of firing of the weapon. It interacts with the view and collects targeted players for its mode.
	 */
	@Override
	public void primaryFire() {

	}

	/**
	 * Get the targets of the primary mode of fire for this weapon.
	 *
	 * @return the targettable players.
	 */
	@Override
	public List<Player> getPrimaryTargets() {
		getGameMap()
	}

	@Override
	Pair handlePrimaryFire(int choice) {
		return null;
	}
}