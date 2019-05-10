package it.polimi.se2019.model;

import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;

public abstract class Representation extends Message {

	public Representation(MessageType messageType, MessageSubtype messageSubtype) {
		super(messageType, messageSubtype);
	}
}
