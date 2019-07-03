package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.util.HashMap;
import java.util.Map;

/**
 * A sharable version of the game map.
 *
 * @author MarcerAndrea
 */
public class GameMapRep implements Representation {

	private String mapName;
	private int numOfRows;
	private int numOfColumns;
	private SquareRep[][] mapRep;
	private Map<String, Coordinates> playersPositions;
	private Map<AmmoType, Coordinates> spawnSquares;

	public GameMapRep(GameMap gameMapToRepresent) {
		this.numOfColumns = gameMapToRepresent.getNumOfColumns();
		this.numOfRows = gameMapToRepresent.getNumOfRows();

		mapRep = new SquareRep[gameMapToRepresent.getNumOfRows()][gameMapToRepresent.getNumOfColumns()];

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				this.mapRep[i][j] = gameMapToRepresent.getSquareRep(new Coordinates(i, j));
			}
		}

		spawnSquares = new HashMap<>();
		for (AmmoType ammoType : AmmoType.values()) {
			spawnSquares.put(ammoType, gameMapToRepresent.getSpawnCoordinates(ammoType));
		}

		playersPositions = new HashMap<>();
		gameMapToRepresent.getPlayersCoordinates().forEach((player, coordinates) -> playersPositions.put(player.getPlayerName(), coordinates));

		mapName = gameMapToRepresent.getName();
	}

    /**
     * Returns the name of the map.
     *
     * @return the name of the map.
     */
    public String getName() {
        return mapName;
    }

    /**
     * Returns the number of rows in the map.
     * @return the number of rows in the map.
	 */
	public int getNumOfRows() {
		return numOfRows;
    }

    /**
     * Returns the number of columns in the map.
     * @return the number of columns in the map.
	 */
    public int getNumOfColumns() {
        return numOfColumns;
    }

    /**
     * Returns the matrix of square reps.
     * @return the matrix of square reps.
     */
    public SquareRep[][] getMapRep() {
        return mapRep;
    }

    /**
     * Returns the players' position.
     * @return the players' position.
	 */
    public Map<String, Coordinates> getPlayersCoordinates() {
        return playersPositions;
    }

    /**
     * Returns the position of the specified player.
     * @param playerName name of the player.
     * @return the position of the specified player.
     */
    public Coordinates getPlayerCoordinates(String playerName) {
        return playersPositions.get(playerName);
    }

    /**
     * Returns the coordinates of spawn that has the specified ammo as associated ammo.
     *
     * @param ammoType the ammo.
     * @return the coordinates of spawn that has the specified ammo as associated ammo.
     */
    public Coordinates getSpawnCoordinates(AmmoType ammoType) {
        return spawnSquares.get(ammoType);
    }

    /**
     * Returns the square rep in the position of the specified player.
     * @param playerName the player name.
     * @return the square rep in the position of the specified player.
     */
    public SquareRep getPlayerSquare(String playerName) {
        Coordinates playerCoordinates = playersPositions.get(playerName);
        return mapRep[playerCoordinates.getRow()][playerCoordinates.getColumn()];
    }

    /**
     * Returns true if and only if in the specified coordinates there is a spawn square.
     * @param coordinates the coordinates to check.
     * @return true if and only if in the specified coordinates there is a spawn square.
     */
    public boolean isSpawn(Coordinates coordinates) {
        return spawnSquares.containsValue(coordinates);
    }

}