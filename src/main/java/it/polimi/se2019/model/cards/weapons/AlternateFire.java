package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import static java.lang.Boolean.FALSE;

/**
 * Weapons with an alternate fire method. The player can choose one of the two firing modes the weapon has.
 * @author Marchingegno
 */
public abstract class AlternateFire extends WeaponCard {
	int maximumAlternateSteps;
	List<DamageAndMarks> secondaryDamagesAndMarks;
	List<AmmoType> secondaryCost;
	boolean alternateFireActive;
	static final int INITIAL_CHOICES = 2;

	public AlternateFire(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		reset();
	}

	public AlternateFire(String description, List<AmmoType> reloadPrice, List<AmmoType> secondaryCost) {
		this(description, reloadPrice);
		this.secondaryCost = secondaryCost;
		reset();
	}

	List<DamageAndMarks> getSecondaryDamagesAndMarks() {
		return secondaryDamagesAndMarks;
	}

	@Override
	public void registerChoice(int choice) {
		if (choice == 1)
			alternateFireActive = true;
	}

	@Override
	public boolean doneFiring() {
		if (isAlternateFireActive()){
			return getCurrentStep() == getMaximumAlternateSteps();
		}
		else return super.doneFiring();
	}

	abstract Pair handleSecondaryFire(int choice);

	@Override
	public Pair handleFire(int choice) {
		if (getCurrentStep() == 1) {
			return askingPair();
		} else if(getCurrentStep() == 2){
			registerChoice(choice);
		}
		return null;
	}

	@Override
	public Pair askingPair() {
		List<String> options = new ArrayList<>();
		options.add("Standard fire.");
		options.add("Alternate fire.");
		return new Pair<>("Which fire mode do you want to use?", options);
	}

	/**
	 * Secondary mode of firing.
	 */
	public abstract void secondaryFire();

	/**
	 * Get the targets of the secondary mode of fire for this weapon.
	 * @return the targettable players.
	 */
	public abstract List<Player> getSecondaryTargets();


	void secondaryReset(){
		this.alternateFireActive = FALSE;
	}

	@Override
	public void reset() {
		super.reset();
		secondaryReset();
	}


	int getMaximumAlternateSteps() {
		return maximumAlternateSteps;
	}

	boolean isAlternateFireActive() {
		return alternateFireActive;
	}
}