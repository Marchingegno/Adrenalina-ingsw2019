package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.player.Player;

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


	/**
	 * Activates the powerup.
	 *
	 * @param activatingPlayer player who has activated the powerup.
	 */
	public abstract void activatePowerup(Player activatingPlayer);

	public abstract boolean canBeActivated();


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

	protected GameBoard getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

	protected Player getOwnerPlayer() {
		return ownerPlayer;
	}

	public void setOwnerPlayer(Player ownerPlayer) {
		this.ownerPlayer = ownerPlayer;
	}

	protected Player getShootingPlayer() {
		return shootingPlayer;
	}

	public void setShootingPlayer(Player shootingPlayer) {
		this.shootingPlayer = shootingPlayer;
	}

	protected WeaponCard getShootingWeapon() {
		return shootingWeapon;
	}

	public void setShootingWeapon(WeaponCard shootingWeapon) {
		this.shootingWeapon = shootingWeapon;
	}


	@Override
	public Representation getRep() {
		return new PowerupCardRep(this);
	}


	public enum PowerupUseCaseType {
		ON_TURN, ON_DAMAGE, ON_SHOOT
	}
}