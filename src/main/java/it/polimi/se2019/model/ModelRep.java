package it.polimi.se2019.model;

import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import java.io.Serializable;
import java.util.ArrayList;

public class ModelRep implements Serializable {

	private GameBoardRep gameBoardRep;
	private GameMapRep gameMapRep;
	private ArrayList<PlayerRep> playersRep;
	private boolean changed = true;

	public ModelRep() {
	}

	public GameBoardRep getGameBoardRep() {
		return gameBoardRep;
	}

	public GameMapRep getGameMapRep() {
		return gameMapRep;
	}

	public ArrayList<PlayerRep> getPlayersRep() {
		return playersRep;
	}

	public void setGameBoardRep(GameBoardRep gameBoardRep) {
		this.gameBoardRep = gameBoardRep;
	}

	public void setGameMapRep(GameMapRep gameMapRep) {
		this.gameMapRep = gameMapRep;
	}

	public void setPlayersRep(ArrayList<PlayerRep> playersRep) {
		this.playersRep = playersRep;
	}

	private ArrayList<PlayerRep> generatePlayersRep(ArrayList<Player> players){
		ArrayList<PlayerRep> tempPlayersRep = new ArrayList<>();
		for (Player player: players ) {
			tempPlayersRep.add(new PlayerRep(player));
		}
		return tempPlayersRep;
	}

	private ArrayList<PlayerRep> generatePlayersRep(ArrayList<Player> players, ModelRep oldModelRep){
		ArrayList<PlayerRep> tempPlayersRep = new ArrayList<>();
		for (int i = 0; i < players.size(); i++){
			if (players.get(i).hasChanged())
				tempPlayersRep.add(new PlayerRep(players.get(i)));
			else
				tempPlayersRep.add(oldModelRep.getPlayersRep().get(i));
		}
		return tempPlayersRep;
	}
}