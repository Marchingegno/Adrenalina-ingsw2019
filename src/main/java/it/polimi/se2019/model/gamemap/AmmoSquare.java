package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoDeck;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.utils.exceptions.InventoryFullException;

/**
 * Normal square associated with an ammo card
 *
 * @author MarcerAndrea
 */
public class AmmoSquare extends Square {

	private AmmoDeck ammoDeck;

	public AmmoSquare(int roomID, Color.CharacterColorType squareColor, boolean[] possibleDirections, Coordinates coordinates, GameBoard gameBoard) {
		super(roomID, possibleDirections, squareColor, coordinates);
		ammoDeck = gameBoard.getAmmoDeck();
		hasChanged = true;
		setNotFilled();
	}

	/**
	 * Refills the card slot with an ammo card.
	 */
	@Override
	public void refillCards() {
		if (!isFilled()) {
			Utils.logInfo("AmmoSquare -> refillCards(): Refilling the ammo square in " + getCoordinates());
			cards.add(ammoDeck.drawCard());
			setFilled();
			setChanged();
		} else
			Utils.logInfo("AmmoSquare -> refillCards(): The ammo square is already filled");
	}

	@Override
	public boolean canGrab(Player player) {
		return isFilled();
	}

	@Override
	public MessageType getGrabMessageType() {
		return MessageType.GRAB_AMMO;
	}

	/**
	 * Removes the ammo card from the square and returns it.
	 * @param index index of the card to grab.
	 * @return the ammo card in the square
	 * @throws IllegalArgumentException if the index is different from 0. there should be only a card in the ammo square.
	 */
	@Override
	public Card grabCard(int index) {
		if (index != 0)
			throw new IllegalArgumentException("This is an ammo square, index can be only 0 and you are asking " + index);
		setNotFilled();
		setChanged();
		Utils.logInfo("AmmoSquare -> grabCard(): Grabbing " + cards.get(index).getCardDescription());
		return cards.remove(index);
	}


	/**
	 * Adds the specified card to the ammo square.
	 *
	 * @param cardToAdd Card to add to the square.
	 * @throws InventoryFullException when the card slot is full and this method is called.
	 * @throws NullPointerException     when the card to add is null.
	 */
	@Override
	public void addCard(Card cardToAdd) {
		if (cardToAdd == null)
			throw new NullPointerException("The card to add is null");
		if (!isFilled()) {
			cards.add(cardToAdd);
			Utils.logInfo("AmmoSquare -> addCard(): Adding to the ammo square " + cardToAdd.getCardDescription());
		} else
			throw new InventoryFullException("The square inventory is full");
	}

	/**
	 * Returns the ammo square's representation.
	 *
	 * @return the ammo square's representation.
	 */
	@Override
	public Representation getRep() {
		if (hasChanged || squareRep == null) {
			squareRep = new AmmoSquareRep(this);
			setNotChanged();
			Utils.logInfo("AmmoSquare -> getRep(): Updated the square's representation");
		}
		return squareRep;
	}
}