package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the weapon Flamethrower.
 */
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
        this.setSecondaryDamage(parameters.get("secondaryDamage").getAsInt());
        this.setSecondaryMarks(parameters.get("secondaryMarks").getAsInt());
        this.getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
        this.getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
        getSecondaryDamagesAndMarks().add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));
        getSecondaryDamagesAndMarks().add(new DamageAndMarks(SECONDARY_FOLLOWING_DAMAGE, SECONDARY_FOLLOWING_MARKS));
        this.availableDirections = new ArrayList<>();
    }

    @Override
    QuestionContainer handlePrimaryFire(int choice) {
        switch (getCurrentStep()) {
            case 2:
                return handleDirectionChoice(choice);
            case 3:
                handleDirectionChoice(choice);
                setCurrentTargets(getPrimaryTargets());
                //If there are no targets on the first square, ask second square target.
                //So I increment the step and re-call this method.
                if (getCurrentTargets().isEmpty()) {
                    incrementCurrentStep();
                    return handlePrimaryFire(0);
                }
                return getTargetPlayersQnO(getCurrentTargets());
            case 4:
                try {
                    firstSquareTarget = getCurrentTargets().get(choice);
                } catch (IndexOutOfBoundsException e) {
                    Utils.logInfo("There are no players in the first square chosen by " + getOwner().getPlayerName() + ".");
                    firstSquareTarget = null;
                }

                setCurrentTargets(getSecondSquareTargets());
                try {
                    if (getCurrentTargets().isEmpty()) {
                        incrementCurrentStep();
                        return handlePrimaryFire(0);
                    }
                } catch (NullPointerException e) {
                    Utils.logError("Flamethrower: currentTargets is null", e);
                }

                return getTargetPlayersQnO(getCurrentTargets());
            case 5:
                try {
                    secondSquareTarget = getCurrentTargets().get(choice);
                } catch (IndexOutOfBoundsException e) {
                    Utils.logInfo("There are no players in the second square chosen by " + getOwner().getPlayerName() + ".");
                    secondSquareTarget = null;
                }
                primaryFire();
                break;
            default:
                throw new IllegalStateException("Reached an illegal state.");
        }
        return null;
    }


    @Override
    QuestionContainer handleSecondaryFire(int choice) {
        if (getCurrentStep() == 2) {
            return handleDirectionChoice(choice);
        } else if (getCurrentStep() == 3) {
            handleDirectionChoice(choice);
            setCurrentTargets(getSecondaryTargets());
            secondSquareTargets = getSecondSquareTargets();
            secondaryFire();
        }
        return null;
    }

    /**
     * Handles the choosing of a direction by the owner.
     *
     * @param choice the choice of the owner.
     * @return the QuestionContainer to be asked to the owner.
     */
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
        dealDamageAndConclude(getStandardDamagesAndMarks(), firstSquareTarget, secondSquareTarget);
    }

    @Override
    public void secondaryFire() {
        List<DamageAndMarks> firstSquareDamage = new ArrayList<>();
        List<DamageAndMarks> secondSquareDamage = new ArrayList<>();
        for (int i = 0; i < getCurrentTargets().size(); i++) {
            firstSquareDamage.add(getSecondaryDamagesAndMarks().get(0));
        }
        for (int i = 0; i < secondSquareTargets.size(); i++) {
            secondSquareDamage.add(getSecondaryDamagesAndMarks().get(1));
        }

        dealDamageAndConclude(firstSquareDamage, getCurrentTargets());
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
    boolean canSecondaryBeFired() {
        return canPrimaryBeActivated();
    }
}