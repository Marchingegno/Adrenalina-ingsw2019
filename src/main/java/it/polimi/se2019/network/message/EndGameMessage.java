package it.polimi.se2019.network.message;

import it.polimi.se2019.utils.PlayersPosition;

import java.util.ArrayList;
import java.util.List;

public class EndGameMessage extends Message {

	private final ArrayList<PlayersPosition> finalPlayersInfo;


	public EndGameMessage(List<PlayersPosition> finalPlayersInfo, MessageSubtype messageSubtype) {
		super(MessageType.END_GAME, messageSubtype);
		this.finalPlayersInfo = new ArrayList<>(finalPlayersInfo);
	}


	public List<PlayersPosition> getFinalPlayersInfo() {
		return finalPlayersInfo;
	}
}
