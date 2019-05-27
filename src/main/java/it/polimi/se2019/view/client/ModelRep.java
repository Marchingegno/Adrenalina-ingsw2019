package it.polimi.se2019.view.client;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ModelRep{

	private GameBoardRep gameBoardRep;
	private GameMapRep gameMapRep;
	private ArrayList<PlayerRep> playersRep = new ArrayList<>();

	public GameBoardRep getGameBoardRep() {
		return gameBoardRep;
	}

	public GameMapRep getGameMapRep() {
		return gameMapRep;
	}

	public List<PlayerRep> getPlayersRep() {
		return playersRep;
	}

	public PlayerRep getClientPlayerRep() {
		for (PlayerRep playerRep : playersRep) {
			if(!playerRep.isHidden())
				return playerRep;
		}
		throw new IllegalStateException("Player rep of Client not found.");
	}

	public void setGameBoardRep(GameBoardRep gameBoardRep) {
		this.gameBoardRep = gameBoardRep;
		Utils.logRep("ModelRep -> setGameBoardRep(): Updated the GameBoardRep");
	}

	public void setGameMapRep(GameMapRep gameMapRep) {
		this.gameMapRep = gameMapRep;
		Utils.logRep("ModelRep -> setGameMapRep(): Updated the GameMapRep");
	}

	public void setPlayerRep(PlayerRep playerRepToSet) {
		int numOfPlayersRep = playersRep.size();
		for (int i = 0; i < numOfPlayersRep; i++) {
			if (playersRep.get(i).getPlayerName().equals(playerRepToSet.getPlayerName())) {
				playersRep.set(i, playerRepToSet);
				Utils.logRep("ModelRep -> setPlayersRep(): Updated the PlayersRep of " + playerRepToSet.getPlayerName());
				return;
			}
		}
		//There is no PlayerRep for this player so I add it
		Utils.logRep("ModelRep -> setPlayersRep(): Received a new PlayersRep of " + playerRepToSet.getPlayerName());
		playersRep.add(playerRepToSet);
	}
}