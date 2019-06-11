package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.List;

public abstract class OptionalMoveWeapon extends OptionalEffectsWeapon {
	protected ActionType actionTypeInExecution;
	boolean selectionDone;
	boolean moveRequested;
	List<Coordinates> possibleMoveCoordinates;

	public OptionalMoveWeapon(JsonObject parameters) {
		super(parameters);
		actionTypeInExecution = null;
		selectionDone = false;
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


	protected QuestionContainer handleMoveRequestAnswer(int choice) {
		if (!moveRequested) {
			if (selectionDone) {
				possibleMoveCoordinates = getMoveCoordinates();
			} else {
				possibleMoveCoordinates = getMoveBeforeFiringCoordinates();
			}
			moveRequested = true;
			return getMoveCoordinatesQnO(possibleMoveCoordinates);
		} else {
			relocateOwner(possibleMoveCoordinates.get(choice));
			relocationDone = true;
			moveRequested = false;
		}
		return null;
	}

	protected List<Coordinates> getMoveCoordinates() {
		return getGameMap().reachableCoordinates(getOwner(), getMoveDistance());
	}

	protected List<Coordinates> getMoveBeforeFiringCoordinates() {
		return getMoveCoordinates();
	}

	protected QuestionContainer handleNoOptionalEffects(int choice) {
		return null;
	}

	protected QuestionContainer getPossibleActionTypeQnO() {
		boolean canShoot = !getPrimaryTargets().isEmpty();
		return getActionTypeQnO(relocationDone, selectionDone || canShoot);
	}


}
