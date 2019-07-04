package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.utils.Pair;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for weapons that have optional effects that can be fired in any order.
 * The user is prompted to choose which effect to activate every time it wishes to do so.
 * The whole weapon can be split into at most three separate effects: BASE,EXTRA,MOVE.
 * Every effect can have a status: REQUEST and ANSWER.
 * REQUEST is used when the Question Container is asked to the player; ANSWER is used when the player has answered to the
 * Question Container.
 * The state ACTION is used to ask to the player which of the three effects he would like to choose.
 *
 * @author Marchingeno
 */
public abstract class OptionalChoiceWeapon extends OptionalEffectsWeapon {

    boolean canAddBase;
    boolean canAddMove;
    boolean canAddExtra;
    boolean baseCompleted;
    boolean moveCompleted;
    boolean extraCompleted;
    private String baseName;
    private String moveName;
    private String extraName;
    private Pair<WeaponEffectType, EffectState> weaponState;
    private boolean effectHasChanged;
    private WeaponEffectType[] currentEffectList;
    private WeaponEffectType nextType;
    private boolean ended;

    public OptionalChoiceWeapon(JsonObject parameters) {
        super(parameters);
        weaponState = new Pair<>(WeaponEffectType.ACTION, EffectState.REQUEST);
        baseCompleted = false;
        moveCompleted = false;
        extraCompleted = false;
        effectHasChanged = false;
        currentEffectList = new WeaponEffectType[3];
        nextType = null;
        ended = false;

        baseName = "Placeholder base name";
        moveName = "Placeholder move name";
        extraName = "Placeholder extra name";
    }

    @Override
    QuestionContainer handlePrimaryFire(int choice) {
        return handleChoices(choice);
    }

    /**
     * Advance the weapon state from REQUEST to ANSWER, and set true to the correct boolean.
     *
     * @return true if weaponState was in ANSWER state.
     */
    private boolean advanceState() {
        if (weaponState.getSecond() == EffectState.ANSWER) {
            switch (weaponState.getFirst()) {
                case MOVE:
                    weaponState = new Pair<>(WeaponEffectType.ACTION, EffectState.REQUEST);
                    moveCompleted = true;
                    break;
                case BASE:
                    weaponState = new Pair<>(WeaponEffectType.ACTION, EffectState.REQUEST);
                    baseCompleted = true;
                    break;
                case EXTRA:
                    weaponState = new Pair<>(WeaponEffectType.ACTION, EffectState.REQUEST);
                    extraCompleted = true;
                    break;
                case ACTION:
                    weaponState = new Pair<>(nextType, EffectState.REQUEST);
                    break;
            }
            effectHasChanged = true;
            return true;
        } else {
            weaponState = new Pair<>(weaponState.getFirst(), EffectState.values()[weaponState.getSecond().ordinal() + 1]);
            Utils.logWeapon("Setting weapon state to: " + weaponState.toString());
        }
        return false;
    }

    /**
     * Handles the base case of the weapon.
     *
     * @param choice the choice of the player.
     * @return the Question Container to ask to the player.
     */
    private QuestionContainer handleBase(int choice) {
        switch (weaponState.getSecond()) {
            case REQUEST:
                return handleBaseRequest(choice);
            case ANSWER:
                handleBaseAnswer(choice);
                break;
        }
        return null;
    }


    protected QuestionContainer handleBaseRequest(int choice) {
        return setPrimaryCurrentTargetsAndReturnTargetQnO();
    }

    private void handleBaseAnswer(int choice) {
        setTarget(getCurrentTargets().get(choice));
    }

    /**
     * Handles the move case of the weapon.
     * @param choice the choice of the player.
     * @return the Question Container to ask to the player.
     */
    protected QuestionContainer handleMove(int choice) {
        switch (weaponState.getSecond()) {
            case REQUEST:
                return handleMoveRequest(choice);
            case ANSWER:
                handleMoveAnswer(choice);
                break;
        }
        return null;
    }

