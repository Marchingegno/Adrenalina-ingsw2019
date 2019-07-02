package it.polimi.se2019.model.cards.weapons;


import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.List;

/**
 * Class of the weapon Lock rifle.
 */
public class LockRifle extends OptionalEffectsWeapon {
	private Player secondTarget;

	public LockRifle(JsonObject parameters) {
		super(parameters);
		getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		getStandardDamagesAndMarks().add(new DamageAndMarks(optional1Damage, optional1Marks));
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return getGameMap().getVisiblePlayers(getOwner());
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (getCurrentStep() == 2) {
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		}
		if (getCurrentStep() == 3) {
			setTarget(getCurrentTargets().remove(choice));
		}
		if (isOptionalActive(1)) {
			return handleOptionalEffect1(choice);
		} else {
			primaryFire();
			return null;
		}
	}

	@Override
	protected QuestionContainer handleOptionalEffect1(int choice) {
		if (getCurrentStep() == 3) {
			return getTargetPlayersQnO(getCurrentTargets());
		}
		if (getCurrentStep() == 4) {
			secondTarget = getCurrentTargets().remove(choice);
			primaryFire();
		}
		return null;
	}

	@Override
	public void primaryFire() {
		if (isOptionalActive(1)) {
			dealDamageAndConclude(getStandardDamagesAndMarks(), getTarget(), secondTarget);
		} else {
			dealDamageAndConclude(getStandardDamagesAndMarks(), getTarget());
		}
	}

	@Override
	protected boolean canFireOptionalEffect1() {
		return getPrimaryTargets().size() >= 2;
	}

	@Override
	public void reset() {
		super.reset();
		secondTarget = null;
	}
}