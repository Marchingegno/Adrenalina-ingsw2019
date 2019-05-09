package it.polimi.se2019.network.message;

/**
 * Used for action requesting, grab, spawn, shoot and reload.
 * @author Marchingegno
 */
	private int index;
public class DefaultActionMessage extends IntMessage {

	public DefaultActionMessage(int index, MessageType messageType, MessageSubtype messageSubtype) {
		super(messageType, messageSubtype);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
