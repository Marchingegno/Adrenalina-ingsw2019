package it.polimi.se2019.model;

import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelRep implements Serializable, Changeable {

	private GameBoardRep gameBoardRep;
	private GameMapRep gameMapRep;
	private ArrayList<PlayerRep> playersRep;


	public ModelRep(GameBoard gameBoard) {
		this.gameBoardRep = new GameBoardRep(gameBoard);
		this.gameMapRep = new GameMapRep(gameBoard.getGameMap());
		this.playersRep = generatePlayersRep(gameBoard.getPlayers());
	}

	public ModelRep(GameBoard gameBoard, ModelRep oldModelRep) {
		this.gameBoardRep = (gameBoard.isChanged()? new GameBoardRep(gameBoard) : oldModelRep.getGameBoardRep());
		this.gameMapRep = gameBoard.getGameMap().isChanged()? new GameMapRep(gameBoard.getGameMap()) : oldModelRep.getMapRep();
		this.playersRep = generatePlayersRep(gameBoard.getPlayers(), oldModelRep);
	}


	public GameBoardRep getGameBoardRep() {
		return gameBoardRep;
	}

	public GameMapRep getMapRep() {
		return gameMapRep;
	}

	public List<PlayerRep> getPlayersRep() {
		return new ArrayList<>(playersRep);
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
			if (players.get(i).isChanged())
				tempPlayersRep.add(new PlayerRep(players.get(i)));
			else
				getPlayersRep().get(i);
		}
		return tempPlayersRep;
	}

	@Override
	public boolean isChanged() {
		return false;
	}

	@Override
	public void change() {

	}

	@Override
	public void reset() {

	}
}