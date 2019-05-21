package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.CardRep;
import it.polimi.se2019.utils.Color;

import java.util.List;

/**
 * A sharable version of the square.
 *
 * @author MarcerAmdrea
 */
public abstract class SquareRep implements Representation {

	private List<CardRep> cardsRep;
	private int roomID;
	private Color.CharacterColorType squareColor;
	private Coordinates coordinates;
	private boolean[] possibleDirection;

	public SquareRep(Square squareToRepresent) {
		this.cardsRep = squareToRepresent.getCardsRep();
		this.roomID = squareToRepresent.getRoomID();
		this.coordinates = squareToRepresent.getCoordinates();
		this.squareColor = squareToRepresent.getSquareColor();
		this.possibleDirection = squareToRepresent.getPossibleDirections();
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
	 * Returns coordinates of the square.
	 * @return coordinates of the square.
	 */
	public Coordinates getCoordinates() {
		return coordinates;
	}

	/**
	 * Returns the possible direction in which tha palyer can move.
	 * @return the possible direction in which tha palyer can move.
	 */
	public boolean[] getPossibleDirection() {
		return possibleDirection;
	}

	/**
	 * Returns the color of the square.
	 *
	 * @return the color of the square.
	 */
	public Color.CharacterColorType getSquareColor() {
		return squareColor;
	}

	public List<CardRep> getCards() {
		return cardsRep;
	}

	/**
	 * Returns the elements to print in the CLI.
	 *
	 * @return the elements to print in the CLI.
	 */
	public abstract String[] getElementsToPrint();
}
