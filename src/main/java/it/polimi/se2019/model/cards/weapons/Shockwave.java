package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the weapon Shockwave.
 * @author Marchingeno
 */
public class Shockwave extends AlternateFireWeapon {
	private List<Player> chosenTargets;

	public Shockwave(JsonObject parameters) {
		super(parameters);
		this.chosenTargets = new ArrayList<>();
		this.setSecondaryDamage(parameters.get("secondaryDamage").getAsInt());
		this.setSecondaryMarks(parameters.get("secondaryMarks").getAsInt());
		for (int i = 0; i < 3; i++) {
			this.getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		}
		this.getSecondaryDamagesAndMarks().add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {

		if (getCurrentStep() == 5) {
			chosenTargets.add(getCurrentTargets().get(choice));
			primaryFire();
		} else {
			return handleChooseTargets(choice);
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		setCurrentTargets(getSecondaryTargets());
		secondaryFire();
		return null;
	}

	/**
	 * Handles choosing targets.
	 *
	 * @param choice the choice of the player.
	 * @return the QuestionContainer containing the targets.
	 */
	private QuestionContainer handleChooseTargets(int choice) {
		//Initial step: the player hasn't chosen yet.
		if (getCurrentStep() != 2) {
			try {
				chosenTargets.add(getCurrentTargets().get(choice));
			} catch (IndexOutOfBoundsException e) {
				Utils.logWarning("Shockwave: no targets near " + getOwner().getPlayerName() + ".");
			}
		}
		setCurrentTargets(getPrimaryTargets());
		if (getCurrentTargets().isEmpty()) {
			primaryFire();
			return null;
		}
		return getTargetPlayersQnO(getCurrentTargets());
	}

	@Override
	protected void primaryFire() {
		dealDamageAndConclude(getStandardDamagesAndMarks(), chosenTargets);
	}

	@Override
	public void secondaryFire() {
		for (int i = 0; i < getCurrentTargets().size(); i++) {
			this.getSecondaryDamagesAndMarks().add(this.getSecondaryDamagesAndMarks().get(0));
		}
		dealDamageAndConclude(getSecondaryDamagesAndMarks(), getCurrentTargets());
	}

	@Override
	public List<Player> getPrimaryTargets() {
		//Adjacent players, except the players in squares where a player is already been chosen.
		List<Player> adjacentPlayersExceptChosen = getGameMap().reachablePlayers(getOwner(), 1);
		adjacentPlayersExceptChosen.removeAll(getGameMap().reachablePlayers(getOwner(), 0));
		if (chosenTargets != null && !chosenTargets.isEmpty()) {
			//Then the player has already chosen someone, we need to remove all the players in target's squares.
			for (Player enemy : chosenTargets) {
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


	@Override
	public void reset() {
		super.reset();
		chosenTargets = new ArrayList<>();
	}


}