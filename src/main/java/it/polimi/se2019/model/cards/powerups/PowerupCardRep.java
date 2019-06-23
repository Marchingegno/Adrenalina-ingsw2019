package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.CardRep;
import it.polimi.se2019.model.cards.ammo.AmmoType;

public class PowerupCardRep extends CardRep {

	private AmmoType associatedAmmo;
	private PowerupCard.PowerupUseCaseType powerupUseCaseType;
	private String imagePath;

	public PowerupCardRep(PowerupCard powerupCard) {
		super(powerupCard);
		this.associatedAmmo = powerupCard.getAssociatedAmmo();
		this.powerupUseCaseType = powerupCard.getUseCase();
		this.imagePath = powerupCard.getImagePath();
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
	public PowerupCard.PowerupUseCaseType getUseCase() {
		return powerupUseCaseType;
	}

	/**
	 * Returns the image path.
	 *
	 * @return the image path.
	 */
	public String getImagePath() {
		return imagePath;
	}

	@Override
	public String toString() {
		return getCardName() + "_" + associatedAmmo.getCharacterColorType();
	}
}
