package it.polimi.se2019.model;

import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.player.Player;

import java.util.List;

public class ModelDriver extends Model {

	public ModelDriver(String mapName, List<String> playerNames, int startingSkulls) {
		super(mapName, playerNames, startingSkulls);
	}

	@Override
	public GameBoard getGameBoard() {
		return super.getGameBoard();
	}

	@Override
	public Player getPlayerFromName(String playerName) {
		return super.getPlayerFromName(playerName);
	}
}
