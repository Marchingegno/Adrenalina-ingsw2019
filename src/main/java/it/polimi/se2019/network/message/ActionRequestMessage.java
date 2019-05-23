package it.polimi.se2019.network.message;

public class ActionRequestMessage extends Message {

	private final boolean activablePowerups;
	private final boolean activableWeapons;


	public ActionRequestMessage(boolean activablePowerups, boolean activableWeapons) {
		super(MessageType.ACTION, MessageSubtype.REQUEST);
		this.activablePowerups = activablePowerups;
		this.activableWeapons = activableWeapons;
	}


	public boolean isActivablePowerups() {
		return activablePowerups;
	}

	public boolean isActivableWeapons() {
		return activableWeapons;
	}
}
