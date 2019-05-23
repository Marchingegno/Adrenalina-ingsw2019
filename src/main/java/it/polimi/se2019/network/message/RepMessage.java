package it.polimi.se2019.network.message;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;

import java.util.ArrayList;
import java.util.List;

public class RepMessage extends Message {

	private Message message = null;
	private GameMapRep gameMapRep = null;
	private GameBoardRep gameBoardRep = null;
	private List<PlayerRep> playersRep = new ArrayList<>();
	private boolean hasReps = false;


	public RepMessage() {
		super(MessageType.UPDATE_REPS, MessageSubtype.INFO);
	}


	public void addMessage(Message message) {
		this.message = message;
	}

	public void addGameMapRep(GameMapRep gameMapRep) {
		this.gameMapRep = gameMapRep;
		hasReps = true;
	}

	public void addGameBoardRep(GameBoardRep gameBoardRep) {
		this.gameBoardRep = gameBoardRep;
		hasReps = true;
	}

	public void addPlayersRep(PlayerRep playerRepToAdd) {
		hasReps = true;
		for (int i = 0; i < playersRep.size(); i++) {
			if (playersRep.get(i).getPlayerName().equals(playerRepToAdd.getPlayerName())) {
				playersRep.set(i, playerRepToAdd);
				return;
			}
		}
		playersRep.add(playerRepToAdd);
	}

	public Message getMessage() {
		return message;
	}

	public GameMapRep getGameMapRep() {
		return gameMapRep;
	}

	public GameBoardRep getGameBoardRep() {
		return gameBoardRep;
	}

	public List<PlayerRep> getPlayersRep() {
		return playersRep;
	}

	public boolean hasReps() {
		return hasReps;
	}
}
