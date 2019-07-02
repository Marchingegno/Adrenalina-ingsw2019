package it.polimi.se2019.utils.exceptions;

/**
 * Thrown when coordinates is initialized with negative indexes.
 *
 * @author MarcerAndrea
 */
public class NegativeCoordinatesException extends RuntimeException {

	/**
	 * Constructs an NegativeCoordinatesException with the specified message.
	 *
	 * @param message the detail message.
	 */
	public NegativeCoordinatesException(String message) {
		super(message);
	}
}
