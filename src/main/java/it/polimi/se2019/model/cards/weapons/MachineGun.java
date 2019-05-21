package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class MachineGun extends OptionalEffectsWeapon {
	List<Player> chosenTargets;
	private int OPTIONAL2_EXTRA_DAMAGE;

	public MachineGun(String description, List<AmmoType> reloadPrice) {
		super("Machine Gun", description, reloadPrice);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.PRIMARY_DAMAGE = 1;
		this.PRIMARY_MARKS = 0;
		this.OPTIONAL1_DAMAGE = 1;
		this.OPTIONAL1_MARKS = 0;
		this.OPTIONAL2_DAMAGE = 1;
		this.OPTIONAL2_MARKS = 0;
		this.OPTIONAL2_EXTRA_DAMAGE = 1;
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));

		this.optional1DamagesAndMarks = new ArrayList<>(standardDamagesAndMarks);
		this.optional1DamagesAndMarks.get(0).enrich(OPTIONAL1_DAMAGE, OPTIONAL1_MARKS);

		this.optional2DamagesAndMarks = new ArrayList<>(standardDamagesAndMarks);
		this.optional2DamagesAndMarks.get(1).enrich(OPTIONAL2_EXTRA_DAMAGE, 0);
		this.optional2DamagesAndMarks.add(new DamageAndMarks(OPTIONAL2_DAMAGE, OPTIONAL2_MARKS));

		this.optional1and2DamagesAndMarks = new ArrayList<>(standardDamagesAndMarks);
		this.optional1and2DamagesAndMarks.get(0).enrich(OPTIONAL1_DAMAGE, OPTIONAL1_MARKS);
		this.optional1and2DamagesAndMarks.get(1).enrich(OPTIONAL2_EXTRA_DAMAGE, 0);
		this.optional1and2DamagesAndMarks.add(new DamageAndMarks(OPTIONAL2_DAMAGE, OPTIONAL2_MARKS));

	}


	public void primaryFire() {
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		if(getCurrentStep() == 3){
			//Chosen first target.
			chosenTargets = new ArrayList<>();
			chosenTargets.add(currentTargets.get(choice));
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		if(getCurrentStep() == 4) {
			//Chosen second target.
			chosenTargets.add(currentTargets.get(choice));
		}

		if(isOptionalActive(2)){
			return handleOptionalEffect2(choice);
		}

		primaryFire();

		return null;
	}

	@Override
	protected QuestionContainer handleOptionalEffect2(int choice) {
		if(getCurrentStep() == 4){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 5){
			chosenTargets.add(currentTargets.get(choice));
		}
		primaryFire();
		return null;
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return null;
	}

	@Override
	public void optionalEffect1() {

	}

	@Override
	public void optionalEffect2() {

	}
}