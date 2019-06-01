package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MachineGun extends OptionalEffectsWeapon {
	private List<Player> chosenTargets;
	private static final int OPTIONAL2_EXTRA_DAMAGE = 1;

	public MachineGun(JsonObject parameters) {
		super(parameters);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.optional1Damage = parameters.get("optional1Damage").getAsInt();
		this.optional1Marks = parameters.get("optional1Marks").getAsInt();
		this.optional2Damage = parameters.get("optional2Damage").getAsInt();
		this.optional2Marks = parameters.get("optional2Marks").getAsInt();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));

		this.optional1DamagesAndMarks = new ArrayList<>(standardDamagesAndMarks);
		this.optional1DamagesAndMarks.get(0).enrich(optional1Damage, optional1Marks);

		this.optional2DamagesAndMarks = new ArrayList<>(standardDamagesAndMarks);
		this.optional2DamagesAndMarks.get(1).enrich(OPTIONAL2_EXTRA_DAMAGE, 0);
		this.optional2DamagesAndMarks.add(new DamageAndMarks(optional2Damage, optional2Marks));

		this.optionalBothDamagesAndMarks = new ArrayList<>(standardDamagesAndMarks);
		this.optionalBothDamagesAndMarks.get(0).enrich(optional1Damage, optional1Marks);
		this.optionalBothDamagesAndMarks.get(1).enrich(OPTIONAL2_EXTRA_DAMAGE, 0);
		this.optionalBothDamagesAndMarks.add(new DamageAndMarks(optional2Damage, optional2Marks));

	}


	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2){
			chosenTargets = new ArrayList<>();
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		if(getCurrentStep() == 3){
			//Chosen first target.
			chosenTargets.add(currentTargets.get(choice));
			currentTargets = getPrimaryTargets();
			if (currentTargets.isEmpty()) {
				//Skip step 4
				//Recall this method with refusal choice of an empty array.
				Utils.logWeapon("Skip of MachineGun");
				return doActivationStep(0);
			}
			return getTargetPlayersAndRefusalQnO(currentTargets);
		}
		if(getCurrentStep() == 4) {
			//Chosen second target.
			if (!isThisChoiceRefusal(currentTargets, choice)) {
				chosenTargets.add(currentTargets.get(choice));
			}
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
		if (chosenTargets != null && !chosenTargets.isEmpty()) {
			visibleExceptChosen.removeAll(chosenTargets);
		}
		return visibleExceptChosen;
	}

	@Override
	public void reset() {
		super.reset();
		chosenTargets = new ArrayList<>();
	}

	@Override
	protected boolean canAddOptionalEffect2() {
		//There's at least 3 visible players.
		return getPrimaryTargets().size() >= 3;
	}

}