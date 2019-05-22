package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class MachineGun extends OptionalEffectsWeapon {
	private List<Player> chosenTargets;
	private static final int OPTIONAL2_EXTRA_DAMAGE = 1;

	public MachineGun(String description, List<AmmoType> reloadPrice) {
		super("Machine Gun", description, reloadPrice, 0, 1, 0);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.OPTIONAL1_DAMAGE = 1;
		this.OPTIONAL1_MARKS = 0;
		this.OPTIONAL2_DAMAGE = 1;
		this.OPTIONAL2_MARKS = 0;
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));

		this.optional1DamagesAndMarks = new ArrayList<>(standardDamagesAndMarks);
		this.optional1DamagesAndMarks.get(0).enrich(OPTIONAL1_DAMAGE, OPTIONAL1_MARKS);

		this.optional2DamagesAndMarks = new ArrayList<>(standardDamagesAndMarks);
		this.optional2DamagesAndMarks.get(1).enrich(OPTIONAL2_EXTRA_DAMAGE, 0);
		this.optional2DamagesAndMarks.add(new DamageAndMarks(OPTIONAL2_DAMAGE, OPTIONAL2_MARKS));

		this.optionalBothDamagesAndMarks = new ArrayList<>(standardDamagesAndMarks);
		this.optionalBothDamagesAndMarks.get(0).enrich(OPTIONAL1_DAMAGE, OPTIONAL1_MARKS);
		this.optionalBothDamagesAndMarks.get(1).enrich(OPTIONAL2_EXTRA_DAMAGE, 0);
		this.optionalBothDamagesAndMarks.add(new DamageAndMarks(OPTIONAL2_DAMAGE, OPTIONAL2_MARKS));

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
		}else{
			currentTargets = chosenTargets;
			primaryFire();
		}
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
			currentTargets = chosenTargets;
		}
		primaryFire();
		return null;
	}

	@Override
	public List<Player> getPrimaryTargets() {
		//All players except the ones already chosen
		List<Player> visibleExceptChosen = getGameMap().getVisiblePlayers(getOwner());
		visibleExceptChosen.removeAll(chosenTargets);
		return visibleExceptChosen;
	}

	@Override
	public void reset() {
		super.reset();
		chosenTargets = new ArrayList<>();
	}
}