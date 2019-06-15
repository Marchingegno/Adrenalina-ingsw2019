package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.utils.Pair;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class OptionalChoiceWeapon extends OptionalEffectsWeapon {

	protected Pair<WeaponEffectType, EffectState> weaponState;
	private boolean ended;
	protected String baseName;
	protected String moveName;
	protected String extraName;
	protected boolean canAddBase;
	protected boolean canAddMove;
	protected boolean canAddExtra;
	protected boolean baseCompleted;
	protected boolean moveCompleted;
	protected boolean extraCompleted;
	protected boolean effectHasChanged;
	protected WeaponEffectType[] currentEffectList;
	protected WeaponEffectType nextType;

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
	}

	/**
	 * Advance the weapon state from REQUEST to ANSWER, and set true to the correct boolean.
	 *
	 * @return true if weaponState is in ANSWER state.
	 */
	protected boolean advanceState() {
		Utils.logWeapon("Current weapon state: " + weaponState.toString());
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
			}
			effectHasChanged = true;
			return true;
		} else {
			weaponState = new Pair<>(weaponState.getFirst(), EffectState.values()[weaponState.getSecond().ordinal() + 1]);
			Utils.logWeapon("Setting weapon state to: " + weaponState.toString());
		}
		return false;
	}

	protected boolean ended() {
		return extraCompleted && moveCompleted && baseCompleted;
	}

	protected QuestionContainer handleBase(int choice) {
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

	protected QuestionContainer handleBaseAnswer(int choice) {
		target = currentTargets.get(choice);
		return null;
	}

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

	protected abstract QuestionContainer handleMoveAnswer(int choice);

	protected QuestionContainer handleExtra(int choice) {
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

	protected abstract QuestionContainer handleExtraAnswer(int choice);

	protected QuestionContainer handleActionSelect(int choice) {
		switch (weaponState.getSecond()) {
			case REQUEST:
				return setCurrentActionListReturnActionTypeQnO();
			case ANSWER:
				nextType = currentEffectList[choice];
				break;
		}
		return null;
	}

	protected QuestionContainer setCurrentActionListReturnActionTypeQnO() {
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
			primaryFire();
			ended = true;
			Utils.logWeapon("Ended because no other options is available.");
		}

		return QuestionContainer.createStringQuestionContainer(question, options);


	}

	protected QuestionContainer handleChoices(int choice) {
		QuestionContainer qc;
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
