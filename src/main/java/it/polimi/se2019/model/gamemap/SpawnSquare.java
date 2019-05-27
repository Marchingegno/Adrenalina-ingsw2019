package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoContainer;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.cards.weapons.WeaponDeck;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

/**
 * Spawn square associated with an ammo type that represents its color
 *
 * @author MarcerAndrea
 */
public class SpawnSquare extends Square {

	private WeaponDeck weaponDeck;
	private AmmoType ammoType;

	public SpawnSquare(int roomID, AmmoType ammoType, Color.CharacterColorType squareColor, boolean[] possibleDirections, Coordinates coordinates, GameBoard gameBoard) {
		super(roomID, possibleDirections, squareColor, coordinates);
		weaponDeck = gameBoard.getWeaponDeck();
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
				if(!weaponDeck.isEmpty())
					cards.add(weaponDeck.drawCard());
			}
			setFilled();
			setChanged();
		} else
			Utils.logInfo("SpawnSquare -> refillCards(): The spawn square is already filled");
	}

	@Override
	public boolean canGrab(Player player) {
		AmmoContainer playerAmmoContainer = player.getPlayerBoard().getAmmoContainer();
		for (Card weaponCard : cards) {
			if (playerAmmoContainer.hasEnoughAmmo(((WeaponCard) weaponCard).getGrabPrice())) {
				System.out.print("-------------------------------------------------------- PLAYER ");
				for (AmmoType ammo : AmmoType.values()) {
					System.out.print(playerAmmoContainer.getAmmo(ammo));
				}

				for (AmmoType ammo : ((WeaponCard) weaponCard).getGrabPrice()) {
					System.out.print(ammo + " ");
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public MessageType getGrabMessageType() {
		return MessageType.GRAB_WEAPON;
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
		Utils.logInfo("SpawnSquare -> grabCard(): Grabbing " + cards.get(index).getCardName() + " from the square");
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
			Utils.logInfo("SpawnSquare -> addCard(): Added " + cardToAdd.getCardName());
		} else
			throw new IllegalArgumentException("Trying to add a weapon to a filled Spawn square: SpawnSquare refillCards(weapon)");
	}

	/**
	 * Returns the spawn square's representation.
	 *
	 * @return the spawn square's representation.
	 */
	@Override
	public Representation getRep() {
		if (hasChanged || squareRep == null) {
			squareRep = new SpawnSquareRep(this);
			setNotChanged();
			if (Utils.DEBUG_REPS)
				Utils.logInfo("SpawnSquare -> getRep(): Updated the square's representation");
		}
		return squareRep;
	}
}