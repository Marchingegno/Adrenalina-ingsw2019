package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Thor extends OptionalEffectsWeapon {
	private List<Player> chosenTargets;

	public Thor(JsonObject parameters) {
		super(parameters);
		standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		standardDamagesAndMarks.add(new DamageAndMarks(optional1Damage, optional1Marks));
		standardDamagesAndMarks.add(new DamageAndMarks(optional2Damage, optional2Marks));
		chosenTargets = new ArrayList<>();
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2){
			 currentTargets = new ArrayList<>();
			 currentTargets = getPrimaryTargets();
			 return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3) {
			chosenTargets.add(currentTargets.get(choice));
		}

		if(isOptionalActive(1)){
			return handleOptionalEffect1(choice);
		}
		else{
			primaryFire();
		}
		return null;
	}

	@Override
	protected QuestionContainer handleOptionalEffect1(int choice) {
		if(getCurrentStep() == 3){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 4){
			chosenTargets.add(currentTargets.get(choice));
		}

		if (isOptionalActive(2)){
			return handleOptionalEffect2(choice);
		}
		else{
			primaryFire();
		}
		return null;
	}

	@Override
	protected QuestionContainer handleOptionalEffect2(int choice) {
		if(getCurrentStep() == 4){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 5){
			chosenTargets.add(currentTargets.get(choice));
			primaryFire();
		}
		return null;
	}

	@Override
	public List<Player> getPrimaryTargets() {
		//Get a target that the player can see if he has not chosen any target yet
		//Get a target that the last player can see, except already chosen targets.
		if (chosenTargets.isEmpty()) {
			return getGameMap().getVisiblePlayers(getOwner());
		}
		else{
			List<Player> enemiesNotChosenSeenByLast = getGameMap().getVisiblePlayers(chosenTargets.get(chosenTargets.size() - 1));
			enemiesNotChosenSeenByLast.removeAll(chosenTargets);
			enemiesNotChosenSeenByLast.remove(getOwner());
			return enemiesNotChosenSeenByLast;
		}
	}

	@Override
	public void primaryFire() {
		dealDamageAndConclude(standardDamagesAndMarks, chosenTargets);
	}

	@Override
	public QuestionContainer initialQuestion() {
		String question = "Which optional effect do you want to activate?";
		List<String> options = new ArrayList<>();
		options.add("No optional effects.");

		//Can add optionalEffect 1?
		if (canAddThisOptionalEffect(1)) {
			options.add("Optional effect 1.");
		}
		//Can add optionalEffect 1 + 2?
		if (canAddBothOptionalEffects()) {
			options.add("Optional effect 1 + Optional effect 2.");
		}
		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	@Override
	protected void registerChoice(int choice) {
		//The choice 3 can't be made because of the overrided "initialQuestion".
		if(choice == 3){
			Utils.logError("Thor: received wrong choice number (3).", new IllegalArgumentException());
		}

		//If the choice is 2, the player has chosen both optional effects.
		if(choice == 2){
			super.registerChoice(3);
		}
		else{
			super.registerChoice(choice);
		}
	}

	@Override
	public void reset() {
		super.reset();
		chosenTargets = new ArrayList<>();
	}

	@Override
	protected boolean canAddOptionalEffect1() {
		//There is at least a chain of two players.
		return !getChainOfTwoPlayers()[1].isEmpty();
	}

	private List<Player>[] getChainOfTwoPlayers() {
		List<Player> visiblePlayers = getPrimaryTargets();
		List<Player> enemiesSeenByVisiblePlayers = new ArrayList<>();
		for (Player visiblePlayer : visiblePlayers) {
			List<Player> seenByVisiblePlayer = getGameMap().getVisiblePlayers(visiblePlayer);
			seenByVisiblePlayer.remove(getOwner());
			for (Player playerToAddIfNotPresent : seenByVisiblePlayer) {
				if (!enemiesSeenByVisiblePlayers.contains(playerToAddIfNotPresent)) {
					enemiesSeenByVisiblePlayers.add(playerToAddIfNotPresent);
				}
			}
		}

		return null;
	}

	@Override
	protected boolean canAddBothOptionalEffects() {
		return true;
	}
}