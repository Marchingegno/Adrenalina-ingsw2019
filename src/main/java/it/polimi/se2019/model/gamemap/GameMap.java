package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.GameBoard;
import it.polimi.se2019.model.Representable;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the game map
 *
 * @author MarcerAndrea
 */
public class GameMap extends Representable {

	private int numOfRows;
	private int numOfColumns;
	private MapSquare[][] map;
	private ArrayList<ArrayList<Coordinates>> rooms = new ArrayList<>();
	private HashMap<Player, Coordinates> playersPositions = new HashMap<>();
	private ArrayList<Coordinates> spawnSquaresCoordinates = new ArrayList<>();
	private GameMapRep gameMapRep;

	public GameMap(String mapName, List<Player> players, GameBoard gameBoard) {

		generateMap(mapName, gameBoard);
		connectSquares();
		addSquaresToRooms();
		fillMap();

		for (Player playerToAdd : players) {
			playersPositions.put(playerToAdd, null);
		}

		setChanged();
	}

	/**
	 * For each square in the map if it has an empty slot
	 * it gets refilled.
	 */
	public void fillMap() {
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				map[i][j].refillCards();
			}
		}
	}

	/**
	 * Returns the number of rows.
	 *
	 * @return the number of rows
	 */
	public int getNumOfRows() {
		return numOfRows;
	}

	/**
	 * Returns the number of columns.
	 *
	 * @return the number of columns
	 */
	public int getNumOfColumns() {
		return numOfColumns;
	}

	/**
	 * Returns the player position.
	 *
	 * @return the player position
	 */
	public Map<Player, Coordinates> getPlayersCoordinates() {
		return new HashMap<>(playersPositions);
	}

	/**
	 * Returns the player's coordinates.
	 *
	 * @param playerToFind player to find
	 * @return the player's coordinates
	 * @throws PlayerNotInTheMapException when the player is not in the map
	 */
	public Coordinates playerCoordinates(Player playerToFind) {
		Coordinates playerCoordinates = playersPositions.get(playerToFind);
		if (playerCoordinates != null)
			return playerCoordinates;
		else
			throw new PlayerNotInTheMapException("player position not in the map");
	}

	/**
	 * Returns the player's square.
	 *
	 * @param playerToFind player to find
	 * @return the player's square
	 * @throws PlayerNotInTheMapException when the player is not in the map
	 */
	public MapSquare playerSquare(Player playerToFind) {
		Coordinates playerCoordinates = playersPositions.get(playerToFind);
		if (playerCoordinates != null)
			return map[playerCoordinates.getRow()][playerCoordinates.getColumn()];
		else
			throw new PlayerNotInTheMapException("player position not in the map");
	}

	/**
	 * Moves the player in the specified coordinates.
	 *
	 * @param playerToMove player to move
	 * @param coordinates  coordinates where the player has to be moved to
	 * @throws OutOfBoundariesException when the player is moved to a square not in the map
	 */
	public void movePlayerTo(Player playerToMove, Coordinates coordinates) {
		if (isIn(coordinates))
			playersPositions.replace(playerToMove, coordinates);
		else
			throw new OutOfBoundariesException("tried to move the player out of the map" + coordinates.toString());
		setChanged();
	}

	/**
	 * Returns the list of all reachable coordinates by a player within the specified distance.
	 * @param player the player that wants to move.
	 * @param maxDistance distance the player wants to move.
	 * @return the list of reachable coordinates.
	 */
	public List<Coordinates> reachableCoordinates(Player player, int maxDistance) {
		ArrayList<Coordinates> reachableCoordinates = new ArrayList<>();
		return reachableSquares(getSquare(playersPositions.get(player)), maxDistance, reachableCoordinates);
	}


	/**
	 * Returns the list of all reachable coordinates from the specified coordinates within the specified distance.
	 * @param coordinates starting coordinates.
	 * @param maxDistance distance the player wants to move.
	 * @return the list of reachable coordinates.
	 */
	public List<Coordinates> reachableCoordinates(Coordinates coordinates, int maxDistance) {
		ArrayList<Coordinates> reachableCoordinates = new ArrayList<>();
		return reachableSquares(getSquare(coordinates), maxDistance, reachableCoordinates);
	}

	/**
	 * Returns the set of all reachable squares from the coordinates and distance at most max distance.
	 *
	 * @param mapSquare   mapSquare of the starting point
	 * @param maxDistance maximum distance
	 * @return the set of all reachable squares from the coordinates and distance at most max distance
	 */
	private ArrayList<Coordinates> reachableSquares(MapSquare mapSquare, int maxDistance, ArrayList<Coordinates> reachableCoordinates) {

		if (maxDistance != 0) {
			for (MapSquare adjacentMapSquare : mapSquare.getAdjacentMapSquares()) {
				reachableSquares(adjacentMapSquare, maxDistance - 1, reachableCoordinates);
			}
		}

		if (!(reachableCoordinates.contains(getCoordinates(mapSquare))))
			reachableCoordinates.add(getCoordinates(mapSquare));
		return reachableCoordinates;
	}

	/**
	 * Returns the set of all the squares belonging to the same room of the specified mapSquare.
	 *
	 * @param mapSquare a mapSquare
	 * @return the set of all the squares belonging to the same room of the specified mapSquare
	 * @throws OutOfBoundariesException when the mapSquare does not belong to the map
	 */
	public List<Coordinates> getRoomCoordinates(MapSquare mapSquare) {
		if (isIn(mapSquare))
			return new ArrayList<>(rooms.get(mapSquare.getRoomID()));
		else
			throw new OutOfBoundariesException("the mapSquare does not belong to the map " + getCoordinates(mapSquare));
	}

	/**
	 * Returns the set of all the squares belonging to the same room of the specified square.
	 *
	 * @param coordinates a coordinates
	 * @return the set of all the squares belonging to the same room of the specified square
	 * @throws OutOfBoundariesException the coordinates do not belong to the map
	 */
	public List<Coordinates> getRoomCoordinates(Coordinates coordinates) {
		if (isIn(coordinates))
			return new ArrayList<>(rooms.get(getSquare(coordinates).getRoomID()));
		else
			throw new OutOfBoundariesException("the coordinates do not belong to the map " + coordinates);
	}

	/**
	 * Returns true if and only if the player2 is visible from the player1. This is done looking at the rooms ID of the squares where the player are.
	 *
	 * @param player1 player how is observing
	 * @param player2 player target
	 * @return true if and only if the player2 is visible from the player1
	 */
	public boolean isVisible(Player player1, Player player2) {
		MapSquare mapSquarePlayer1 = getSquare(playersPositions.get(player1));
		MapSquare mapSquarePlayer2 = getSquare(playersPositions.get(player2));

		if (mapSquarePlayer1.getRoomID() == mapSquarePlayer2.getRoomID())
			return true;

		for (MapSquare adjacentMapSquare : mapSquarePlayer1.getAdjacentMapSquares()) {
			if (adjacentMapSquare.getRoomID() == mapSquarePlayer2.getRoomID())
				return true;
		}
		return false;
	}

	/**
	 * Returns the coordinates of the spawn square associated with the ammo type or null if it could not find it
	 *
	 * @param ammoType ammo type of the spawn we want
	 * @return the coordinates of the spawn square associated with the ammo type or null if it could not find it
	 */
	public Coordinates getSpawnSquare(AmmoType ammoType) {
		for (Coordinates spawnCoordinates : spawnSquaresCoordinates) {
			if (((SpawnSquare) getSquare(spawnCoordinates)).getAmmoType().equals(ammoType))
				return spawnCoordinates;
		}
		return null;
	}

	/**
	 * Given the mapSquare returns its coordinates in the map.
	 *
	 * @param mapSquare mapSquare we want to know thw coordinates of
	 * @return the coordinates of the mapSquare
	 * @throws OutOfBoundariesException if the mapSquare does not belong to the map.
	 */
	public Coordinates getCoordinates(MapSquare mapSquare) {
		if (isIn(mapSquare))
			return mapSquare.getCoordinates();
		else
			throw new OutOfBoundariesException("mapSquare does not belong to the map");
	}

	/**
	 * Given the coordinates of the map returns the associated square.
	 *
	 * @param coordinates coordinates of the square we want
	 * @return the square in the specified coordinates
	 * @throws OutOfBoundariesException if the coordinates do not belong to the map
	 */
	public MapSquare getSquare(Coordinates coordinates) {
		if (isIn(coordinates))
			return map[coordinates.getRow()][coordinates.getColumn()];
		else
			throw new OutOfBoundariesException("the coordinates do not belong to the map " + coordinates);
	}

	/**
	 * Used to know if in some coordinates there is a spawn square
	 *
	 * @param coordinates coordinates of the square to check
	 * @return true if and only if at the coordinates there is a Spawn square
	 */
	public boolean isSpawnSquare(Coordinates coordinates) {
		return spawnSquaresCoordinates.contains(coordinates);
	}

	/**
	 * Returns true if and only if the coordinates belong to the map
	 *
	 * @param coordinates coordinates to check
	 * @return true if and only if the coordinates belong to the map
	 */
	private boolean isIn(Coordinates coordinates) {
		return (!((coordinates.getRow() >= numOfRows) || (coordinates.getColumn() >= numOfColumns))) &&
				map[coordinates.getRow()][coordinates.getColumn()].getRoomID() != -1;
	}

	/**
	 * Returns true if and only if the mapSquare belong to the map
	 *
	 * @param mapSquare mapSquare to check
	 * @return true if and only if the mapSquare belong to the map
	 */
	private boolean isIn(MapSquare mapSquare) {
		Coordinates coordinates = mapSquare.getCoordinates();
		return isIn(mapSquare.getCoordinates()) && map[coordinates.getRow()][coordinates.getColumn()].equals(mapSquare);
	}

	public MapSquareRep getSquareRep(Coordinates coordinates) {
		return map[coordinates.getRow()][coordinates.getColumn()].getRep();
	}

	/**
	 * Adds a MapSquare to the map according to the specified coordinates.
	 *
	 * @param coordinatesOfTheSquare: coordinates where the square has to be added
	 * @param ammoType:               ammo associated with the square
	 * @param roomID:                 the roomID of the room to which the square belongs to
	 * @param possibleDirections:     array that specify the directions in which the player can move from the square
	 */
	private void addSquareToMap(Coordinates coordinatesOfTheSquare, String ammoType, int roomID, boolean[] possibleDirections, GameBoard gameBoard) {
		if (!ammoType.equals("NONE")) {
			map[coordinatesOfTheSquare.getRow()][coordinatesOfTheSquare.getColumn()] = new SpawnSquare(AmmoType.valueOf(ammoType), roomID, possibleDirections, coordinatesOfTheSquare, gameBoard);
			spawnSquaresCoordinates.add(coordinatesOfTheSquare);
		} else
			map[coordinatesOfTheSquare.getRow()][coordinatesOfTheSquare.getColumn()] = new AmmoSquare(roomID, possibleDirections, coordinatesOfTheSquare, gameBoard);
	}

	/**
	 * Creates the structure to memorize the squares belonging to each room
	 */
	private void addSquaresToRooms() {
		for (int i = 0; i < numOfRows; i++)
			for (int j = 0; j < numOfColumns; j++) {
				int roomID = map[i][j].getRoomID();
				if (roomID >= 0)
					rooms.get(roomID).add(new Coordinates(i, j));
			}
	}

	/**
	 * Links each square to the adjacent ones
	 */
	private void connectSquares() {
		MapSquare mapSquare;
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				for (CardinalDirection direction : CardinalDirection.values()) {
					mapSquare = map[i][j];
					if (mapSquare.getRoomID() != -1 && mapSquare.getPossibleDirections()[direction.ordinal()])
						mapSquare.addAdjacentSquare(getSquare(Coordinates.getDirectionCoordinates(new Coordinates(i, j), direction)));
				}
			}
		}
	}

	/**
	 * Receives the name of the map to generate and initialize the squares according to the file.
	 * <p>
	 * FILE FORMAT:
	 * NUM_OF_ROWS, NUM_OF_COLUMNS
	 * NUM_OF_ROOMS
	 * AMMO_TYPE_ASSOCIATED, ROOMID, CAN_MOVE_UP, CAN_MOVE_RIGHT, CAN_MOVE_DOWN, CAN_MOVE_LEFT <= for each square
	 * <p>
	 * <p>
	 * The declaration of squares follows this order: [0,0], [0,1], ...., [0,NUM_OF_COLUMNS], [1,0], ..., [NUM_OF_ROWS, NUM_OF_COLUMNS].
	 * <p>
	 * To declare that in a position there is no square the roomID has to be '-1'.
	 * <p>
	 * If the square is a Ammo square the ammo type associated is "NONE", otherwise if it is a spawn square the AMMO_TYPE_ASSOCIATED is the color of the spawn.
	 *
	 * @param mapName: name of the map to load
	 */
	private void generateMap(String mapName, GameBoard gameBoard) {

		String mapPath = System.getProperty("user.dir") + "/src/resources/maps/" + mapName;
		String line;
		String separator = ",";
		boolean[] possibleDirections = new boolean[4];

		try (BufferedReader bufReader = new BufferedReader(new FileReader(mapPath))) {

			line = bufReader.readLine();
			String[] elements = line.split(separator);

			numOfRows = Integer.parseInt(elements[0]);
			numOfColumns = Integer.parseInt(elements[1]);

			map = new MapSquare[numOfRows][numOfColumns];

			line = bufReader.readLine();

			for (int i = 0; i < Integer.parseInt(line); i++)
				rooms.add(new ArrayList<>());

			for (int i = 0; i < numOfRows; i++) {
				for (int j = 0; j < numOfColumns; j++) {

					line = bufReader.readLine();
					elements = line.split(separator);

					possibleDirections[0] = Boolean.parseBoolean(elements[2]);
					possibleDirections[1] = Boolean.parseBoolean(elements[3]);
					possibleDirections[2] = Boolean.parseBoolean(elements[4]);
					possibleDirections[3] = Boolean.parseBoolean(elements[5]);

					addSquareToMap(new Coordinates(i, j), elements[0], Integer.parseInt(elements[1]), possibleDirections.clone(), gameBoard);
				}
			}

		} catch (IOException e) {
			Utils.logError("Error in generateMap()", e);
		}
	}

	/**
	 *
	 */
	public void updateRep() {
		if (gameMapRep == null || hasChanged()) {
			gameMapRep = new GameMapRep(this);
			Utils.logInfo("Game board rep updated");
		}
	}

	/**
	 *
	 * @return
	 */
	public Representation getRep() {
		return gameMapRep;
	}
}

/**
 * Thrown when some coordinates are out of the map.
 *
 * @author MarcerAndrea
 */
class OutOfBoundariesException extends RuntimeException {

	/**
	 * Constructs an OutOfBoundariesException with the specified message.
	 *
	 * @param message the detail message.
	 */
	public OutOfBoundariesException(String message) {
		super(message);
	}
}

/**
 * Thrown when a player is not in the Map.
 *
 * @author MarcerAndrea
 */
class PlayerNotInTheMapException extends RuntimeException {

	/**
	 * Constructs an PlayerNotInTheMapException with the specified message.
	 *
	 * @param message the detail message.
	 */
	public PlayerNotInTheMapException(String message) {
		super(message);
	}
}