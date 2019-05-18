package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.Pair;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Flamethrower extends AlternateFire {
	private int SECONDARY_FOLLOWING_DAMAGE;
	private int SECONDARY_FOLLOWING_MARKS;
	private CardinalDirection chosenDirection;
	private Player firstSquareTarget;
	private Player secondSquareTarget;
	private List<Player> secondSquareTargets;

	public Flamethrower(String description, List<AmmoType> reloadPrice) {
		super("Flame Thrower", description, reloadPrice);
		this.PRIMARY_DAMAGE = 1;
		this.PRIMARY_MARKS = 0;
		this.SECONDARY_DAMAGE = 2;
		this.SECONDARY_MARKS = 0;
		this.SECONDARY_FOLLOWING_DAMAGE = 1;
		this.SECONDARY_FOLLOWING_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		secondaryDamagesAndMarks = new ArrayList<>();
		secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_FOLLOWING_DAMAGE, SECONDARY_FOLLOWING_MARKS));
		this.maximumSteps = 5;
		this.maximumAlternateSteps = 3;
	}


	@Override
	Pair handlePrimaryFire(int choice) {
		switch (getCurrentStep()){
			case 2:
				return handleDirectionChoice(choice);
			case 3:
				handleDirectionChoice(choice);
				currentTargets = getPrimaryTargets();
				//If there are no targets on the first square, ask second square target.
				//So I increment the step and re-call this method.
				if (currentTargets.isEmpty()){
					incrementStep();
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
						incrementStep();
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
	Pair handleSecondaryFire(int choice) {
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

	private Pair handleDirectionChoice(int choice){
		if(getCurrentStep() == 2) {
			return getCardinalQnO();
		}
		else if(getCurrentStep() == 3){
			chosenDirection = CardinalDirection.values()[choice];
		}
		return null;
	}

	@Override
	void primaryFire() {
		List<Player> theTwoTargets = new ArrayList<>();
		//One of the following element, or both, could be null!
		theTwoTargets.add(firstSquareTarget);
		theTwoTargets.add(secondSquareTarget);
		dealDamage(theTwoTargets, standardDamagesAndMarks);
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

		dealDamage(currentTargets, firstSquareDamage);
		dealDamage(secondSquareTargets, secondSquareDamage);
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

	private Pair getCardinalQnO(){
		String question = "In which direction do you wish to fire?";
		List<String> options = 	Arrays.stream(CardinalDirection.values()).map(Enum::toString).collect(Collectors.toList());
		return new Pair<>(question,options);
	}

}