    protected abstract QuestionContainer handleMoveRequest(int choice);

    protected abstract void handleMoveAnswer(int choice);

    /**
     * Handles the extra case of the weapon.
     * @param choice the choice of the player.
     * @return the Question Container to ask to the player.
     */
    private QuestionContainer handleExtra(int choice) {
        switch (weaponState.getSecond()) {
            case REQUEST:
                return handleExtraRequest(choice);
            case ANSWER:
                handleExtraAnswer(choice);
                break;
        }
        return null;
    }

    protected abstract QuestionContainer handleExtraRequest(int choice);

    protected abstract void handleExtraAnswer(int choice);

    /**
     * Handles the selection of the next action of the weapon.
     * @param choice the choice of the player.
     * @return the Question Container to ask to the player.
     */
    private QuestionContainer handleActionSelect(int choice) {
        Utils.logWeapon("Weapon " + this.getCardName() + " : executing handleActionSelect with weapon state: ");
        Utils.logWeapon(weaponState.toString());

        switch (weaponState.getSecond()) {
            case REQUEST:
                return setCurrentActionListReturnActionTypeQnO();
            case ANSWER:
                nextType = currentEffectList[choice];
                break;
        }
        return null;
    }

    /**
     * Saves the effects that the player can activate, and ask the effect to activate.
     * @return the Question Container to ask to the player.
     */
    private QuestionContainer setCurrentActionListReturnActionTypeQnO() {
        String question = "Which action do you want to do?";
        List<String> options = new ArrayList<>();
        currentEffectList = new WeaponEffectType[3];
        updateBooleans();
        int i = 0;
        if (canAddBase) {
            options.add(baseName);
            currentEffectList[i++] = WeaponEffectType.BASE;
        }
        if (canAddMove) {
            options.add(moveName);
            currentEffectList[i++] = WeaponEffectType.MOVE;
        }
        if (canAddExtra) {
            options.add(extraName);
            currentEffectList[i] = WeaponEffectType.EXTRA;
        }


        if (options.isEmpty()) {
            ended = true;
            Utils.logWeapon("Ended because no other options is available.");
        }

        return QuestionContainer.createStringQuestionContainer(question, options);


    }

    /**
     * The core logic of the weapon. Base on the current WeaponEffectState, it calls the right method to advance the
     * firing process.
     * @param choice the choice of the player.
     * @return the Question Container to ask to the player.
     */
    QuestionContainer handleChoices(int choice) {
        QuestionContainer qc;
        Utils.logWeapon("Executing handleChoices with choice " + choice + " and pair:");
        Utils.logWeapon(weaponState.toString());
        switch (weaponState.getFirst()) {
            case ACTION:
                qc = handleActionSelect(choice);
                break;
            case BASE:
                qc = handleBase(choice);
                break;
            case MOVE:
                qc = handleMove(choice);
                break;
            case EXTRA:
                qc = handleExtra(choice);
                break;
            default:
                qc = null;
                break;
        }

        if (ended) {
            primaryFire();
            return null;
        }

        advanceState();

        if (effectHasChanged) {
            effectHasChanged = false;
            return handleChoices(choice);
        } else {
            return qc;
        }
    }

    /**
     * Updates the boolean that tells whether the player can activate the effects.
     */
    protected abstract void updateBooleans();

    @Override
    public void reset() {
        super.reset();
        baseCompleted = false;
        extraCompleted = false;
        moveCompleted = false;
        weaponState = new Pair<>(WeaponEffectType.ACTION, EffectState.REQUEST);
        effectHasChanged = false;
        ended = false;
    }

    void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    void setMoveName(String moveName) {
        this.moveName = moveName;
    }

    void setExtraName(String extraName) {
        this.extraName = extraName;
    }

    enum EffectState {
        REQUEST,
        ANSWER
    }

    enum WeaponEffectType {
        BASE,
        MOVE,
        EXTRA,
        ACTION
    }
}
