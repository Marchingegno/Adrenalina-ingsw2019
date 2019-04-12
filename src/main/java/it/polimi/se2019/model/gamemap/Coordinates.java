package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.Card;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 * Class with two integers representing the positions in the map matrix
 * @author MarcerAndrea
 */
public class Coordinates {

	private int row;
	private int column;

	public Coordinates(int row, int column){
		if ((row < 0) || (column < 0)) throw new NegativeCoordinatesException("some index is negative");
		this.row = row;
		this.column = column;
	}

	/**
	 * @return row index
	 */
	public int getRow() { return row; }

	/**
	 * @return column index
	 */
	public int getColumn() { return column;	}

	/**
	 * returns the distance from two coordinates
	 * @param otherCoordinates coordinates of the other point
	 * @return the distance between the two points
	 */
	public int distance(Coordinates otherCoordinates){
		return abs(getRow()-otherCoordinates.getRow()) + abs(getColumn()-otherCoordinates.getColumn());
	}

	public static Coordinates getDirectionCoordinates(Coordinates coordinates, CardinalDirection direction){
		switch (direction){
			case UP: return new Coordinates(coordinates.getRow() - 1, coordinates.getColumn());
			case RIGHT: return new Coordinates(coordinates.getRow(), coordinates.getColumn() + 1);
			case DOWN: return new Coordinates(coordinates.getRow() + 1, coordinates.getColumn());
			case LEFT: return new Coordinates(coordinates.getRow(), coordinates.getColumn() - 1);
			default: return null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof Coordinates) && (((Coordinates) obj).getRow() == this.row) && (((Coordinates) obj).getColumn() == this.column));
	}

	@Override
	public int hashCode(){	return (int)(pow(2, row - 1) * (2 * column - 1));}
}

/**
 * Thrown when coordinates is initialized with negative indexes.
 * @author MarcerAndrea
 */
class NegativeCoordinatesException extends RuntimeException {

	/**
	 * Constructs an NegativeCoordinatesException with the specified message.
	 * @param message the detail message.
	 */
	NegativeCoordinatesException(String message) {
		super(message);
	}
}