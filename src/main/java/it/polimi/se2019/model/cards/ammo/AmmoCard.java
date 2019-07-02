package it.polimi.se2019.model.cards.ammo;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the ammo card.
 *
 * @author MarcerAndrea
 */
public class AmmoCard extends Card {

	private List<AmmoType> ammo;
	private boolean hasPowerup;


	public AmmoCard(List<AmmoType> ammo, boolean hasPowerup, String name, String imagePath) {
		super(name, createDescription(ammo, hasPowerup), imagePath);
		this.ammo = new ArrayList<>(ammo);
		this.hasPowerup = hasPowerup;
	}


	// ####################################
	// PUBLIC METHODS
	// ####################################

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
			description.append(ammoType.toString().toLowerCase());
			description.append(" ");
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


	// ####################################
	// OVERRIDDEN METHODS
	// ####################################

	/**
	 * Returns a copy of the list of ammo associated with the card.
	 *
	 * @return a copy of the list of ammo associated with the card.
	 */
	public List<AmmoType> getAmmo() {
		return new ArrayList<>(ammo);
	}

	// ####################################
	// PRIVATE METHODS
	// ####################################

	@Override
	public Representation getRep() {
		return new AmmoCardRep(this);
	}
}