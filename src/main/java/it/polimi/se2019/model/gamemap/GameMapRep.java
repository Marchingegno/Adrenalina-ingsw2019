package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;

import java.util.HashMap;

/**
 * A sharable version of the game map.
 *
 * @author MarcerAndrea
 */
public class GameMapRep extends Representation {

	private int numOfRows;
	private int numOfColumns;
	private MapSquareRep[][] mapRep;
	private HashMap<String, Coordinates> playersPositions;

	public GameMapRep(GameMap gameMapToRepresent) {
		super(MessageType.GAME_MAP_REP, MessageSubtype.INFO);
		this.numOfColumns = gameMapToRepresent.getNumOfColumns();
		this.numOfRows = gameMapToRepresent.getNumOfRows();

		mapRep = new MapSquareRep[gameMapToRepresent.getNumOfRows()][gameMapToRepresent.getNumOfColumns()];

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

	public MapSquareRep[][] getMapRep() {
		return mapRep;
	}

	public HashMap<String, Coordinates> getPlayersCoordinates() {
		return playersPositions;
	}

	public boolean equals(Object object) {
		boolean temp = (object instanceof GameMapRep &&
				this.numOfColumns == ((GameMapRep) object).numOfColumns &&
				this.numOfRows == ((GameMapRep) object).numOfRows &&
				this.playersPositions.equals(object));
		if (!temp)
			return false;
		temp = true;
		MapSquareRep[][] mapRepToCompare = ((GameMapRep) object).mapRep;
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				if (!mapRep[i][j].equals(mapRepToCompare[i][j]))
					return false;
			}
		}
		return true;
	}
}