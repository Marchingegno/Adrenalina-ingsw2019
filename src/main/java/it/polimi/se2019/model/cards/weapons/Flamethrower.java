package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Flamethrower extends AlternateFireWeapon {
	private static final int SECONDARY_FOLLOWING_DAMAGE = 1;
	private static final int SECONDARY_FOLLOWING_MARKS = 0;
	private CardinalDirection chosenDirection;
	private Player firstSquareTarget;
	private Player secondSquareTarget;
	private List<Player> secondSquareTargets;

	public Flamethrower(String description, List<AmmoType> reloadPrice) {
		super("FlameThrower", description, reloadPrice, 0, 1, 0);
		this.SECONDARY_DAMAGE = 2;
		this.SECONDARY_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		secondaryDamagesAndMarks = new ArrayList<>();
		secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_FOLLOWING_DAMAGE, SECONDARY_FOLLOWING_MARKS));
		this.maximumSteps = 5;
		this.maximumAlternateSteps = 3;
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		switch (getCurrentStep()){
			case 2:
				return handleDirectionChoice(choice);
			case 3:
				handleDirectionChoice(choice);
				currentTargets = getPrimaryTargets();
				//If there are no targets on the first square, ask second square target.
				//So I increment the step and re-call this method.
				if (currentTargets.isEmpty()){
					incrementCurrentStep();
					return handlePrimaryFire(0);
				}
				return getTargetPlayersQnO(currentTargets);
			case 4:
				try{
					firstSquareTarget = currentTargets.get(choice);
				} catch (IndexOutOfBoundsException e){
					Utils.logInfo("There are no players in the first square chosen by " + getOwner().getPlayerName() +".");
					firstSquareTarget = null;
				}

				currentTargets = getSecondSquareTargets();
				try{
					if (currentTargets.isEmpty()){
						incrementCurrentStep();
						return handlePrimaryFire(0);
					}
				} catch (NullPointerException e){
					Utils.logError("Flamethrower: currentTargets is null", e);
				}

				return getTargetPlayersQnO(currentTargets);
			case 5:
				try{
					secondSquareTarget = currentTargets.get(choice);
				}catch (IndexOutOfBoundsException e){
					Utils.logInfo("There are no players in the second square chosen by " + getOwner().getPlayerName() +".");
					secondSquareTarget = null;
				}
				primaryFire();
				break;
		}
		return null;
	}


	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		switch (getCurrentStep()) {
			case 2:
				return handleDirectionChoice(choice);
			case 3:
				handleDirectionChoice(choice);
				currentTargets = getSecondaryTargets();
				secondSquareTargets = getSecondSquareTargets();
				secondaryFire();
				break;
		}
		return null;
	}

	private QuestionContainer handleDirectionChoice(int choice){
		if(getCurrentStep() == 2) {
			return getCardinalQnO();
		}
		else if(getCurrentStep() == 3){
			chosenDirection = CardinalDirection.values()[choice];
		}
		return null;
	}

	@Override
	public void primaryFire() {
		dealDamage(standardDamagesAndMarks, firstSquareTarget, secondSquareTarget);
	}

	@Override
	public void secondaryFire() {
		List<DamageAndMarks> firstSquareDamage = new ArrayList<>();
		List<DamageAndMarks> secondSquareDamage = new ArrayList<>();
		for (int i = 0; i < currentTargets.size(); i++) {
			firstSquareDamage.add(secondaryDamagesAndMarks.get(0));
		}
		for (int i = 0; i < secondSquareTargets.size(); i++) {
			firstSquareDamage.add(secondaryDamagesAndMarks.get(1));
		}

		dealDamage(firstSquareDamage, currentTargets);
		dealDamage(secondSquareDamage, secondSquareTargets);
	}


	@Override
	public List<Player> getPrimaryTargets() {
		Coordinates nextSquare = getGameMap().getCoordinatesFromDirection(getGameMap().getPlayerCoordinates(getOwner()), chosenDirection);
		return getGameMap().getPlayersFromCoordinates(nextSquare);
	}

	private List<Player> getSecondSquareTargets() {
		Coordinates nextSquare = getGameMap().getCoordinatesFromDirection(getGameMap().getPlayerCoordinates(getOwner()), chosenDirection);
		Coordinates nextNextSquare = getGameMap().getCoordinatesFromDirection(nextSquare, chosenDirection);
		if(nextNextSquare != null){
			return getGameMap().getPlayersFromCoordinates(nextNextSquare);
		}
		return new ArrayList<>();
	}

	@Override
	public List<Player> getSecondaryTargets() {
		Coordinates nextSquare = getGameMap().getCoordinatesFromDirection(getGameMap().getPlayerCoordinates(getOwner()), chosenDirection);
		Coordinates nextNextSquare = getGameMap().getCoordinatesFromDirection(nextSquare, chosenDirection);
		List<Player> targets = getGameMap().getPlayersFromCoordinates(nextSquare);
		targets.addAll(getGameMap().getPlayersFromCoordinates(nextNextSquare));
		return targets;
	}


	@Override
	public void reset() {
		chosenDirection = null;
		firstSquareTarget = null;
		secondSquareTarget = null;
		secondSquareTargets = new ArrayList<>();
	}
}