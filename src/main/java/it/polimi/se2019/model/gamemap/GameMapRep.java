package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A sharable vertion of the game map.
 * @author MarcerAndrea
 */
public class GameMapRep extends Message {

	private int numOfRows;
	private int	numOfColumns;
	private SquareRep[][] mapRep;
	private HashMap<String, Coordinates> playersPositions;

	public GameMapRep(GameMap gameMapToRepresent) {
		super(MessageType.GAME_MAP_REP, MessageSubtype.INFO);
		this.numOfColumns = gameMapToRepresent.getNumOfColumns();
		this.numOfRows = gameMapToRepresent.getNumOfRows();

		mapRep = new SquareRep[gameMapToRepresent.getNumOfRows()][gameMapToRepresent.getNumOfColumns()];

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				try{
					this.mapRep[i][j] = new SquareRep(gameMapToRepresent.getSquare(new Coordinates(i,j)));
				}catch (OutOfBoundariesException e){
					this.mapRep[i][j] = new SquareRep(new AmmoSquare(-1, new boolean[4], new Coordinates(i,j)));
				}
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

	public HashMap<String, Coordinates> getPlayersCoordinates() {
		return playersPositions;
	}
}