package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.utils.QuestionContainer;

public abstract class OptionalMoveWeapon extends OptionalEffectsWeapon {
	private ActionType actionTypeInExecution;
	private boolean shootingDone;


	public OptionalMoveWeapon(JsonObject parameters) {
		super(parameters);
		actionTypeInExecution = null;
		shootingDone = false;
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (isBothOptionalActive()) {
			return handleBothOptionalEffects(choice);
		}
		if (isOptionalActive(1)) {
			return handleOptionalEffect1(choice);
		}
		if (isOptionalActive(2)) {
			return handleOptionalEffect2(choice);
		}

		return handleNoOptionalEffects(choice);

	}

	protected QuestionContainer handleNoOptionalEffects(int choice) {
		return null;
	}

	protected QuestionContainer getPossibleActionTypeQnO() {
		return getActionTypeQnO(relocationDone, shootingDone);
	}


}
