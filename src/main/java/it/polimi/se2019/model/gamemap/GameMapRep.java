package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author MarcerAndrea
 */
public class GameMapRep implements Serializable {

	private int numOfRows;
	private int	numOfColumns;
	private SquareRep[][] mapRep;
	private HashMap<Player, Coordinates> playersPositions;

	public GameMapRep(GameMap gameMapToRepresent) {
		this.numOfColumns = gameMapToRepresent.getNumOfColumns();
		this.numOfRows = gameMapToRepresent.getNumOfRows();

		mapRep = new SquareRep[this.numOfRows][this.numOfColumns];

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				try{
					this.mapRep[i][j] = new SquareRep(gameMapToRepresent.getSquare(new Coordinates(i,j)));
				}catch (OutOfBoundariesException e){
					mapRep[i][j] = new SquareRep(new AmmoSquare(-1, new boolean[4], new Coordinates(i,j)));
				}
			}
		}

		playersPositions = gameMapToRepresent.getPlayersCoordinates();
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

	public HashMap<Player, Coordinates> getPlayersCoordinates() {
		return playersPositions;
	}
}