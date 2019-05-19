package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.utils.Pair;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Weapons with at most two optional effects. An optional effect is an enhancement the player can choose to give at the
 * primary fire mode. Some optional effect have an additional ammo cost.
 * @author  Marchingegno
 */
public abstract class OptionalEffect extends WeaponCard {
	private boolean[] optionalEffectsActive;
	private boolean[] canAddOptionalEffect; //Index 2 is optional 1 + 2
	private List<List<AmmoType>> optionalCosts;


	public OptionalEffect(String weaponName, String description, List<AmmoType> reloadPrice) {
		super(weaponName, description, reloadPrice);
		optionalEffectsActive = new boolean[2];
		canAddOptionalEffect = new boolean[3];
		reset();
	}

	@Override
	public Pair askingPair() {
		String question = "Which optional effect do you want to activate?";
		checkOptionalEffects();
		List<String> options = new ArrayList<>();
		options.add("No optional effects.");
		for (int i = 0; i < optionalEffectsActive.length; i++) {
			if(canAddOptionalEffect[i]){
				options.add("Optional effect "+i+".");
			}
		}
		//the following is hardcoded.
		if(canAddOptionalEffect[2]){
			options.add("Optional effect 1 + Optional effect 2");
		}
		return new Pair<>(question, options);
	}

	private void checkOptionalEffects() {
		for (int i = 0; i < canAddOptionalEffect.length; i++) {
			canAddOptionalEffect[i] = getOwner().hasEnoughAmmo(optionalCosts.get(i));
		}
		//the following is hardcoded.
		List<AmmoType> optionalCost12 = optionalCosts.get(0);
		optionalCost12.addAll(optionalCosts.get(1));
	}

	protected void registerChoice(int choice) {
		switch (choice){
			case 0:
				//No optional effects.
				break;
			case 1:
				optionalEffectsActive[0] = true;
				break;
			case 2:
				optionalEffectsActive[1] = true;
				break;
			case 3:
				optionalEffectsActive[0] = true;
				optionalEffectsActive[1] = true;
				break;
			default:
				Utils.logError("There is no options with choice number " +choice, new IllegalArgumentException());
		}
	}

	@Override
	public Pair handleFire(int choice) {
		return null;
	}

	@Override
	public void primaryFire() {
	}


	public abstract void optionalEffect1();

	public abstract void optionalEffect2();

	void optionalReset(){
		for (int i = 0; i < optionalEffectsActive.length; i++) {
			optionalEffectsActive[i] = false;
		}
	}

	@Override
	public void reset() {
		super.reset();
		optionalReset();

	}

	@Override
	public boolean doneFiring() {
		//TODO Implement
		return super.doneFiring();
	}

	protected boolean isOptionalActive(int optionalIndex) {
		return optionalEffectsActive[optionalIndex - 1];
	}

}