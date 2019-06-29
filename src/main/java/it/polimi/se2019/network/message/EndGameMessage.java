package it.polimi.se2019.network.message;

import it.polimi.se2019.utils.PlayerRepPosition;

import java.util.ArrayList;
import java.util.List;

public class EndGameMessage extends Message {

	private final ArrayList<PlayerRepPosition> finalPlayersInfo;


	public EndGameMessage(List<PlayerRepPosition> finalPlayersInfo, MessageSubtype messageSubtype) {
		super(MessageType.END_GAME, messageSubtype);
		this.finalPlayersInfo = new ArrayList<>(finalPlayersInfo);
	}


	public List<PlayerRepPosition> getFinalPlayersInfo() {
		return finalPlayersInfo;
	}
}
