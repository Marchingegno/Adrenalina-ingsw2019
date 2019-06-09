package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Weapons with at most two optional effects. An optional effect is an enhancement the player can choose to give at the
 * primary fire mode. Some optional effect have an additional ammo cost.
 * @author  Marchingegno
 */
public abstract class OptionalEffectsWeapon extends WeaponCard {
	private boolean[] optionalEffectsActive;
	boolean[] hasOptionalEffects;
	private List<AmmoType> optionalPrices;
	int optional1Damage;
	int optional1Marks;
	int optional2Damage;
	int optional2Marks;
	List<DamageAndMarks> optional1DamagesAndMarks;
	List<DamageAndMarks> optional2DamagesAndMarks;
	List<DamageAndMarks> optionalBothDamagesAndMarks;

	public OptionalEffectsWeapon(JsonObject parameters) {
		super(parameters);
		hasOptionalEffects = new boolean[2];
		hasOptionalEffects[0] = true;
		hasOptionalEffects[1] = true;
		optionalEffectsActive = new boolean[2];
		optionalPrices = new ArrayList<>();
		optional1DamagesAndMarks = new ArrayList<>();
		optional2DamagesAndMarks = new ArrayList<>();
		optionalBothDamagesAndMarks = new ArrayList<>();
		this.optional1Damage = parameters.get("optional1Damage").getAsInt();
		this.optional1Marks = parameters.get("optional1Marks").getAsInt();
		this.optional2Damage = parameters.get("optional2Damage").getAsInt();
		this.optional2Marks = parameters.get("optional2Marks").getAsInt();
		this.optionalPrices = new ArrayList<>();
		for (JsonElement price : parameters.getAsJsonArray("optionalPrices")) {
			if (price.getAsString().equals("NONE"))
				this.optionalPrices.add(null);
			else
				this.optionalPrices.add(AmmoType.valueOf(price.getAsString()));
		}
	}

	@Override
	public QuestionContainer initialQuestion() {
		String question = "Which optional effect do you want to activate?";
		List<String> options = new ArrayList<>();
		options.add("No optional effects.");
		for (int i = 0; i < optionalEffectsActive.length; i++) {
			if (canAddThisOptionalEffect(i) && hasOptionalEffects[i]) {
				options.add("Optional effect "+i+".");
			}
		}
		//the following is hardcoded.
		if (canAddBothOptionalEffects() && hasOptionalEffects[1]) {
			options.add("Optional effect 1 + Optional effect 2.");
		}
		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	protected void registerChoice(int choice) {
		switch (choice){
			case 0:
				//No optional effects.
				break;
			case 1:
				//If the first optional effect can't be added, then choice 1 is the second optional effect
				if (canAddThisOptionalEffect(1)) {
					optionalEffectsActive[0] = true;
				}
				else{
					optionalEffectsActive[1] = true;
				}
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
	public QuestionContainer doActivationStep(int choice) {
		incrementCurrentStep();
		Utils.logWeapon(this.getCardName() + ": executing main method with currentStep " + getCurrentStep() + " and choice " + choice);
		if(getCurrentStep() == 1){
			return initialQuestion();
		}
		else if(getCurrentStep() == 2){
			registerChoice(choice);
		}
		return handlePrimaryFire(choice);
	}

	protected QuestionContainer handleOptionalEffect1(int choice){
		return null;
	}

	protected QuestionContainer handleOptionalEffect2(int choice){
		return null;
	}

	protected QuestionContainer handleBothOptionalEffects(int choice) {
		return null;
	}

	@Override
	public void primaryFire() {
		if(isBothOptionalActive()){
			dealDamageAndConclude(optionalBothDamagesAndMarks, currentTargets);
		}
		else if(isOptionalActive(1)){
			dealDamageAndConclude(optional1DamagesAndMarks, currentTargets);
		}
		else if(isOptionalActive(2)){
			dealDamageAndConclude(optional2DamagesAndMarks, currentTargets);
		}
		else{
			dealDamageAndConclude(standardDamagesAndMarks, currentTargets);
		}
	}



	public void optional1Fire(){
	}

	public void optional2Fire(){

	}

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

	public AmmoType getCostOfOptionalEffect(int numberOfEffect){
		return optionalPrices.get(numberOfEffect - 1);

	}

	protected boolean isOptionalActive(int optionalIndex) {
		return optionalEffectsActive[optionalIndex - 1];
	}

	protected boolean isBothOptionalActive(){
		return optionalEffectsActive[0] && optionalEffectsActive[1];
	}

	protected boolean canAddThisOptionalEffect(int numberOfEffect) {
		if (numberOfEffect == 1)
			return canAddOptionalEffect1();
		else
			return canAddOptionalEffect2();
	}

	//To override
	protected boolean canAddOptionalEffect1() {
		return getOwner().hasEnoughAmmo(Arrays.asList(getCostOfOptionalEffect(1)));
	}

	//To override
	protected boolean canAddOptionalEffect2() {
		if (!hasOptionalEffects[1]) return false;
		return getOwner().hasEnoughAmmo(Arrays.asList(getCostOfOptionalEffect(2)));
	}

	protected boolean canAddBothOptionalEffects() {
		if (!hasOptionalEffects[1]) return false;
		return getOwner().hasEnoughAmmo(optionalPrices) && canAddOptionalEffect1() && canAddOptionalEffect2();
	}

}