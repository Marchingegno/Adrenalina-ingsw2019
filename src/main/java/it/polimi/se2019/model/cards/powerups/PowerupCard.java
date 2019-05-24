package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.ActivableCard;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.player.Player;

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
	private WeaponCard shootingWeapon;


	public PowerupCard(String name, AmmoType associatedAmmo, String description, PowerupUseCaseType powerupUseCaseType) {
		super(name, description);
		this.associatedAmmo = associatedAmmo;
		this.powerupUseCaseType = powerupUseCaseType;
	}


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

	public void setShootingPlayer(Player shootingPlayer) {
		this.shootingPlayer = shootingPlayer;
	}

	public void setShootingWeapon(WeaponCard shootingWeapon) {
		this.shootingWeapon = shootingWeapon;
	}


	// ####################################
	// PROTECTED METHODS (only for subclasses)
	// ####################################

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