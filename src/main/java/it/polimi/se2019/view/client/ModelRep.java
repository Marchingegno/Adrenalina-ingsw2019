package it.polimi.se2019.view.client;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;

import java.util.ArrayList;

public class ModelRep{

	private GameBoardRep gameBoardRep;
	private GameMapRep gameMapRep;
	private ArrayList<PlayerRep> playersRep;

	public ModelRep() {
		playersRep = new ArrayList<>();
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

	public void setPlayersRep(PlayerRep playerRepToSet) {
		int numOfPlayersRep = playersRep.size();
		for (int i = 0; i < numOfPlayersRep; i++) {
			if (playersRep.get(i).getPlayerName().equals(playerRepToSet.getPlayerName())) {
				playersRep.set(i, playerRepToSet);
				return;
			}
		}

		//There is no PlayerRep for this player so I add it
		playersRep.add(playerRepToSet);
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