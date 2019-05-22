package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class Whisper extends WeaponCard {

	public Whisper(String description, List<AmmoType> reloadPrice) {
		super("Whisper", description, reloadPrice, 1, 3, 0);
		this.standardDamagesAndMarks = new ArrayList<>();
		standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));

	}

	@Override
	public QuestionContainer handleFire(int choice) {
		incrementCurrentStep();
		return handlePrimaryFire(choice);
	}


	@Override
	public void primaryFire() {
		dealDamage(standardDamagesAndMarks, target);
	}


	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> distantPlayers = getGameMap().getVisiblePlayers(getOwner());
		distantPlayers.removeAll(getGameMap().reachablePlayers(getOwner(), 1));
		return distantPlayers;
	}


	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if(getCurrentStep() == 1){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 2){
			target = currentTargets.get(choice);
			primaryFire();
		}
		return null;
	}

}