package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
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
	private List<String> availableDirections;
	private CardinalDirection chosenDirection;
	private Player firstSquareTarget;
	private Player secondSquareTarget;
	private List<Player> secondSquareTargets;

	public Flamethrower(JsonObject parameters) {
		super(parameters);
		this.secondaryDamage = parameters.get("secondaryDamage").getAsInt();
		this.secondaryMarks = parameters.get("secondaryMarks").getAsInt();
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		secondaryDamagesAndMarks = new ArrayList<>();
		secondaryDamagesAndMarks.add(new DamageAndMarks(secondaryDamage, secondaryMarks));
		secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_FOLLOWING_DAMAGE, SECONDARY_FOLLOWING_MARKS));
		this.maximumSteps = 5;
		this.maximumAlternateSteps = 3;
		this.availableDirections = new ArrayList<>();
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		switch (getCurrentStep()) {
			case 2:
				return handleDirectionChoice(choice);
			case 3:
				handleDirectionChoice(choice);
				currentTargets = getPrimaryTargets();
				//If there are no targets on the first square, ask second square target.
				//So I increment the step and re-call this method.
				if (currentTargets.isEmpty()) {
					incrementCurrentStep();
					return handlePrimaryFire(0);
				}
				return getTargetPlayersQnO(currentTargets);
			case 4:
				try {
					firstSquareTarget = currentTargets.get(choice);
				} catch (IndexOutOfBoundsException e) {
					Utils.logInfo("There are no players in the first square chosen by " + getOwner().getPlayerName() + ".");
					firstSquareTarget = null;
				}

				currentTargets = getSecondSquareTargets();
				try {
					if (currentTargets.isEmpty()) {
						incrementCurrentStep();
						return handlePrimaryFire(0);
					}
				} catch (NullPointerException e) {
					Utils.logError("Flamethrower: currentTargets is null", e);
				}

				return getTargetPlayersQnO(currentTargets);
			case 5:
				try {
					secondSquareTarget = currentTargets.get(choice);
				} catch (IndexOutOfBoundsException e) {
					Utils.logInfo("There are no players in the second square chosen by " + getOwner().getPlayerName() + ".");
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

	private QuestionContainer handleDirectionChoice(int choice) {
		if (getCurrentStep() == 2) {
			availableDirections = getAvailableDirections();
			return getCardinalQnO(availableDirections);
		} else if (getCurrentStep() == 3) {
			chosenDirection = Enum.valueOf(CardinalDirection.class, availableDirections.get(choice));
		}
		return null;
	}

	@Override
	public void primaryFire() {
		dealDamageAndConclude(standardDamagesAndMarks, firstSquareTarget, secondSquareTarget);
	}

	@Override
	public void secondaryFire() {
		List<DamageAndMarks> firstSquareDamage = new ArrayList<>();
		List<DamageAndMarks> secondSquareDamage = new ArrayList<>();
		for (int i = 0; i < currentTargets.size(); i++) {
			firstSquareDamage.add(secondaryDamagesAndMarks.get(0));
		}
		for (int i = 0; i < secondSquareTargets.size(); i++) {
			secondSquareDamage.add(secondaryDamagesAndMarks.get(1));
		}

		dealDamageAndConclude(firstSquareDamage, currentTargets);
		dealDamageAndConclude(secondSquareDamage, secondSquareTargets);
	}


	@Override
	public List<Player> getPrimaryTargets() {
		Coordinates nextSquare = getGameMap().getCoordinatesFromDirection(getGameMap().getPlayerCoordinates(getOwner()), chosenDirection);
		if (nextSquare != null)
			return getGameMap().getPlayersFromCoordinates(nextSquare);
		else
			return new ArrayList<>();
	}

	private List<Player> getSecondSquareTargets() {
		Coordinates nextSquare = getGameMap().getCoordinatesFromDirection(getGameMap().getPlayerCoordinates(getOwner()), chosenDirection);
		if (nextSquare != null) {
			Coordinates nextNextSquare = getGameMap().getCoordinatesFromDirection(nextSquare, chosenDirection);
			if (nextNextSquare != null) {
				return getGameMap().getPlayersFromCoordinates(nextNextSquare);
			}
		}
		return new ArrayList<>();
	}

	@Override
	public List<Player> getSecondaryTargets() {
		Coordinates nextSquare = getGameMap().getCoordinatesFromDirection(getGameMap().getPlayerCoordinates(getOwner()), chosenDirection);
		if (nextSquare != null) {
			List<Player> targets = getGameMap().getPlayersFromCoordinates(nextSquare);
			Coordinates nextNextSquare = getGameMap().getCoordinatesFromDirection(nextSquare, chosenDirection);
			if (nextNextSquare != null)
				targets.addAll(getGameMap().getPlayersFromCoordinates(nextNextSquare));
			return targets;
		}
		return new ArrayList<>();
	}


	@Override
	public void reset() {
		super.reset();
		chosenDirection = null;
		firstSquareTarget = null;
		secondSquareTarget = null;
		secondSquareTargets = new ArrayList<>();
	}

	private List<String> getAvailableDirections() {
		List<String> directionsFound = new ArrayList<>();
		for (CardinalDirection direction : CardinalDirection.values()) {
			chosenDirection = direction;
			List<Player> possibleTargets = getPrimaryTargets();
			possibleTargets.addAll(getSecondSquareTargets());
			chosenDirection = null;
			if (!possibleTargets.isEmpty()) {
				directionsFound.add(direction.toString());
			}
		}
		return directionsFound;
	}

	@Override
	public boolean canPrimaryBeActivated() {
		//There's at least one player in two squares away in one direction.
		return !getAvailableDirections().isEmpty();
	}

	@Override
	protected boolean canSecondaryBeActivated() {
		return canPrimaryBeActivated() && getOwner().hasEnoughAmmo(secondaryPrice);
	}
}