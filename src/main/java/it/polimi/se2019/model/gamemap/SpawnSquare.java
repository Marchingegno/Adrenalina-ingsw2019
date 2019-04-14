package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;

/**
 * Spawn square associated with an ammo type that represents its color
 * @author MarcerAndrea
 */
public class SpawnSquare extends Square {

	private AmmoType ammoType;

	public SpawnSquare(AmmoType ammoType, int roomID, boolean[] possibleDirections, Coordinates coordinates) {
		super(possibleDirections, roomID, coordinates);
		this.ammoType = ammoType;
	}

	@Override
	public Card grabCard() {return null; }

	/**
	 * @return the ammo type associated with the spawn square
	 */
	public AmmoType getAmmoType() {	return ammoType;}

}