package it.polimi.se2019.utils.exceptions;

/**
 * Thrown when a new item hasn't be added because the inventory has reached the max capacity.
 * @author Desno365
 */
public class InventoryFullException extends RuntimeException {

	/**
	 * Constructs an InventoryFullException with the specified detail message.
	 * @param message the detail message.
	 */
	public InventoryFullException(String message) {
		super(message);
	}
}
