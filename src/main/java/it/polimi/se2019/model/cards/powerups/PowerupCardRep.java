package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.CardRep;
import it.polimi.se2019.model.cards.ammo.AmmoType;

public class PowerupCardRep extends CardRep {

    private AmmoType associatedAmmo;
    private PowerupCard.PowerupUseCaseType powerupUseCaseType;

    public PowerupCardRep(PowerupCard powerupCard) {
        super(powerupCard);
        this.associatedAmmo = powerupCard.getAssociatedAmmo();
        this.powerupUseCaseType = powerupCard.getUseCase();
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
    PowerupCard.PowerupUseCaseType getUseCase() {
        return powerupUseCaseType;
    }

    @Override
    public String toString() {
        return getCardName() + "_" + associatedAmmo.getCharacterColorType();
    }
}
