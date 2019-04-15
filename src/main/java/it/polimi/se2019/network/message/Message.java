package it.polimi.se2019.network.message;

import java.io.Serializable;

public class Message implements Serializable {

	private MessageType messageType;


	public Message(MessageType messageType) {
		this.messageType = messageType;
	}


	public MessageType getMessageType() {
		return messageType;
	}
}
