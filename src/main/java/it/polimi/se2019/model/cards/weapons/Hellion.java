package it.polimi.se2019.model.cards.weapons;


import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the weapon Hellion.
 */
public class Hellion extends AlternateFireWeapon {
	private final int primaryFollowingDamage;
	private final int secondaryFollowingDamage;
	private final int primaryFollowingMarks;
	private final int secondaryFollowingMarks;

	public Hellion(JsonObject parameters) {
		super(parameters);
		this.primaryFollowingDamage = parameters.get("primaryFollowingDamage").getAsInt();
		this.secondaryFollowingDamage = parameters.get("secondaryFollowingDamage").getAsInt();
		this.primaryFollowingMarks = parameters.get("primaryFollowingMarks").getAsInt();
		this.secondaryFollowingMarks = parameters.get("secondaryFollowingMarks").getAsInt();
		this.getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.getStandardDamagesAndMarks().add(new DamageAndMarks(primaryFollowingDamage, primaryFollowingMarks));
		this.setSecondaryDamagesAndMarks(new ArrayList<>());
		this.getSecondaryDamagesAndMarks().add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));
		this.getSecondaryDamagesAndMarks().add(new DamageAndMarks(secondaryFollowingDamage, secondaryFollowingMarks));
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if (getCurrentStep() == 2) {
			setCurrentTargets(getPrimaryTargets());
			return getTargetPlayersQnO(getCurrentTargets());
		} else if (getCurrentStep() == 3) {
			setTarget(getCurrentTargets().get(choice));
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		if (getCurrentStep() == 2) {
			setCurrentTargets(getSecondaryTargets());
			return getTargetPlayersQnO(getCurrentTargets());
		} else if (getCurrentStep() == 3) {
			setTarget(getCurrentTargets().get(choice));
			secondaryFire();
		}
		return null;
	}

	@Override
	protected void primaryFire() {
		unifiedFire();
	}

	@Override
	public void secondaryFire() {
		unifiedFire();
	}

	/**
	 * PrimaryFire and SecondaryFire can be unified via this method.
	 */
	private void unifiedFire() {
		//Get the right list of damages and marks to begin firing, depending on which fire mode is chosen.
		List<DamageAndMarks> damageAndMarksList = isAlternateFireActive() ? new ArrayList<>(getSecondaryDamagesAndMarks()) : new ArrayList<>(getStandardDamagesAndMarks());
		//Get the player in the same square of the target, then add the target on top of the list.
		List<Player> finalTargets = getGameMap().getPlayersFromCoordinates(getGameMap().getPlayerCoordinates(getTarget()));
		finalTargets.add(0, getTarget());
		//Add the additional marks for the other players in the square.
		List<DamageAndMarks> finalDamageAndMarks = new ArrayList<>(damageAndMarksList);
		for (int i = 0; i < finalTargets.size() - 1; i++) {
			finalDamageAndMarks.add(damageAndMarksList.get(1));
		}
		dealDamageAndConclude(finalDamageAndMarks, finalTargets);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		List<Player> distantVisiblePlayers = getGameMap().getVisiblePlayers(getOwner());
		distantVisiblePlayers.removeAll(getGameMap().reachablePlayers(getOwner(), 0));
		return distantVisiblePlayers;
	}


	@Override
	public List<Player> getSecondaryTargets() {
		return getPrimaryTargets();
	}


}