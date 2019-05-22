package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class Thor extends OptionalEffectsWeapon {
	private List<Player> chosenTargets;

	public Thor(String description, List<AmmoType> reloadPrice) {
		super("T.H.O.R.", description, reloadPrice, 0, 2, 0);
		this.OPTIONAL1_DAMAGE = 1;
		this.OPTIONAL1_MARKS = 0;
		this.OPTIONAL2_DAMAGE = 2;
		this.OPTIONAL2_MARKS = 0;
		standardDamagesAndMarks.add(new DamageAndMarks(OPTIONAL1_DAMAGE, OPTIONAL1_MARKS));
		standardDamagesAndMarks.add(new DamageAndMarks(OPTIONAL2_DAMAGE, OPTIONAL2_MARKS));
	}



	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2){
			 currentTargets = new ArrayList<>();
			 currentTargets = getPrimaryTargets();
			 return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3) {
			chosenTargets = new ArrayList<>();
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
		if(currentTargets.isEmpty()){
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
		dealDamage(standardDamagesAndMarks, chosenTargets);
	}

}