package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.Representation;

import java.util.HashMap;
import java.util.Map;

/**
 * A sharable version of the game map.
 *
 * @author MarcerAndrea
 */
public class GameMapRep implements Representation {

	private int numOfRows;
	private int numOfColumns;
	private SquareRep[][] mapRep;
	private HashMap<String, Coordinates> playersPositions;

	public GameMapRep(GameMap gameMapToRepresent) {
		this.numOfColumns = gameMapToRepresent.getNumOfColumns();
		this.numOfRows = gameMapToRepresent.getNumOfRows();

		mapRep = new SquareRep[gameMapToRepresent.getNumOfRows()][gameMapToRepresent.getNumOfColumns()];

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				this.mapRep[i][j] = gameMapToRepresent.getSquareRep(new Coordinates(i, j));
			}
		}

		playersPositions = new HashMap<>();
		gameMapToRepresent.getPlayersCoordinates().forEach((player, coordinates) -> playersPositions.put(player.getPlayerName(), coordinates));
	}

	public int getNumOfRows() {
		return numOfRows;
	}

	public int getNumOfColumns() {
		return numOfColumns;
	}

	public SquareRep[][] getMapRep() {
		return mapRep;
	}

	public Map<String, Coordinates> getPlayersCoordinates() {
		return playersPositions;
	}

	public Coordinates getPlayerCoordinates(String playerName) {
		return playersPositions.get(playerName);
	}



}