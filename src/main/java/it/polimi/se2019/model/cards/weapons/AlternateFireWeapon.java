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
	int SECONDARY_DAMAGE;
	int SECONDARY_MARKS;

	/**
	 * @deprecated
	 */
	public AlternateFireWeapon(String weaponName, String description, List<AmmoType> reloadPrice, final int primaryMarks, final int primaryDamage, final int moveDistance) {
		super(weaponName, description, reloadPrice, primaryMarks, primaryDamage, moveDistance);
		reset();
	}

	/**
	 * @deprecated
	 */
	public AlternateFireWeapon(String weaponName, String description, List<AmmoType> reloadPrice, final int primaryMarks, final int primaryDamage) {
		super(weaponName, description, reloadPrice, primaryMarks, primaryDamage);
		reset();
	}

	/**
	 * @deprecated
	 */
	public AlternateFireWeapon(String weaponName, String description, List<AmmoType> reloadPrice, final int primaryMarks) {
		super(weaponName, description, reloadPrice, primaryMarks);
		reset();
	}

	public AlternateFireWeapon(JsonObject parameters) {
		super(parameters);
		this.SECONDARY_DAMAGE = parameters.get("secondaryDamage").getAsInt();
		this.SECONDARY_MARKS = parameters.get("secondaryMarks").getAsInt();
		this.secondaryPrice = new ArrayList<>();

		for (JsonElement price : parameters.getAsJsonArray("secondaryPrice")) {
			this.secondaryPrice.add(AmmoType.valueOf(price.getAsString()));
		}

		parameters.get("secondaryMarks").getAsInt();
	}

	List<DamageAndMarks> getSecondaryDamagesAndMarks() {
		return secondaryDamagesAndMarks;
	}

	public void registerChoice(int choice) {
		if (choice == 1)
			alternateFireActive = true;
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
	public QuestionContainer handleFire(int choice) {
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
		options.add("Standard fire.");
		options.add("Alternate fire.");
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


	void secondaryReset(){
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