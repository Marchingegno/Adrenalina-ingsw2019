package it.polimi.se2019.network.message;

public class StringMessage extends Message {

	private String content;


	public StringMessage(String content, MessageType messageType, MessageSubtype messageSubtype) {
		super(messageType, messageSubtype);
		this.content = content;
	}


	public String getContent() {
		return content;
	}
}
