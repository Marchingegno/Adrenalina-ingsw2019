package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class ZX_2 extends AlternateFireWeapon {
	private List<Player> secondaryTargets;

	public ZX_2(JsonObject parameters) {
		super(parameters);
		this.secondaryDamage = parameters.get("secondaryDamage").getAsInt();
		this.secondaryMarks = parameters.get("secondaryMarks").getAsInt();
		this.standardDamagesAndMarks = new ArrayList<>();
		standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.secondaryDamagesAndMarks = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			this.secondaryDamagesAndMarks.add(new DamageAndMarks(secondaryDamage, secondaryMarks));
		}
		this.maximumSteps = 3;
	}


	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (getCurrentStep() == 2){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3){
			target = currentTargets.get(choice);
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		switch (getCurrentStep()){
			case 2:
				currentTargets = getSecondaryTargets();
				return getTargetPlayersQnO(currentTargets);
			case 3:
				secondaryTargets = new ArrayList<>();
				secondaryTargets.add(currentTargets.remove(choice));
				if (currentTargets.isEmpty()){
					terminateSecondaryFire();
					return null;
				}
				return getTargetPlayersQnO(currentTargets);
			case 4:
				if (currentTargets.isEmpty()){
					terminateSecondaryFire();
					return null;
				}
				secondaryTargets.add(currentTargets.remove(choice));
				return getTargetPlayersQnO(currentTargets);
			case 5:
				if (currentTargets.isEmpty()){
					terminateSecondaryFire();
					return null;
				}
				secondaryTargets.add(currentTargets.remove(choice));
				secondaryFire();
		}
		return null;
	}

	public void primaryFire() {
		dealDamage(standardDamagesAndMarks, target);
	}

	public void secondaryFire() {
		dealDamage(secondaryDamagesAndMarks, secondaryTargets);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return getGameMap().getVisiblePlayers(getOwner());
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return getPrimaryTargets();
	}

	/**
	 * Instantly fires the weapon. Called if there are no more target to choose from in secondary fire mode.
	 */
	private void terminateSecondaryFire(){
		while(getCurrentStep() < getMaximumSteps()){
			incrementCurrentStep();
		}
		secondaryFire();
	}


	@Override
	public void reset() {
		super.reset();
		secondaryTargets = new ArrayList<>();
	}

	@Override
	public boolean canBeActivated() {
		return true;
	}
}