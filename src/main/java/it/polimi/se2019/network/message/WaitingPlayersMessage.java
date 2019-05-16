package it.polimi.se2019.network.message;

import java.util.ArrayList;
import java.util.List;

public class WaitingPlayersMessage extends Message {

	private ArrayList<String> waitingPlayersNames;

	public WaitingPlayersMessage(List<String> waitingPlayersNames) {
		super(MessageType.WAITING_PLAYERS, MessageSubtype.INFO);
		this.waitingPlayersNames = new ArrayList<>(waitingPlayersNames);
	}

	public List<String> getWaitingPlayersNames() {
		return waitingPlayersNames;
	}

}
