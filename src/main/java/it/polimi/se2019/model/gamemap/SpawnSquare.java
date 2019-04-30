package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.GameBoard;
import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.utils.GameConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Spawn square associated with an ammo type that represents its color
 * @author MarcerAndrea
 */
public class SpawnSquare extends MapSquare {

	private AmmoType ammoType;

	public SpawnSquare(AmmoType ammoType, int roomID, boolean[] possibleDirections, Coordinates coordinates, GameBoard gameBoard) {
		super(possibleDirections, roomID, coordinates);
		deck = gameBoard.getWeaponDeck();
		this.ammoType = ammoType;
	}

	/**
	 * @return the ammo type associated with the spawn square
	 */
	public AmmoType getAmmoType() {
		return ammoType;
	}

	public void refillCards() {
		for (int i = 0; i < GameConstants.MAX_NUM_OF_WEAPONS_IN_SPAWN_SQUARE; i++) {
			cards.add(deck.drawCard());
		}
	}

	public void addCard(WeaponCard weapon) {
		if (cards.size() < GameConstants.MAX_NUM_OF_WEAPONS_IN_SPAWN_SQUARE)
			cards.add(weapon);
		else
			throw new OutOfBoundariesException("Trying to add a weapon to a filled Spawn square: SpawnSquare refillCards(weapon)");
	}

	public Card grabCard(int index) {
		if (cards == null)
			throw new NullPointerException("Ammo Square without ammo card");
		return cards.remove(index);
	}

	public String[] getElementsToPrint(){
		String[] elementsToPrint = new String[3];
		elementsToPrint[0] = "S";
		elementsToPrint[1] = "P";
		elementsToPrint[2] = "W";
		return elementsToPrint;
	}

	public List<Card> getWeapons(){
		return new ArrayList<>(cards);
	}
}