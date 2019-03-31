package it.polimi.se2019.Model.Cards.Powerups;

import it.polimi.se2019.Model.Cards.Ammo.AmmoType;
import it.polimi.se2019.Model.Cards.Card;
import it.polimi.se2019.Model.Player.Player;

import java.util.*;

/**
 * This class implements Strategy Pattern
 */
public abstract class PowerupCard extends Card {

	/**
	 * Default constructor
	 */
	public PowerupCard() {
	}

	/**
	 *
	 */
	private AmmoType associatedAmmo;


	/**
	 * @param associatedAmmo
	 * @param description
	 */
	public void PowerupCard(AmmoType associatedAmmo, String description) {
		// TODO implement here
	}

	/**
	 * @param targetPlayer
	 */
	public abstract void activatePowerup(Player targetPlayer);

	/**
	 * @return
	 */
	public AmmoType getAssociatedAmmo() {
		// TODO implement here
		return null;
	}

}