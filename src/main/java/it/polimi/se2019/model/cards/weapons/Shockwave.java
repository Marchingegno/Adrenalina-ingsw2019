package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Shockwave extends AlternateFireWeapon {
	private List<Player> chosenTargets;

	public Shockwave(String description, List<AmmoType> reloadPrice) {
		super("Shock Wave", description, reloadPrice);
		this.chosenTargets = new ArrayList<>();
		this.PRIMARY_DAMAGE = 1;
		this.PRIMARY_MARKS = 0;
		this.SECONDARY_DAMAGE = 1;
		this.SECONDARY_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		}
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		this.maximumSteps = 5;
		this.maximumAlternateSteps = 2;
	}

	@Override
	Pair handlePrimaryFire(int choice) {

		if(getCurrentStep() == 5){
			chosenTargets.add(currentTargets.get(choice));
			primaryFire();
		}
		else{
			return handleChooseTargets(choice);
		}
		return null;
	}

	@Override
	Pair handleSecondaryFire(int choice) {
		currentTargets = getSecondaryTargets();
		secondaryFire();
		return null;
	}

	private Pair handleChooseTargets(int choice){
		//Initial step: the player hasn't chosen yet.
		if(getCurrentStep() != 2){
			try{
				chosenTargets.add(currentTargets.get(choice));
			} catch (IndexOutOfBoundsException e){
				Utils.logWarning("Shockwave: no targets near "+getOwner().getPlayerName() + ".");
			}
		}
		currentTargets = getPrimaryTargets();
		return getTargetPlayersQnO(currentTargets);
	}


	public void primaryFire() {
		dealDamage(standardDamagesAndMarks, chosenTargets);
	}

	public void secondaryFire() {
		for (int i = 0; i < currentTargets.size(); i++) {
			this.secondaryDamagesAndMarks.add(this.secondaryDamagesAndMarks.get(0));
		}
		dealDamage(secondaryDamagesAndMarks, currentTargets);
	}

	@Override
	public List<Player> getPrimaryTargets() {
		//Adjacent players, except the players in squares where a player is already been chosen.
		List<Player> adjacentPlayersExceptChosen = getGameMap().reachablePlayers(getOwner(), 1);
		adjacentPlayersExceptChosen.removeAll(getGameMap().reachablePlayers(getOwner(), 0));
		if(!chosenTargets.isEmpty()){
			//Then the player has already chosen someone, we need to remove all the players in target's squares.
			for (Player enemy:chosenTargets) {
				List<Player> playersToRemove = getGameMap().getPlayersFromCoordinates(getGameMap().getPlayerCoordinates(enemy));
				adjacentPlayersExceptChosen.removeAll(playersToRemove);
			}
		}
		return adjacentPlayersExceptChosen;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		List<Player> adjacentPlayers = getGameMap().reachablePlayers(getOwner(), 1);
		adjacentPlayers.removeAll(getGameMap().reachablePlayers(getOwner(), 0));
		return adjacentPlayers;
	}

}