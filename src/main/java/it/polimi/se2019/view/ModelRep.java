package it.polimi.se2019.view;

import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import java.util.ArrayList;

public class ModelRep{

	private GameBoardRep gameBoardRep;
	private GameMapRep gameMapRep;
	private ArrayList<PlayerRep> playersRep;

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

	public void setPlayersRep(PlayerRep playerRep) {
		if(!playersRep.contains(playerRep))
			playersRep.add(playerRep);
		else
			for (int i = 0; i < playersRep.size(); i++) {
				if(playersRep.get(i).getPlayerName().equals(playerRep.getPlayerName()))
					playersRep.add(i, playerRep);
			}
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