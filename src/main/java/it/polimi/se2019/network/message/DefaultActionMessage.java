package it.polimi.se2019.network.message;

/**
 * Used for action requesting, grab, spawn, shoot and reload.
 * @author Marchingegno
 */
public class DefaultActionMessage extends IntMessage {

	public DefaultActionMessage(int index, MessageType messageType, MessageSubtype messageSubtype) {
		super(index, messageType, messageSubtype);
	}
}
