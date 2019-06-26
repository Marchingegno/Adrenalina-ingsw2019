package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class Thor extends OptionalEffectsWeapon {
	private List<Player> chosenTargets;
	private List<List<Player>> chainsOnHold;

	public Thor(JsonObject parameters) {
		super(parameters);
		standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		standardDamagesAndMarks.add(new DamageAndMarks(optional1Damage, optional1Marks));
		standardDamagesAndMarks.add(new DamageAndMarks(optional2Damage, optional2Marks));
		chosenTargets = new ArrayList<>();
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (isBothOptionalActive()) {
			return handleBothOptionalEffects(choice);
		}
		if (isOptionalActive(1)) {
			return handleOptionalEffect1(choice);
		} else {
			if (getCurrentStep() == 2) {
				currentTargets = getPrimaryTargets();
				return getTargetPlayersQnO(currentTargets);
			} else if (getCurrentStep() == 3) {
				chosenTargets.add(currentTargets.get(choice));
			}
		}

		primaryFire();
		return null;
	}

	@Override
	protected QuestionContainer handleOptionalEffect1(int choice) {
		if (getCurrentStep() == 2) {
			chainsOnHold = getChainOfTwoPlayers();
			return getChainPlayerQnO(chainsOnHold);
		} else if (getCurrentStep() == 3) {
			chosenTargets = chainsOnHold.get(choice);
		}

		primaryFire();
		return null;
	}

	@Override
	protected QuestionContainer handleBothOptionalEffects(int choice) {
		if (getCurrentStep() == 2) {
			chainsOnHold = getChainOfThreePlayers();
			return getChainPlayerQnO(chainsOnHold);
		} else if (getCurrentStep() == 3) {
			chosenTargets = chainsOnHold.get(choice);
		}

		primaryFire();
		return null;
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return getGameMap().getVisiblePlayers(getOwner());
	}

	@Override
	public void primaryFire() {
		dealDamageAndConclude(standardDamagesAndMarks, chosenTargets);
	}

	private List<List<Player>> getChainOfTwoPlayers() {
		List<List<Player>> chainsOfTwoPlayer = new ArrayList<>();

		List<Player> visiblePlayers = getGameMap().getVisiblePlayers(getOwner());
		for (Player firstPlayer : visiblePlayers) {
			List<Player> visibleByFirst = getGameMap().getVisiblePlayers(firstPlayer);
			visibleByFirst.remove(getOwner());
			for (Player secondPlayer : visibleByFirst) {
				List<Player> chain = new ArrayList<>();
				chain.add(firstPlayer);
				chain.add(secondPlayer);
				chainsOfTwoPlayer.add(chain);
			}
		}
		return chainsOfTwoPlayer;
	}

	private List<List<Player>> getChainOfThreePlayers() {
		List<List<Player>> chainsOfThreePlayer = new ArrayList<>();

		List<Player> visiblePlayers = getGameMap().getVisiblePlayers(getOwner());
		for (Player firstPlayer : visiblePlayers) {
			List<Player> visibleByFirst = getGameMap().getVisiblePlayers(firstPlayer);
			visibleByFirst.remove(getOwner());
			for (Player secondPlayer : visibleByFirst) {
				List<Player> visibleBySecond = getGameMap().getVisiblePlayers(secondPlayer);
				visibleBySecond.remove(getOwner());
				visibleBySecond.remove(firstPlayer);
				for (Player thirdPlayer : visibleBySecond) {
					List<Player> chain = new ArrayList<>();
					chain.add(firstPlayer);
					chain.add(secondPlayer);
					chain.add(thirdPlayer);
					chainsOfThreePlayer.add(chain);
				}
			}
		}
		return chainsOfThreePlayer;
	}

	private boolean isThereAChainOfThreePlayers() {
		return !getChainOfThreePlayers().isEmpty();
	}

	private boolean isThereAChainOfTwoPlayers() {
		return !getChainOfTwoPlayers().isEmpty();
	}

	private QuestionContainer getChainPlayerQnO(List<List<Player>> chains) {
		String question = "Which group of player do you want to target?";
		List<String> options = new ArrayList<>();
		for (List<Player> chain : chains) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Player player : chain) {
				stringBuilder.append(player.getPlayerName());
				stringBuilder.append(" - ");
			}
			stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length());
			options.add(stringBuilder.toString());
		}

		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	@Override
	protected boolean canFireOptionalEffect1() {
		return isThereAChainOfTwoPlayers();
	}

	@Override
	protected boolean canFireBothOptionalEffects() {
		return isThereAChainOfThreePlayers();
	}

	@Override
	protected boolean canFireOptionalEffect2() {
		return false;
	}

	@Override
	public void reset() {
		super.reset();
		chosenTargets = new ArrayList<>();
	}
}