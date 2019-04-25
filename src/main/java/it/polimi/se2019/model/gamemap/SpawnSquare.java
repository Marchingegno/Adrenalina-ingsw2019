package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.weapons.WeaponCard;

import java.util.ArrayList;

/**
 * Spawn square associated with an ammo type that represents its color
 * @author MarcerAndrea
 */
public class SpawnSquare extends MapSquare {

	private AmmoType ammoType;
	private boolean isFilled;
	private ArrayList<WeaponCard> weaponCards;

	public SpawnSquare(AmmoType ammoType, int roomID, boolean[] possibleDirections, Coordinates coordinates) {
		super(possibleDirections, roomID, coordinates);
		this.ammoType = ammoType;
	}

	/**
	 * @return the ammo type associated with the spawn square
	 */
	public AmmoType getAmmoType() {
		return ammoType;
	}

	public boolean isFilled() {
		return isFilled;
	}

	public void setFilled() {
		isFilled = true;
	}

	public void setNtoFilled() {
		isFilled = false;
	}

	public WeaponCard grabWeaponCard(int index) {
		WeaponCard removedCard = weaponCards.get(index);
		weaponCards.remove(index);
		return removedCard;
	}

	public void addWeaponCards(WeaponCard weaponCardToAdd) {
		if (weaponCards.size() < 3) {
			weaponCards.add(weaponCardToAdd);
		} else {
			throw new OutOfCapacityException("Trying to add a new weapon to a spawn with already 3 weapons. SpawnSquare: addWeaponCards()");
		}
	}

	public String[] getElementsToPrint(){
		String[] elementsToPrint = new String[3];
		elementsToPrint[0] = " ";
		elementsToPrint[1] = " ";
		elementsToPrint[2] = " ";
		return elementsToPrint;
	}
}

/**
 * Thrown when there are already 3 weapons and is called addWeaponCard
 * @author MarcerAndrea
 */
class OutOfCapacityException extends RuntimeException {

	/**
	 * Constructs an OutOfCapacityException with the specified message.
	 * @param message the detail message.
	 */
	public OutOfCapacityException(String message) {
		super(message);
	}
}