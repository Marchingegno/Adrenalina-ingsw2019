package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.Representable;
import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.CardRep;
import it.polimi.se2019.model.cards.Deck;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represent a square in the map.
 *
 * @author MarcerAndrea
 */
public abstract class Square implements Representable {

	protected int roomID;
	protected boolean hasChanged;
	protected SquareRep squareRep;
	private Coordinates coordinates;
	protected List<Card> cards;
	protected Deck<Card> deck;
	protected boolean[] possibleDirections;
	private Color.CharacterColorType squareColor;
	private boolean isFilled;
	private List<Square> adjacentSquares;


	public Square(int roomID, boolean[] possibleDirections, Color.CharacterColorType squareColor, Coordinates coordinates) {
		this.coordinates = coordinates;
		this.roomID = roomID;
		this.possibleDirections = possibleDirections;
		adjacentSquares = new ArrayList<>();
		this.cards = new ArrayList<>();
		this.squareColor = squareColor;
	}

	/**
	 * Returns a copy of the list of adjacent squares.
	 *
	 * @return a copy of the list of adjacent squares.
	 */
	List<Square> getAdjacentSquares() {
		return new ArrayList<>(adjacentSquares);
	}

	/**
	 * Returns the room ID.
	 *
	 * @return the room ID.
	 */
	public int getRoomID() {
		return roomID;
	}

	/**
	 * Returns the coordinates of the square in the map.
	 *
	 * @return the coordinates of the square in the map.
	 */
	public Coordinates getCoordinates() {
		return coordinates;
	}

	/**
	 * Returns the possible direction toward which the can move.
	 *
	 * @return the possible direction toward which the can move.
	 */
	public boolean[] getPossibleDirections() {
		return possibleDirections.clone();
	}

	/**
	 * Adds an adjacent square to the list of adjacent squares.
	 *
	 * @param adjacentSquare the adjacent square to add to the list.
	 */
	public void addAdjacentSquare(Square adjacentSquare) {
		adjacentSquares.add(adjacentSquare);
	}


	/**
	 * Returns the color of the square.
	 *
	 * @return the color of the square.
	 */
	public Color.CharacterColorType getSquareColor() {
		return squareColor;
	}

	/**
	 * Returns a copy of the list of cards in the square.
	 *
	 * @return a copy of the list of cards in the square.
	 */
	public List<Card> getCards() {
		return new ArrayList<>(cards);
	}

	public List<CardRep> getCardsRep() {
		List<CardRep> cardsRep = new ArrayList<>();
		for (Card card : cards) {
			cardsRep.add((CardRep) card.getRep());
		}
		return cardsRep;
	}

	/**
	 * Removes the card from the square and returns it.
	 *
	 * @param index index of the card to grab.
	 * @return the card in the index position.
	 */
	public abstract Card grabCard(int index);

	/**
	 * Adds the card to the square cards slot.
	 *
	 * @param cardToAdd Card to add to the square.
	 */
	public abstract void addCard(Card cardToAdd);

	/**
	 * If square card slot is not full it gets refilled.
	 */
	public abstract void refillCards();

	/**
	 * Returns true if and only if the square card slot is full.
	 *
	 * @return true if and only if the square card slot is full.
	 */
	public boolean isFilled() {
		return isFilled;
	}

	/**
	 * Sets the square as filled.
	 */
	public void setFilled() {
		isFilled = true;
	}

	/**
	 * Sets the square as not filled.
	 */
	public void setNotFilled() {
		isFilled = false;
	}

	/**
	 * Sets the square as changed.
	 */
	public void setChanged() {
		hasChanged = true;
	}

	/**
	 * Sets the square as not changed.
	 */
	public void setNotChanged() {
		hasChanged = false;
	}

	/**
	 * @return
	 * @deprecated
	 */
	public abstract boolean hasGrabbable();

	public abstract boolean canGrab(Player player);

	public abstract MessageType getGrabMessageType();
}