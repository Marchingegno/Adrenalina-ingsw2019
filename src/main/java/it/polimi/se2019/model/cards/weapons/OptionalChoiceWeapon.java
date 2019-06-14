package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.utils.Pair;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

public abstract class OptionalChoiceWeapon extends OptionalEffectsWeapon {

	protected Pair<WeaponEffectType, EffectState> weaponState;
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

	protected abstract QuestionContainer handleBase(int choice);

	protected abstract QuestionContainer handleMove(int choice);

	protected abstract QuestionContainer handleExtra(int choice);

	protected abstract QuestionContainer handleActionSelect(int choice);

	@Override
	public void reset() {
		super.reset();
		baseCompleted = false;
		extraCompleted = false;
		moveCompleted = false;
		weaponState = new Pair<>(WeaponEffectType.ACTION, EffectState.REQUEST);
		effectHasChanged = false;
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
