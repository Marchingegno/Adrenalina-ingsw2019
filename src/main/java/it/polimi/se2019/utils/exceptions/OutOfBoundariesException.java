package it.polimi.se2019.utils.exceptions;

/**
 * Thrown when some coordinates are out of the map.
 *
 * @author MarcerAndrea
 */
public class OutOfBoundariesException extends RuntimeException {

	/**
	 * Constructs an OutOfBoundariesException with the specified message.
	 *
	 * @param message the detail message.
	 */
	public OutOfBoundariesException(String message) {
		super(message);
	}
}
