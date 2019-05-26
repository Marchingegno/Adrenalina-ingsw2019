package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

/**
 * Weapons with an alternate fire method. The player can choose one of the two firing modes the weapon has.
 * @author Marchingegno
 */
public abstract class AlternateFireWeapon extends WeaponCard {
	int maximumAlternateSteps;
	List<DamageAndMarks> secondaryDamagesAndMarks;
	List<AmmoType> secondaryPrice;
	boolean alternateFireActive;
	int secondaryDamage;
	int secondaryMarks;

	public AlternateFireWeapon(JsonObject parameters) {
		super(parameters);
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamage = parameters.get("secondaryDamage").getAsInt();
		this.secondaryMarks = parameters.get("secondaryMarks").getAsInt();
		this.secondaryPrice = new ArrayList<>();

		for (JsonElement price : parameters.getAsJsonArray("secondaryPrice")) {
			this.secondaryPrice.add(AmmoType.valueOf(price.getAsString()));
		}
	}

	List<DamageAndMarks> getSecondaryDamagesAndMarks() {
		return secondaryDamagesAndMarks;
	}

	private void registerChoice(int choice) {
		if (getPrimaryTargets().isEmpty() || choice == 1) {
			alternateFireActive = true;
		}
	}

	/**
	 * Handles the secondary mode of fire of weapon.
	 * This will be called if currentStep is at least 2.
	 *
	 * @param choice the choice of the player.
	 * @return the "Question" Pair.
	 */
	abstract QuestionContainer handleSecondaryFire(int choice);

	@Override
	public QuestionContainer doActivationStep(int choice) {
		incrementCurrentStep();
		if (getCurrentStep() == 1) {
			return initialQuestion();
		} else if(getCurrentStep() == 2){
			registerChoice(choice);
		}
		if (isAlternateFireActive()) {
			return handleSecondaryFire(choice);
		} else {
			return handlePrimaryFire(choice);
		}
	}

	@Override
	public QuestionContainer initialQuestion() {
		List<String> options = new ArrayList<>();
		if (!getPrimaryTargets().isEmpty()) {
			options.add("Standard fire.");
		}
		if (!getSecondaryTargets().isEmpty()) {
			options.add("Alternate fire.");
		}
		return QuestionContainer.createStringQuestionContainer("Which fire mode do you want to use?", options);
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


	private void secondaryReset() {
		this.alternateFireActive = FALSE;
	}

	@Override
	public void reset() {
		super.reset();
		secondaryReset();
	}

	protected QuestionContainer setSecondaryCurrentTargetsAndReturnTargetQnO(){
		currentTargets = getSecondaryTargets();
		return getTargetPlayersQnO(currentTargets);
	}

	@Override
	public boolean canFire() {
		return super.canFire() || !getSecondaryTargets().isEmpty();
	}

	int getMaximumAlternateSteps() {
		return maximumAlternateSteps;
	}

	boolean isAlternateFireActive() {
		return alternateFireActive;
	}

	@Override
	public Representation getRep() {
		return super.getRep();
	}
}