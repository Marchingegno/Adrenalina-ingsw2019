package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

/**
 * This abstract class implements the powerup card
 * @author MarcerAndrea
 */
public abstract class PowerupCard extends Card {

	private AmmoType associatedAmmo;

	public PowerupCard(AmmoType associatedAmmo, String description){
		super(description);
		this.associatedAmmo = associatedAmmo;
	}

	public abstract void activatePowerup(Player activatingPlayer);

	/**
	 * @return the ammo associated with the card
	 */
	public AmmoType getAssociatedAmmo() {return associatedAmmo;}

}