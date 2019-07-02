package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class of the weapon Power glove.
 */
public class PowerGlove extends AlternateFireWeapon {

    private List<CardinalDirection> targettableDirections;
    private CardinalDirection targetDirection;
    private Coordinates targetCoordinates;

    // Attributes for alternate fire.
    private List<Player> secondaryTargets = new ArrayList<>();
    private boolean isLongFly = false;
    private int longFlyChoice = 0;

    public PowerGlove(JsonObject parameters) {
        super(parameters);
        this.setSecondaryDamagesAndMarks(new ArrayList<>());
        this.getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
        this.getSecondaryDamagesAndMarks().add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));
        this.getSecondaryDamagesAndMarks().add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));
    }


    // ####################################
    // OVERRIDDEN METHODS
    // ####################################

    @Override
    public void reset() {
        super.reset();
        secondaryTargets = new ArrayList<>();
        isLongFly = false;
        longFlyChoice = 0;
    }

    @Override
    QuestionContainer handlePrimaryFire(int choice) {
        if (getCurrentStep() == 2) {
            setCurrentTargets(getPrimaryTargets());
            return getTargetPlayersQnO(getCurrentTargets());
        }
        if (getCurrentStep() == 3) {
            setTarget(getCurrentTargets().get(choice));
            targetCoordinates = getGameMap().getPlayerCoordinates(getTarget());
            primaryFire();
        }
        return null;
    }

    @Override
    QuestionContainer handleSecondaryFire(int choice) {
        if (getCurrentStep() == 2) {
            return askDirection();
        } else if (getCurrentStep() == 3) {
            saveDirectionChoice(choice);
            return askFistDistance();
        } else if (getCurrentStep() == 4) {
            saveFistDistanceChoice(choice);
            return initialAskTargets();
        } else if (getCurrentStep() == 5) {
            saveTargetChoice(choice);
            return endOrSecondAskTargets();
        } else if (getCurrentStep() == 6) {
            saveTargetChoice(choice);
            endInSecondSquare();
        }
        return null;
    }

    @Override
    public void primaryFire() {
        relocateOwner(targetCoordinates);
        dealDamageAndConclude(getStandardDamagesAndMarks(), getTarget());
    }

    @Override
    public void secondaryFire() {
        relocateOwner(targetCoordinates);
        dealDamageAndConclude(getSecondaryDamagesAndMarks(), getSecondaryTargets());
    }

    @Override
    public List<Player> getSecondaryTargets() {
        return secondaryTargets;
    }

    @Override
    public List<Player> getPrimaryTargets() {
        List<Player> enemiesOneMoveAway = getGameMap().reachablePlayers(getOwner(), 1);
        enemiesOneMoveAway.removeAll(getGameMap().getPlayersFromCoordinates(getGameMap().getPlayerCoordinates(getOwner())));
        return enemiesOneMoveAway;
    }

    @Override
    boolean canSecondaryBeFired() {
        return !getTargettableDirections().isEmpty();
    }


    // ####################################
    // PRIVATE METHODS FOR ALTERNATE FIRE STEPS
    // ####################################

    /**
     * Finds directions and returns the Question Container to ask to the player.
     *
     * @return the Question Container.
     */
    private QuestionContainer askDirection() {
        targettableDirections = getTargettableDirections();
        List<String> directionNames = targettableDirections.stream().map(Enum::toString).collect(Collectors.toList());
        return QuestionContainer.createStringQuestionContainer("In which direction do you want to fly?", directionNames);
    }

    /**
     * Saves the chosen direction.
     *
     * @param choice the choice of the player.
     */
    private void saveDirectionChoice(int choice) {
        targetDirection = targettableDirections.get(choice);
    }

    /**
     * Finds the possible flying distances.
     *
     * @return the Question Container to ask to the player.
     */
    private QuestionContainer askFistDistance() {
        List<String> distanceAnswers = getPossibleDistanceAnswers();
        return QuestionContainer.createStringQuestionContainer("How long do you want to fly?", distanceAnswers);
    }

    /**
     * Saves the chosen distance of flight.
     *
     * @param choice the choice of the player.
     */
    private void saveFistDistanceChoice(int choice) {
        if (choice == longFlyChoice)
            isLongFly = true;
    }

    /**
     * Asks which player to target of the first square if it isn't empty.
     * If the first square is empty, asks a target of the second square.
     *
     * @return the QuestionContainer for the first or second target question.
     */
    private QuestionContainer initialAskTargets() {
        setCurrentTargets(getFirstSquareTargets());
        if (getCurrentTargets().isEmpty() && isLongFly) {
            incrementCurrentStep(); // Skip step 5.
            setCurrentTargets(getSecondSquareTargets());
            return getTargetPlayersQnO(getCurrentTargets(), "Which of the following players, of the second square, do you want to target?");
        }
        return getTargetPlayersQnO(getCurrentTargets(), "Which of the following players, of the first square, do you want to target?");
    }

    /**
     * Asks which player to target of the second square, if it isn't empty and if the player chose to fly for 2 squares.
     * If the player chose to fly only for 1 square, move it and damage the chosen target.
     *
     * @return the QuestionContainer for the second target question.
     */
    private QuestionContainer endOrSecondAskTargets() {
        if (isLongFly)
            return secondAskTargets();
        else
            endInFirstSquare();

        return null;
    }

    /**
     * Saves the chosen target.
     *
     * @param choice the choice of the player.
     */
    private void saveTargetChoice(int choice) {
        secondaryTargets.add(getCurrentTargets().get(choice));
    }

    /**
     * Called if the player ended in the second square.
     */
    private void endInSecondSquare() {
        targetCoordinates = getSecondSquareCoordinates();
        secondaryFire();
    }


    // ####################################
    // PRIVATE HELPER METHODS FOR ALTERNATE FIRE
    // ####################################

    /**
     * Finds the possible targets on the second square and builds the relative Question Container.
     *
     * @return the Question Container to be asked to the player.
     */
    private QuestionContainer secondAskTargets() {
        setCurrentTargets(getSecondSquareTargets());
        if (getCurrentTargets().isEmpty())
            endInSecondSquare();
        else
            return getTargetPlayersQnO(getCurrentTargets(), "Which of the following players, of the second square, do you want to target?");

        return null;
    }

    /**
     * Called if the player ended on the first square.
     */
    private void endInFirstSquare() {
        targetCoordinates = getFirstSquareCoordinates();
        secondaryFire();
    }

    /**
     * Finds the possible Cardinal Directions available to fly to.
     *
     * @return the list of available Cardinal Direction.
     */
    private List<CardinalDirection> getTargettableDirections() {
        ArrayList<CardinalDirection> targettableDirectionsTemp = new ArrayList<>();
        for (CardinalDirection cardinalDirection : CardinalDirection.values()) {
            Coordinates coordinates1 = getGameMap().getCoordinatesFromDirection(getGameMap().getPlayerCoordinates(getOwner()), cardinalDirection);
            if (coordinates1 != null) {
                List<Player> players1 = getGameMap().getPlayersFromCoordinates(coordinates1);
                if (!players1.isEmpty()) {
                    targettableDirectionsTemp.add(cardinalDirection);
                    continue;
                }
                Coordinates coordinates2 = getGameMap().getCoordinatesFromDirection(coordinates1, cardinalDirection);
                if (coordinates2 != null) {
                    List<Player> players2 = getGameMap().getPlayersFromCoordinates(coordinates2);
                    if (!players2.isEmpty())
                        targettableDirectionsTemp.add(cardinalDirection);
                }
            }
        }
        return targettableDirectionsTemp;
    }

    /**
     * Finds the possible options, of the length of flight, from which the player can choose.
     *
     * @return the list of possible options.
     */
    private List<String> getPossibleDistanceAnswers() {
        List<String> distanceAnswers = new ArrayList<>();

        Coordinates coordinates1 = getGameMap().getCoordinatesFromDirection(getGameMap().getPlayerCoordinates(getOwner()), targetDirection);
        if (getGameMap().getPlayersFromCoordinates(coordinates1).isEmpty()) {
            longFlyChoice = 0;
        } else {
            distanceAnswers.add("1 square");
            longFlyChoice = 1;
        }

        Coordinates coordinates2 = getGameMap().getCoordinatesFromDirection(coordinates1, targetDirection);
        if (coordinates2 != null)
            distanceAnswers.add("2 squares");
        return distanceAnswers;
    }

    /**
     * Finds the coordinates of the first square.
     *
     * @return the coordinates of the first square.
     */
    private Coordinates getFirstSquareCoordinates() {
        return getGameMap().getCoordinatesFromDirection(getGameMap().getPlayerCoordinates(getOwner()), targetDirection);
    }

    /**
     * Finds the coordinates of the second square.
     *
     * @return the coordinates of the second square.
     */
    private Coordinates getSecondSquareCoordinates() {
        return getGameMap().getCoordinatesFromDirection(getFirstSquareCoordinates(), targetDirection);
    }

    /**
     * Finds the targets of the first square.
     *
     * @return the list of player in the first square.
     */
    private List<Player> getFirstSquareTargets() {
        return getGameMap().getPlayersFromCoordinates(getFirstSquareCoordinates());
    }

    /**
     * Finds the targets of the second square.
     *
     * @return the list of player in the second square.
     */
    private List<Player> getSecondSquareTargets() {
        return getGameMap().getPlayersFromCoordinates(getSecondSquareCoordinates());
    }

}