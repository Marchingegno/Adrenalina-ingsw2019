package it.polimi.se2019.model;

import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelView implements Serializable {

	private GameBoardRep gameBoardRep;
	private GameMapRep gameMapRep;
	private ArrayList<PlayerRep> playerReps;


	public ModelView(GameBoardRep gameBoardRep, GameMap gameMap, List<PlayerRep> playerReps) {
		this.gameBoardRep = gameBoardRep;
		this.gameMapRep = new GameMapRep(gameMap);
		this.playerReps = new ArrayList<>(playerReps);
	}


	public GameBoardRep getGameBoardRep() {
		return gameBoardRep;
	}

	public GameMapRep getMapRep() {
		return gameMapRep;
	}

	public List<PlayerRep> getPlayerReps() {
		return new ArrayList<>(playerReps);
	}

}