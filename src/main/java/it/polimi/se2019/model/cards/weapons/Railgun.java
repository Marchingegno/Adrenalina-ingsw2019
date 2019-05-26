package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class Railgun extends AlternateFireWeapon {
	private CardinalDirection chosenDirection;
	private Player secondTarget;

	public Railgun(JsonObject parameters) {
		super(parameters);
		this.secondaryDamage = parameters.get("secondaryDamage").getAsInt();
		this.secondaryMarks = parameters.get("secondaryMarks").getAsInt();
		this.standardDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(secondaryDamage, secondaryMarks));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(secondaryDamage, secondaryMarks));
		this.maximumSteps = 4;
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2){
			return getCardinalQnO();
		}
		else if(getCurrentStep() == 3){
			chosenDirection = CardinalDirection.values()[choice];
			return setPrimaryCurrentTargetsAndReturnTargetQnO();
		}
		else if(getCurrentStep() == 4){
			target = currentTargets.get(choice);
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		if(getCurrentStep() == 2){
			return getCardinalQnO();
		}
		else if(getCurrentStep() == 3){
			chosenDirection = CardinalDirection.values()[choice];
			return setSecondaryCurrentTargetsAndReturnTargetQnO();
		}
		else if(getCurrentStep() == 4){
			target = currentTargets.get(choice);
			//With refusal.
			currentTargets = getSecondaryTargets();
			return getTargetPlayersAndRefusalQnO(currentTargets);
		}
		else if(getCurrentStep() == 5){
			if(!isThisChoiceRefusal(currentTargets, choice)){
				secondTarget = currentTargets.get(choice);
			}
			secondaryFire();
		}

		return null;
	}

	@Override
	public void primaryFire() {
		dealDamage(standardDamagesAndMarks, target);
	}

	public void secondaryFire() {
		dealDamage(secondaryDamagesAndMarks, target, secondTarget);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> playersInDirection = getGameMap().getPlayersInDirection(getGameMap().getPlayerCoordinates(getOwner()), chosenDirection);
		playersInDirection.remove(getOwner());
		return playersInDirection;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		List<Player> playersInDirection = getPrimaryTargets();
		if(target != null){
			playersInDirection.remove(target);
		}
		return playersInDirection;
	}

	@Override
	public void reset() {
		super.reset();
		secondTarget = null;
		chosenDirection = null;
	}


}