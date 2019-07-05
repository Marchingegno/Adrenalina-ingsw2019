package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.ActivableCard;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class implements the powerup card
 *
 * @author MarcerAndrea
 * @author Desno365
 */
public abstract class PowerupCard extends ActivableCard {

	private AmmoType associatedAmmo;
	private PowerupUseCaseType powerupUseCaseType;
	private Player shootingPlayer;
	private List<Player> shotPlayers;


	public PowerupCard(String name, AmmoType associatedAmmo, String description, PowerupUseCaseType powerupUseCaseType, String imagePath) {
		super(name, description, imagePath);
		this.associatedAmmo = associatedAmmo;
		this.powerupUseCaseType = powerupUseCaseType;
	}


	// ####################################
	// ABSTRACT METHODS
	// ####################################

	/**
	 * First step of the powerup.
	 *
	 * @return the next question container to show to the player.
	 */
	protected abstract QuestionContainer firstStep();

	/**
	 * Second step of the powerup.
	 *
	 * @return the next question container to show to the player.
	 */
	protected abstract QuestionContainer secondStep(int choice);

	/**
	 * Third step of the powerup.
	 *
	 * @return the next question container to show to the player.
	 */
	protected abstract QuestionContainer thirdStep(int choice);


	// ####################################
	// PUBLIC METHODS
	// ####################################

	/**
	 * Returns the initial question and options.
	 *
	 * @return the initial question and options.
	 */
	@Override
	public QuestionContainer initialQuestion() {
		incrementCurrentStep();
		return firstStep();
	}

	/**
	 * Performs the powerup action according to the player choice.
	 *
	 * @param choice the choice of the player.
	 * @return question and options to send to the player.
	 */
	@Override
	public QuestionContainer doActivationStep(int choice) {
		incrementCurrentStep();
		if (getCurrentStep() == 2) {
			return secondStep(choice);
		} else if (getCurrentStep() == 3) {
			return thirdStep(choice);
		} else {
			throw new IllegalStateException("Wrong progress.");
		}
	}

	/**
	 * Returns the ammo associated with the card.
	 *
	 * @return the ammo associated with the card.
	 */
	public AmmoType getAssociatedAmmo() {
		return associatedAmmo;
	}

	/**
	 * Returns the use case of the powerup.
	 *
	 * @return the use case of the powerup.
	 */
	public PowerupUseCaseType getUseCase() {
		return powerupUseCaseType;
	}

	/**
	 * Sets the player that shoot with a weapon.
	 *
	 * @param shootingPlayer the player shooting.
	 */
	public void setShootingPlayer(Player shootingPlayer) {
		this.shootingPlayer = shootingPlayer;
	}

	// ####################################
	// PROTECTED METHODS (only for subclasses)
	// ####################################

	/**
	 * Returns the player that shoot with a weapon.
	 *
	 * @return the player that shoot with a weapon.
	 */
	protected Player getShootingPlayer() {
		return shootingPlayer;
	}

	/**
	 * Returns the list of players shot by a weapon.
	 *
	 * @return the list of players shot by a weapon.
	 */
	protected List<Player> getShotPlayers() {
		return shotPlayers;
	}

	/**
	 * Sets the list of players shot by a weapon.
	 *
	 * @param shotPlayers the list of players shot by a weapon.
	 */
	public void setShotPlayers(List<Player> shotPlayers) {
		this.shotPlayers = new ArrayList<>(shotPlayers);
	}


	// ####################################
	// OVERRIDDEN METHODS
	// ####################################

	@Override
	public String toString() {
		return getCardName() + " " + getAssociatedAmmo();
	}

	@Override
	public Representation getRep() {
		return new PowerupCardRep(this);
	}


	// ####################################
	// ENUM
	// ####################################

	public enum PowerupUseCaseType {
		ON_TURN, ON_DAMAGE, ON_SHOOT
	}
}