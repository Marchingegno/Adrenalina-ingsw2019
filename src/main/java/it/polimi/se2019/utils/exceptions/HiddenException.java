package it.polimi.se2019.utils.exceptions;

/**
 * Thrown when trying to get the value of an attribute that is hidden.
 *
 * @author Desno365
 */
public class HiddenException extends RuntimeException {

    /**
     * Constructs an HiddenException with the specified detail message.
     *
     * @param message the detail message.
     */
    public HiddenException(String message) {
        super(message);
    }
}
