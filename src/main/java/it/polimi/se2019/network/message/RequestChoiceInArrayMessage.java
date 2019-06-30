package it.polimi.se2019.network.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Message used to request a choice in an array.
 */
public class RequestChoiceInArrayMessage extends Message {

	private final ArrayList<Integer> availableIndexes;


	/**
	 * Constructs a message.
	 * @param availableIndexes the available indexes of the array to choose from.
	 * @param messageType the message type of this message.
	 */
	public RequestChoiceInArrayMessage(List<Integer> availableIndexes, MessageType messageType) {
		super(messageType, MessageSubtype.REQUEST);
		this.availableIndexes = new ArrayList<>(availableIndexes);
	}


	/**
	 * Returns the available indexes of the array to choose from.
	 * @return the available indexes of the array to choose from.
	 */
	public List<Integer> getAvailableIndexes() {
		return availableIndexes;
	}
}
