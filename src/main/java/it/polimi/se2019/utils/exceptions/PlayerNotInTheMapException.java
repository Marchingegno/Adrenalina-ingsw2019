package it.polimi.se2019.utils.exceptions;

/**
 * Thrown when a player is not in the Map.
 *
 * @author MarcerAndrea
 */
public class PlayerNotInTheMapException extends RuntimeException {

	/**
	 * Constructs an PlayerNotInTheMapException with the specified message.
	 *
	 * @param message the detail message.
	 */
	public PlayerNotInTheMapException(String message) {
		super(message);
	}
}
