package it.polimi.se2019.network.message;

public class IntMessage extends Message {

	private int content;


	public IntMessage(int content, MessageType messageType, MessageSubtype messageSubtype) {
		super(messageType, messageSubtype);
		this.content = content;
	}


	public int getContent() {
		return content;
	}
}
