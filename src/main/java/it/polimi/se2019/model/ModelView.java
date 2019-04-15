package it.polimi.se2019.model;

import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.gamemap.MapRep;
import it.polimi.se2019.model.player.PlayerRep;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelView implements Serializable {

	private GameBoardRep gameBoardRep;
	private MapRep mapRep;
	private ArrayList<PlayerRep> playerReps;


	public ModelView(GameBoardRep gameBoardRep, GameMap gameMap, List<PlayerRep> playerReps) {
		this.gameBoardRep = gameBoardRep;
		this.mapRep = new MapRep(gameMap);
		this.playerReps = new ArrayList<>(playerReps);
	}


	public GameBoardRep getGameBoardRep() {
		return gameBoardRep;
	}

	public MapRep getMapRep() {
		return mapRep;
	}

	public List<PlayerRep> getPlayerReps() {
		return new ArrayList<>(playerReps);
	}

}