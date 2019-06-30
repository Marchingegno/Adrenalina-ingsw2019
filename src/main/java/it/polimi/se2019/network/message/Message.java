package it.polimi.se2019.network.message;

import java.io.Serializable;

/**
 * General Message class.
 * All other messages used in the game extend this class.
 */
public class Message implements Serializable {

	private final MessageType messageType;
	private final MessageSubtype messageSubtype;


	/**
	 * Constructs a general message.
	 * @param messageType the messageType of the message.
	 * @param messageSubtype the messageSubtype of the message.
	 */
	public Message(MessageType messageType, MessageSubtype messageSubtype) {
		this.messageType = messageType;
		this.messageSubtype = messageSubtype;
	}


	/**
	 * Returns the message type of this message.
	 * @return the message type of this message.
	 */
	public MessageType getMessageType() {
		return messageType;
	}

	/**
	 * Returns the message subtype of this message.
	 * @return the message subtype of this message.
	 */
	public MessageSubtype getMessageSubtype() {
		return messageSubtype;
	}

	@Override
	public String toString() {
		return "Type: " + messageType + " subtype: " + messageSubtype;
	}
}
