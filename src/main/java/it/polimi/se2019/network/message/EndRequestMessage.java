package it.polimi.se2019.network.message;

/**
 * Message used to request to the player his final action(s) when ending the turn.
 * @author Desno365
 */
public class EndRequestMessage extends Message {

	private final boolean activablePowerups;


	/**
	 * Constructs a message.
	 *
	 * @param activablePowerups true if the player receiving the message can activate a powerup.
	 */
	public EndRequestMessage(boolean activablePowerups) {
		super(MessageType.END_TURN, MessageSubtype.REQUEST);
		this.activablePowerups = activablePowerups;
	}


	/**
	 * True if the player receiving the message can activate a powerup.
	 *
	 * @return true if the player receiving the message can activate a powerup.
	 */
	public boolean isActivablePowerups() {
		return activablePowerups;
	}
}
