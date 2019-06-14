package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Pair;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GrenadeLauncher extends OptionalEffectsWeapon {
	private Pair<WeaponEffectType, EffectState> weaponState;
	private List<Coordinates> possibleCoordinates;
	private boolean baseCompleted;
	private boolean moveCompleted;
	private boolean extraCompleted;
	private boolean effectHasChanged;
	private WeaponEffectType[] currentEffectList;
	private WeaponEffectType nextType;


	public GrenadeLauncher(JsonObject parameters) {
		super(parameters);
		hasOptionalEffects[1] = false;
		currentEffectList = new WeaponEffectType[3];
		weaponState = new Pair<>(WeaponEffectType.ACTION, EffectState.REQUEST);
		standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		for (int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
			optional1DamagesAndMarks.add(new DamageAndMarks(optional1Damage, optional1Marks));
		}

		baseCompleted = false;
		moveCompleted = false;
		extraCompleted = false;
		effectHasChanged = false;
		nextType = null;
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		//Only move and base.
		if (isOptionalActive(1)) {
			return handleOptionalEffect1(choice);
		}
		if (getCurrentStep() == 2) {
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		}
		if (getCurrentStep() == 3) {
			target = currentTargets.get(choice);
			possibleCoordinates = getEnemyMovingCoordinates();
			return getMovingTargetEnemyCoordinatesQnO(target, possibleCoordinates);
		}
		if (getCurrentStep() == 4) {
			relocateEnemy(target, possibleCoordinates.get(choice));
			primaryFire();
		}
		return null;
	}

	@Override
	protected QuestionContainer handleOptionalEffect1(int choice) {
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

		if (ended()) {
			primaryFire();
			return null;
		}
		advanceState();
		if (effectHasChanged) {
			effectHasChanged = false;
			return handleOptionalEffect1(choice);
		} else {
			return qc;
		}
	}

	private QuestionContainer handleBase(int choice) {
		switch (weaponState.getSecond()) {
			case REQUEST:
				return setPrimaryCurrentTargetsAndReturnTargetQnO();
			case ANSWER:
				target = currentTargets.get(choice);
				break;
		}
		return null;
	}

	private QuestionContainer handleMove(int choice) {
		switch (weaponState.getSecond()) {
			case REQUEST:
				possibleCoordinates = getEnemyMovingCoordinates();
				return getMovingTargetEnemyCoordinatesQnO(target, possibleCoordinates);
			case ANSWER:
				relocateEnemy(target, possibleCoordinates.get(choice));
				break;
		}
		return null;
	}

	private QuestionContainer handleExtra(int choice) {
		switch (weaponState.getSecond()) {
			case REQUEST:
				possibleCoordinates = getExtraCoordinates();
				return getTargetCoordinatesQnO(possibleCoordinates);
			case ANSWER:
				List<Player> playersToShoot = getGameMap().getPlayersFromCoordinates(possibleCoordinates.get(choice));
				playersToShoot.remove(getOwner());
				dealDamage(optional1DamagesAndMarks, playersToShoot);
				break;
		}
		return null;
	}


	private QuestionContainer handleActionSelect(int choice) {
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
	 * Advance the weapon state from REQUEST to ANSWER, and set true to the correct boolean.
	 *
	 * @return true if weaponState is in ANSWER state.
	 */
	private boolean advanceState() {
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

	@Override
	public void primaryFire() {
		dealDamageAndConclude(standardDamagesAndMarks, target);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return getGameMap().getVisiblePlayers(getOwner());
	}

	private List<Coordinates> getExtraCoordinates() {
		List<Coordinates> visibleCoordinates = getGameMap().getVisibleCoordinates(getOwner());
		List<Coordinates> coordinatesWithPlayer = new ArrayList<>();
		for (Coordinates coordinate : visibleCoordinates) {
			List<Player> playerInThisCoordinates = getGameMap().getPlayersFromCoordinates(coordinate);

			if (getGameMap().getPlayerCoordinates(getOwner()).equals(coordinate)) {
				//If the coordinates is the same as owner's
				//See if there are other players other than owner.
				playerInThisCoordinates.remove(getOwner());
				if (!playerInThisCoordinates.isEmpty()) {
					coordinatesWithPlayer.add(coordinate);
				}
			} else {
				if (!playerInThisCoordinates.isEmpty()) {
					coordinatesWithPlayer.add(coordinate);
				}
			}
		}

		return coordinatesWithPlayer;
	}

	private List<Coordinates> getEnemyMovingCoordinates() {
		return getGameMap().reachableCoordinates(target, 1);
	}


	private QuestionContainer setCurrentActionListReturnActionTypeQnO() {
		String question = "Which action do you want to do?";
		List<String> options = new ArrayList<>();
		boolean canAddBase = !baseCompleted && !getPrimaryTargets().isEmpty();
		boolean canAddMove = !moveCompleted && baseCompleted;
		boolean canAddExtra = !extraCompleted && !getExtraCoordinates().isEmpty();
		currentEffectList = new WeaponEffectType[3];
		int i = 0;
		if (canAddBase) {
			options.add("Base effect");
			currentEffectList[i++] = WeaponEffectType.BASE;
		}
		if (canAddMove) {
			options.add("Move the target");
			currentEffectList[i++] = WeaponEffectType.MOVE;
		}
		if (canAddExtra) {
			options.add("Extra grenade");
			currentEffectList[i] = WeaponEffectType.EXTRA;
		}

		if (options.isEmpty()) {
			primaryFire();
			Utils.logWeapon("The player just lost the right to fire. Probably moved someone over his line of sight and");
			Utils.logWeapon("there are no more squares to fire at.");
		}

		return QuestionContainer.createStringQuestionContainer(question, options);


	}

	@Override
	public void reset() {
		super.reset();
		baseCompleted = false;
		extraCompleted = false;
		moveCompleted = false;
		weaponState = new Pair<>(WeaponEffectType.ACTION, EffectState.REQUEST);
		effectHasChanged = false;
	}

	private boolean ended() {
		return extraCompleted && moveCompleted && baseCompleted;
	}

	@Override
	protected boolean canAddOptionalEffect1() {
		return !getExtraCoordinates().isEmpty();
	}

	private enum EffectState {
		REQUEST,
		ANSWER
	}

	private enum WeaponEffectType {
		BASE,
		MOVE,
		EXTRA,
		ACTION
	}
}
