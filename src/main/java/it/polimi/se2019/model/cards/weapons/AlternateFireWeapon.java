package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

/**
 * Weapons with an alternate fire method. The player can choose one of the two firing modes the weapon has.
 *
 * @author Marchingegno
 */
public abstract class AlternateFireWeapon extends WeaponCard {
    private List<DamageAndMarks> secondaryDamagesAndMarks;
    private List<AmmoType> secondaryPrice;
    private boolean alternateFireActive;
    private int secondaryDamage;
    private int secondaryMarks;

    /**
     * Constructor of the class.
     *
     * @param parameters the JsonObject with the parameters needed for the weapon.
     */
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

    void setSecondaryDamagesAndMarks(List<DamageAndMarks> secondaryDamagesAndMarks) {
        this.secondaryDamagesAndMarks = secondaryDamagesAndMarks;
    }

    /**
     * Register whether or not the player has chosen to fire in alternate mode.
     *
     * @param choice the choice of the player.
     */
    private void registerChoice(int choice) {
        if (!canPrimaryBeActivated() || choice == 1) {
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
        Utils.logWeapon(this.getCardName() + ": executing main method with currentStep " + getCurrentStep() + " and choice " + choice);
        if (getCurrentStep() == 1) {
            return initialQuestion();
        } else if (getCurrentStep() == 2) {
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
        if (canPrimaryBeActivated()) {
            options.add("Standard fire.");
            Utils.logWeapon(this.getCardName() + ": added Standard fire to the options.");
        }
        if (canSecondaryBeActivated()) {
            options.add("Alternate fire.");
            Utils.logWeapon(this.getCardName() + ": added Alternate fire to the options.");
        }
        return QuestionContainer.createStringQuestionContainer("Which fire mode do you want to use?", options);
    }

    /**
     * Secondary mode of firing.
     */
    public abstract void secondaryFire();

    /**
     * Get the targets of the secondary mode of fire for this weapon.
     *
     * @return the targettable players.
     */
    public abstract List<Player> getSecondaryTargets();

    /**
     * Resets the alternate mode.
     */
    private void secondaryReset() {
        this.alternateFireActive = FALSE;
    }

    @Override
    public void reset() {
        super.reset();
        secondaryReset();
    }

    /**
     * After getting the secondary targets,
     * builds a {@link QuestionContainer} that asks in which coordinate to fire at.
     *
     * @return the {@link QuestionContainer}.
     */
    QuestionContainer setSecondaryCurrentTargetsAndReturnTargetQnO() {
        setCurrentTargets(getSecondaryTargets());
        return getTargetPlayersQnO(getCurrentTargets());
    }

    @Override
    public boolean canBeActivated() {
        return (canPrimaryBeActivated() || canSecondaryBeActivated()) && isLoaded();
    }

    boolean isAlternateFireActive() {
        return alternateFireActive;
    }

    /**
     * Check whether the player can activate alternate mode and has enough ammo to do so.
     *
     * @return if the player can activate the alternate mode.
     */
    private boolean canSecondaryBeActivated() {
        return canSecondaryBeFired() && getOwner().hasEnoughAmmo(secondaryPrice);
    }

    /**
     * Check if the player can activate alternate mode.
     * It can be different depending on the weapon.
     *
     * @return if the player can activate alternate mode.
     */
    boolean canSecondaryBeFired() {
        return !getSecondaryTargets().isEmpty();
    }

    @Override
    public List<AmmoType> getFiringCost(int choice) {
        if (!canPrimaryBeActivated() || choice == 1) {
            return new ArrayList<>(secondaryPrice);
        } else {
            return new ArrayList<>();
        }
    }

    int getSecondaryDamage() {
        return secondaryDamage;
    }

    void setSecondaryDamage(int secondaryDamage) {
        this.secondaryDamage = secondaryDamage;
    }

    int getSecondaryMarks() {
        return secondaryMarks;
    }

    void setSecondaryMarks(int secondaryMarks) {
        this.secondaryMarks = secondaryMarks;
    }
}