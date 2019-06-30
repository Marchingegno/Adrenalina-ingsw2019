package it.polimi.se2019.network.message;

/**
 * Message used to request what action the players wants to do.
 */
public class ActionRequestMessage extends Message {

	private final boolean activablePowerups;
	private final boolean activableWeapons;


	/**
	 * Constructs a message.
	 * @param activablePowerups true if the player receiving the message can activate a powerup.
	 * @param activableWeapons true if the player receiving the message can activate a weapon.
	 */
	public ActionRequestMessage(boolean activablePowerups, boolean activableWeapons) {
		super(MessageType.ACTION, MessageSubtype.REQUEST);
		this.activablePowerups = activablePowerups;
		this.activableWeapons = activableWeapons;
	}


	/**
	 * True if the player receiving the message can activate a powerup.
	 * @return true if the player receiving the message can activate a powerup.
	 */
	public boolean isActivablePowerups() {
		return activablePowerups;
	}

	/**
	 * True if the player receiving the message can activate a weapon.
	 * @return true if the player receiving the message can activate a weapon.
	 */
	public boolean isActivableWeapons() {
		return activableWeapons;
	}
}
