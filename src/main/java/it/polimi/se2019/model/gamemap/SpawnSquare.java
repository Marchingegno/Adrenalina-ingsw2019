package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

/**
 * Spawn square associated with an ammo type that represents its color
 *
 * @author MarcerAndrea
 */
public class SpawnSquare extends Square {

	private AmmoType ammoType;

	public SpawnSquare(AmmoType ammoType, Color.CharacterColorType squareColor, boolean[] possibleDirections, Coordinates coordinates, GameBoard gameBoard) {
		super(possibleDirections, squareColor, coordinates);
		deck = gameBoard.getWeaponDeck();
		this.ammoType = ammoType;
		hasChanged = true;
	}

	/**
	 * Returns the ammo type associated with the spawn square.
	 * @return the ammo type associated with the spawn square.
	 */
	public AmmoType getAmmoType() {
		return ammoType;
	}

	/**
	 * Refills the card slot with weapon cards.
	 */
	@Override
	public void refillCards() {
		if (!isFilled()) {
			for (int i = cards.size(); i < GameConstants.MAX_NUM_OF_WEAPONS_IN_SPAWN_SQUARE; i++) {
				Utils.logInfo("SpawnSquare -> refillCards(): Refilling the spawn square in " + getCoordinates());
				cards.add(deck.drawCard());
			}
			setFilled();
			setChanged();
		} else
			Utils.logInfo("SpawnSquare -> refillCards(): The spawn square is already filled");
	}

	/**
	 * Removes the weapon card from the square and returns it.
	 * @param index index of the card to grab.
	 * @return the weapon card in the square.
	 * @throws IllegalArgumentException when the index is negative.
	 */
	@Override
	public Card grabCard(int index) {
		if (index < 0)
			throw new IllegalArgumentException("Index should be positive: " + index);
		setNotFilled();
		setChanged();
		Utils.logInfo("SpawnSquare -> grabCard(): Grabbing " + ((WeaponCard) cards.get(index)).getWeaponName() + " from the square");
		return cards.remove(index);
	}

	/**
	 * Adds a weapon to the card slot.
	 *
	 * @param cardToAdd weapon to add to the square.
	 * @throws IllegalArgumentException when the card slot is full and this method is called.
	 * @throws NullPointerException when the card to add is null.
	 */
	@Override
	public void addCard(Card cardToAdd) {
		if (cardToAdd == null)
			throw new NullPointerException("The card to add is null");
		if (cards.size() < GameConstants.MAX_NUM_OF_WEAPONS_IN_SPAWN_SQUARE) {
			cards.add(cardToAdd);
			setChanged();
			Utils.logInfo("SpawnSquare -> addCard(): Added " + ((WeaponCard) cardToAdd).getWeaponName());
		} else
			throw new IllegalArgumentException("Trying to add a weapon to a filled Spawn square: SpawnSquare refillCards(weapon)");
	}

	/**
	 * Returns the spawn square's representation.
	 *
	 * @return the spawn square's representation.
	 */
	@Override
	public SquareRep getRep() {
		if (hasChanged || squareRep == null) {
			squareRep = new SquareSpawnRep(this);
			setNotChanged();
			Utils.logInfo("SpawnSquare -> getRep(): Updated the square's representation");
		}
		return squareRep;
	}
}