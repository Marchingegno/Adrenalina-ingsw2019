package it.polimi.se2019.network.message;

public class EndRequestMessage extends Message {

	private final boolean activablePowerups;


	public EndRequestMessage(boolean activablePowerups) {
		super(MessageType.END_TURN, MessageSubtype.REQUEST);
		this.activablePowerups = activablePowerups;
	}


	public boolean isActivablePowerups() {
		return activablePowerups;
	}
}
