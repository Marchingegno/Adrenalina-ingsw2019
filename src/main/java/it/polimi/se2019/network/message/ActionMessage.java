package it.polimi.se2019.network.message;

import it.polimi.se2019.view.server.VirtualView;

/**
 * Message used to communicate with the controller.
 */
public class ActionMessage extends Message{
	private VirtualView virtualView;

	public ActionMessage(MessageType messageType, MessageSubtype messageSubtype) {
		super(messageType, messageSubtype);
	}

	public void setVirtualView(VirtualView virtualView) {
		this.virtualView = virtualView;
	}

	public VirtualView getVirtualView(){
		return this.virtualView;
	}
}
