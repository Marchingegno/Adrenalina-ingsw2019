package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.CardRep;
import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.util.List;

/**
 * @author Marchingeno
 */
public class WeaponRep extends CardRep {

    private List<AmmoType> price;
    private boolean isLoaded;

    public WeaponRep(WeaponCard weaponCardToRepresent) {
        super(weaponCardToRepresent);
        price = weaponCardToRepresent.getReloadPrice();
        this.isLoaded = weaponCardToRepresent.isLoaded();
    }

    public String getWeaponName() {
        return getCardName();
    }

    public List<AmmoType> getPrice() {
        return price;
    }

    public boolean isLoaded() {
        return isLoaded;
    }
}
