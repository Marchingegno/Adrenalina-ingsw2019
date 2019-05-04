package it.polimi.se2019.model.cards.ammo;

import it.polimi.se2019.model.cards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the ammo card.
 *
 * @author MarcerAndrea
 */
public class AmmoCard extends Card {

	private ArrayList<AmmoType> ammo;
	private boolean hasPowerup;


	public AmmoCard(List<AmmoType> ammo, boolean hasPowerup) {

		super(createDescription(ammo, hasPowerup));

		this.ammo = new ArrayList<>(ammo);
		this.hasPowerup = hasPowerup;
	}


	/**
	 * Generates the description of the card.
	 *
	 * @param ammo       array of the ammo associated with the card.
	 * @param hasPowerup true if the card has a powerup associated.
	 * @return the description of the card.
	 */
	private static String createDescription(List<AmmoType> ammo, boolean hasPowerup) {

		StringBuilder description = new StringBuilder();

		for (AmmoType ammoType : ammo) {
			description.append(ammoType.toString());
		}

		if (hasPowerup)
			description.append("and a Powerup");

		return description.toString();
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

	@Override
	public String toString() {
		return "AmmoCard: " + description;
	}

}