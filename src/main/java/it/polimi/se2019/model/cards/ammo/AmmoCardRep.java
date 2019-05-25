package it.polimi.se2019.model.cards.ammo;

import it.polimi.se2019.model.cards.CardRep;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the representation of an ammo card.
 *
 * @author MarcerAndrea
 */
public class AmmoCardRep extends CardRep {

	private List<AmmoType> ammo;
	private boolean hasPowerup;

	public AmmoCardRep(AmmoCard ammoCard) {
		super(ammoCard);
		this.ammo = ammoCard.getAmmo();
		this.hasPowerup = ammoCard.hasPowerup();
	}

	/**
	 * Returns true if and only if the card has a powerup associated.
	 *
	 * @return true if and only if the card has a powerup associated.
	 */
	public boolean hasPowerup() {
		return hasPowerup;
	}

	/**
	 * Returns a copy of the list of ammo associated with the card.
	 *
	 * @return a copy of the list of ammo associated with the card.
	 */
	public List<AmmoType> getAmmo() {
		return new ArrayList<>(ammo);
	}

}
