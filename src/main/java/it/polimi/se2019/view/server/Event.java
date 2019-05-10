package it.polimi.se2019.view.server;

import it.polimi.se2019.network.message.Message;

/**
 * Message used to communicate with the controller.
 */
public class Event {
	private VirtualView virtualView;
	private Message message;

	public Event(VirtualView virtualView, Message message) {
		this.virtualView = virtualView;
		this.message = message;
	}

	public VirtualView getVirtualView(){
		return this.virtualView;
	}

	public Message getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Event of message type " + message.getMessageType() + "and subtype" + message.getMessageSubtype();
	}
}
