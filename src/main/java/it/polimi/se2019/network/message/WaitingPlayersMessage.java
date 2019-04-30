package it.polimi.se2019.network.message;

public class WaitingPlayersMessage extends StringMessage {

	public WaitingPlayersMessage(String waitingPlayers) {
		super(waitingPlayers, MessageType.WAITING_PLAYERS, MessageSubtype.INFO);
	}

}
