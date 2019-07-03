package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.exceptions.NegativeCoordinatesException;

import java.io.Serializable;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 * Class with two integers representing the positions in the map matrix
 *
 * @author MarcerAndrea
 */
public class Coordinates implements Serializable {

	private int row;
	private int column;

	/**
	 * @param row    row index
	 * @param column column index
	 * @throws NegativeCoordinatesException when there are negative indexes
	 */
	public Coordinates(int row, int column) {
		if ((row < 0) || (column < 0))
			throw new NegativeCoordinatesException("tried to create a coordinate with negative indexes (" + row + "," + column + ")");
		this.row = row;
		this.column = column;
	}

	/**
	 * Given a coordinate and a direction returns the coordinates of the point directly adjacent in that direction.
	 *
	 * @param coordinates coordinates of the starting point
	 * @param direction   direction of movement
	 * @return the coordinates of the point directly adjacent to the given point in the specified direction
	 */
	static Coordinates getDirectionCoordinates(Coordinates coordinates, CardinalDirection direction) {
		try {
			switch (direction) {
				case UP:
					return new Coordinates(coordinates.getRow() - 1, coordinates.getColumn());
				case RIGHT:
					return new Coordinates(coordinates.getRow(), coordinates.getColumn() + 1);
				case DOWN:
					return new Coordinates(coordinates.getRow() + 1, coordinates.getColumn());
				default:
					return new Coordinates(coordinates.getRow(), coordinates.getColumn() - 1);
			}
		} catch (NegativeCoordinatesException e) {
			return null;
		}
	}

	/**
	 * Returns row index.
	 *
	 * @return row index
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns column index.
	 *
	 * @return column index
	 */
	public int getColumn() {
		return column;
	}

	/**
     * Returns the Manhattan distance between two coordinates
	 *
	 * @param otherCoordinates coordinates of the other point
	 * @return the Manhattan distance between the two points
	 */
	public int distance(Coordinates otherCoordinates) {
		return abs(getRow() - otherCoordinates.getRow()) + abs(getColumn() - otherCoordinates.getColumn());
	}

	/**
	 * Returns the String representation of the Coordinates. The format is ('row','column').
	 *
	 * @return the String representation of the Coordinates
	 */
	@Override
	public String toString() {
		return ("(" + getRow() + "," + getColumn() + ")");
	}

	/**
	 * Returns true if and only if the obj is a Coordinate and has the same row and column index as the Coordinate tha has called the method.
	 *
	 * @param obj object to be compered to
	 * @return true if and only if the obj is a Coordinate and has the same row and column index as the Coordinate tha has called the method
	 */
	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof Coordinates) && (((Coordinates) obj).getRow() == this.row) && (((Coordinates) obj).getColumn() == this.column));
	}

	/**
	 * Returns the hash code of the object. To generate it is used the bijective function from f: NXN : N where f(n, m) = 2^(n - 1) * (2 * m - 1).
	 *
	 * @return the hash code of the object
	 */
	@Override
	public int hashCode() {
		return (int) (pow((double) 2, (double) (row - 1)) * (2 * column - 1));
	}
}