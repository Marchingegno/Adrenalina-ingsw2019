package it.polimi.se2019.model.cards.ammo;

import it.polimi.se2019.model.cards.Card;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the ammo card
 * @author MarcerAndrea
 */
public class AmmoCard extends Card {

	private ArrayList<AmmoType> ammos;
	private boolean hasPowerup;


	public AmmoCard(List<AmmoType> ammos, boolean hasPowerup) {

		super(createDescription(ammos, hasPowerup));

		this.ammos = new ArrayList<>(ammos);
		this.hasPowerup = hasPowerup;
	}


	/**
	 * @param ammos array of the ammos associated with the card
	 * @param hasPowerup true if the card has a powerup associated
	 * @return the description of the card
	 */
	private static String createDescription(List<AmmoType> ammos, boolean hasPowerup){

		StringBuilder description = new StringBuilder();

		for (AmmoType ammoType:ammos) {	description.append(ammoType.toString()); }

		if(hasPowerup)
			description.append("and has powerup");

		return description.toString();
	}

	/**
	 * @return true if and only of the card has a powerup associated
	 */
	public boolean hasPowerup() {
		return hasPowerup;
	}

	/**
	 * @return a copy of the list of ammos associated with the card
	 */
	public List<AmmoType> getAmmos() {
		return new ArrayList<>(ammos);
	}

}