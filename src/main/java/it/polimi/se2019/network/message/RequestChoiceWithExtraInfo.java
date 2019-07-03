package it.polimi.se2019.network.message;

import java.util.List;

/**
 * Message used to request a choice in an array, but adding an extra info.
 * @author Desno365
 */
public class RequestChoiceWithExtraInfo extends RequestChoiceInArrayMessage {

	private String info;


	/**
	 * Constructs a message.
	 *
	 * @param info             the extra info to add.
	 * @param availableIndexes the available indexes of the array to choose from.
	 * @param messageType      the message type of this message.
	 */
	public RequestChoiceWithExtraInfo(String info, List<Integer> availableIndexes, MessageType messageType) {
		super(availableIndexes, messageType);
		this.info = info;
	}


	/**
	 * Returns the extra info in the message.
	 *
	 * @return the extra info in the message.
	 */
	public String getInfo() {
		return info;
	}
}
