package it.polimi.se2019.network.message;

public class NicknameMessage extends StringMessage {

	public NicknameMessage(String content, MessageSubtype messageSubtype) {
		super(content, MessageType.NICKNAME, messageSubtype);
	}
}
