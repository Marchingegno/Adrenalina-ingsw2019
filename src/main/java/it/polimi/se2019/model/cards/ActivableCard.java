package it.polimi.se2019.model.cards;

import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

public abstract class ActivableCard extends Card {

    private Player ownerPlayer;
    private GameBoard gameBoard;
    private int currentStep = 0;
    private boolean activationConcluded;


    public ActivableCard(String cardName, String description, String imagePath) {
        super(cardName, description, imagePath);
        activationConcluded = false;
    }

    public boolean isActivationConcluded() {
        return activationConcluded;
    }


    // ####################################
    // ABSTRACT METHODS
    // ####################################

    /**
     * Returns true if this card can be activated in the current conditions.
     *
     * @return true if this card can be activated in the current conditions.
     */
    public abstract boolean canBeActivated();


    /**
     * Creates a {@link QuestionContainer} to be passed to the view.
     *
     * @return the {@link QuestionContainer}.
     */
    public abstract QuestionContainer initialQuestion();


    /**
     * Handles the activation process of the card based on the currentStep and the choice of the player.
     *
     * @param choice the choice of the player.
     * @return the {@link QuestionContainer} to be passed to the view, or **null** if the process has finished.
     */
    public abstract QuestionContainer doActivationStep(int choice);


    // ####################################
    // PUBLIC METHODS
    // ####################################

    protected Player getOwner() {
        return ownerPlayer;
    }

    public void setOwner(Player player) {
        ownerPlayer = player;
    }


    // ####################################
    // PROTECTED METHODS (only for subclasses)
    // ####################################

    protected GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    protected int getCurrentStep() {
        return currentStep;
    }

    protected void incrementCurrentStep() {
        currentStep++;
    }

    protected void resetCurrentStep() {
        currentStep = 0;
    }

    /**
     * Called when the card has finished its activation.
     */
    protected void concludeActivation() {
        activationConcluded = true;
    }

    /**
     * Reset the card to be used again.
     */
    public void reset() {
        resetCurrentStep();
        activationConcluded = false;
    }
}
