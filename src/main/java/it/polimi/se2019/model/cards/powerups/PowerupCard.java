package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.network.message.Message;

/**
 * This abstract class implements the powerup card
 *
 * @author MarcerAndrea
 * @author Desno365
 */
public abstract class PowerupCard extends Card {

	private AmmoType associatedAmmo;
	private PowerupUseCaseType powerupUseCaseType;
	private GameBoard gameBoard;
	private Player ownerPlayer;
	private Player shootingPlayer;
	private WeaponCard shootingWeapon;


	public PowerupCard(String name, AmmoType associatedAmmo, String description, PowerupUseCaseType powerupUseCaseType) {
		super(name, description);
		this.associatedAmmo = associatedAmmo;
		this.powerupUseCaseType = powerupUseCaseType;
	}


	// ####################################
	// ABSTRACT METHODS
	// ####################################

	/**
	 * Do one step of the powerup.
	 * @param answer the answer given by the user. Can null if it's the first step.
	 * @return a PowerupInfo object with the info for the next request.
	 */
	public abstract PowerupInfo doPowerupStep(Message answer);

	/**
	 * Returns true if this powerup card can be activated in the current conditions.
	 * Note: depending on the powerup this method may need setting the shootingPlayer or the shootingWeapon.
	 * @return true if this powerup card can be activated in the current conditions.
	 */
	public abstract boolean canBeActivated();


	// ####################################
	// PUBLIC METHODS
	// ####################################

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

	public void setGameBoard(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

	public void setOwnerPlayer(Player ownerPlayer) {
		this.ownerPlayer = ownerPlayer;
	}

	public void setShootingPlayer(Player shootingPlayer) {
		this.shootingPlayer = shootingPlayer;
	}

	public void setShootingWeapon(WeaponCard shootingWeapon) {
		this.shootingWeapon = shootingWeapon;
	}


	// ####################################
	// PROTECTED METHODS (only for specific type of powerups)
	// ####################################

	protected GameBoard getGameBoard() {
		return gameBoard;
	}

	protected Player getOwnerPlayer() {
		return ownerPlayer;
	}

	protected Player getShootingPlayer() {
		return shootingPlayer;
	}

	protected WeaponCard getShootingWeapon() {
		return shootingWeapon;
	}


	// ####################################
	// OVERRIDDEN METHODS
	// ####################################

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