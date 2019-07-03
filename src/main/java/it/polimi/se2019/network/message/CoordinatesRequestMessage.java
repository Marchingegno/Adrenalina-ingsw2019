package it.polimi.se2019.network.message;

import it.polimi.se2019.model.gamemap.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * Message used to request a Coordinates to the Player, the Coordinates must be from a list of allowedCoordinates.
 * This message is used for the MOVE action.
 * @author MarcerAndrea
 */
public class CoordinatesRequestMessage extends Message {

	private final ArrayList<Coordinates> coordinates;


	/**
	 * Constructs a message.
	 *
	 * @param coordinates a list of allowed Coordinates to choose from.
	 * @param messageType the messageType of the message.
	 */
	public CoordinatesRequestMessage(List<Coordinates> coordinates, MessageType messageType) {
		super(messageType, MessageSubtype.REQUEST);
		this.coordinates = new ArrayList<>(coordinates);
	}


	/**
	 * Returns a list of allowed Coordinates to choose from.
	 *
	 * @return a list of allowed Coordinates to choose from.
	 */
	public List<Coordinates> getCoordinates() {
		return coordinates;
	}
}
