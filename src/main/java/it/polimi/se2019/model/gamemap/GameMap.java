package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

/**
 * This class implements the game map
 * @author MarcerAndrea
 */
public class GameMap extends Observable {

	private int numOfRows;
	private int	numOfColumns;
	private Square[][] map;
	private ArrayList<ArrayList<Coordinates>> rooms = new ArrayList<>();
	private HashMap<Player, Coordinates> playersPositions = new HashMap<>();
	private ArrayList<Coordinates> spawnSquaresCoordinates = new ArrayList<>();

	public GameMap(String mapName, List<Player> players) {

		generateMap(mapName);
		connectSquares();
		addSquaresToRoom();

		for (Player playerToAdd: players ) {
			playersPositions.put(playerToAdd, null);
		}

		setChanged();
	}

	/**
	 * Returns the number of rows.
	 * @return the number of rows
	 */
	public int getNumOfRows() {
		return numOfRows;
	}

	/**
	 * Returns the number of columns.
	 * @return the number of columns
	 */
	public int getNumOfColumns() {
		return numOfColumns;
	}

	/**
	 * Returns the player position.
	 * @return the player position
	 */
	public HashMap<Player, Coordinates> getPlayersCoordinates() {
		return new HashMap<>(playersPositions);
	}

	/**
	 * Returns the player's coordinates.
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
	 * @param playerToFind player to find
	 * @return the player's square
	 * @throws PlayerNotInTheMapException when the player is not in the map
	 */
	public Square playerSquare(Player playerToFind) {
		Coordinates playerCoordinates = playersPositions.get(playerToFind);
		if (playerCoordinates != null)
			return map[playerCoordinates.getRow()][playerCoordinates.getColumn()];
		else
			throw new PlayerNotInTheMapException("player position not in the map");
	}

	/**
	 * Moves the player in the specified coordinates.
	 * @param playerToMove player to move
	 * @param coordinates coordinates where the player has to be moved to
	 * @throws OutOfBoundariesException when the player is moved to a square not in the map
	 */
	public void movePlayerTo(Player playerToMove, Coordinates coordinates) {
		if (isIn(coordinates))
			playersPositions.replace(playerToMove, coordinates);
		else
			throw new OutOfBoundariesException("tried to move the player out of the map" + coordinates.toString());
		setChanged();
	}

	public ArrayList<Coordinates> reachableCoordinates(Player player, int maxDistance) {
		ArrayList<Coordinates> reachableCoordinates = new ArrayList<>();
		return reachableSquares(getSquare(playersPositions.get(player)), maxDistance, reachableCoordinates);
	}

	public ArrayList<Coordinates> reachableCoordinates(Coordinates coordinates, int maxDistance) {
		ArrayList<Coordinates> reachableCoordinates = new ArrayList<>();
		return reachableSquares(getSquare(coordinates), maxDistance, reachableCoordinates);
	}

	/**
	 * Returns the set of all reachable squares from the coordinates and distance at most max distance.
	 * @param square square of the starting point
	 * @param maxDistance maximum distance
	 * @return the set of all reachable squares from the coordinates and distance at most max distance
	 */
	private ArrayList<Coordinates> reachableSquares(Square square, int maxDistance, ArrayList<Coordinates> reachableCoordinates) {

		if (maxDistance != 0)
		{
			for (Square adjacentSquare: square.getAdjacentSquares()) {
				reachableSquares(adjacentSquare, maxDistance - 1, reachableCoordinates);
			}
		}

		if (!(reachableCoordinates.contains(getCoordinates(square))))
			reachableCoordinates.add(getCoordinates(square));
		return reachableCoordinates;
	}

	/**
	 * Returns the set of all the squares belonging to the same room of the specified square.
	 * @param square a square
	 * @return the set of all the squares belonging to the same room of the specified square
	 * @throws OutOfBoundariesException when the square does not belong to the map
	 */
	public ArrayList<Coordinates> getRoomCoordinates(Square square) {
		if (isIn(square))
			return new ArrayList<>(rooms.get(square.getRoomID()));
		else
			throw new OutOfBoundariesException("the square does not belong to the map " + getCoordinates(square));
	}

	/**
	 * Returns the set of all the squares belonging to the same room of the specified square.
	 * @param coordinates a coordinates
	 * @return the set of all the squares belonging to the same room of the specified square
	 * @throws OutOfBoundariesException the coordinates do not belong to the map
	 */
	public ArrayList<Coordinates> getRoomCoordinates(Coordinates coordinates) {
		if (isIn(coordinates))
			return new ArrayList<>(rooms.get(getSquare(coordinates).getRoomID()));
		else
			throw new OutOfBoundariesException("the coordinates do not belong to the map " + coordinates);
	}

	/**
	 * Returns true if and only if the player2 is visible from the player1. This is done looking at the rooms ID of the squares where the player are.
	 * @param player1 player how is observing
	 * @param player2 player target
	 * @return true if and only if the player2 is visible from the player1
	 */
	public boolean visible(Player player1, Player player2) {
		Square squarePlayer1 = getSquare(playersPositions.get(player1));
		Square squarePlayer2 = getSquare(playersPositions.get(player2));

		if (squarePlayer1.getRoomID() == squarePlayer2.getRoomID())
			return true;

		for (Square adjacentSquare : squarePlayer1.getAdjacentSquares()) {
			if (adjacentSquare.getRoomID() == squarePlayer2.getRoomID())
				return true;
		}
		return false;
	}

	/**
	 * Returns the coordinates of the spawn square associated with the ammo type or null if it could not find it
	 * @param ammoType ammo type of the spawn we want
	 * @return the coordinates of the spawn square associated with the ammo type or null if it could not find it
	 */
	public Coordinates getSpawnSquare(AmmoType ammoType) {
		for (Coordinates spawnCoordinates: spawnSquaresCoordinates) {
			if (((SpawnSquare) getSquare(spawnCoordinates)).getAmmoType().equals(ammoType))
				return spawnCoordinates;
		}
		return null;
	}

	/**
	 * Given the square returns its coordinates in the map.
	 * @param square square we want to know thw coordinates of
	 * @return the coordinates of the square
	 * @throws OutOfBoundariesException if the square does not belong to the map.
	 */
	public Coordinates getCoordinates(Square square){
		if (isIn(square))
			return square.getCoordinates();
		else
			throw new OutOfBoundariesException("square does not belong to the map");
	}

	/**
	 * Given the coordinates of the map returns the associated square.
	 * @param coordinates coordinates of the square we want
	 * @return the square in the specified coordinates
	 * @throws OutOfBoundariesException if the coordinates do not belong to the map
	 */
	public Square getSquare(Coordinates coordinates){
		if (isIn(coordinates))
			return map[coordinates.getRow()][coordinates.getColumn()];
		else
			throw new OutOfBoundariesException("the coordinates do not belong to the map " + coordinates);
	}

	/**
	 * Used to know if in some coordinates there is a spawn square
	 * @param coordinates coordinates of the square to check
	 * @return true if and only if at the coordinates there is a Spawn square
	 */
	public boolean isSpawnSquare(Coordinates coordinates) { return spawnSquaresCoordinates.contains(coordinates);}

	/**
	 * Returns true if and only if the coordinates belong to the map
	 * @param coordinates coordinates to check
	 * @return true if and only if the coordinates belong to the map
	 */
	private boolean isIn(Coordinates coordinates){
		return (!((coordinates.getRow() >= numOfRows) || (coordinates.getColumn() >= numOfColumns))) &&
				map[coordinates.getRow()][coordinates.getColumn()].getRoomID() != -1;}

	/**
	 * Returns true if and only if the square belong to the map
	 * @param square square to check
	 * @return true if and only if the square belong to the map
	 */
	private boolean isIn(Square square){
		Coordinates coordinates = square.getCoordinates();
		return isIn(square.getCoordinates()) &&	map[coordinates.getRow()][coordinates.getColumn()].equals(square);
	}

	/**
	 * Adds a Square to the map according to the specified coordinates.
	 * @param coordinatesOfTheSquare: coordinates where the square has to be added
	 * @param ammoType: ammo associated with the square
	 * @param roomID: the roomID of the room to which the square belongs to
	 * @param possibleDirections: array that specify the directions in which the player can move from the square
	 */
	private void addSquareToMap(Coordinates coordinatesOfTheSquare, String ammoType, int roomID, boolean[] possibleDirections) {
		if(!ammoType.equals("NONE")){
			map[coordinatesOfTheSquare.getRow()][coordinatesOfTheSquare.getColumn()] = new SpawnSquare(AmmoType.valueOf(ammoType), roomID, possibleDirections, coordinatesOfTheSquare);
			spawnSquaresCoordinates.add(coordinatesOfTheSquare);
		}
		else
			map[coordinatesOfTheSquare.getRow()][coordinatesOfTheSquare.getColumn()] = new AmmoSquare(roomID, possibleDirections, coordinatesOfTheSquare);
	}

	/**
	 * Creates the structure to memorize the squares belonging to each room
	 */
	private void addSquaresToRoom(){
		for (int i = 0; i < numOfRows; i++)
			for (int j = 0;  j < numOfColumns; j++){
				int roomID = map[i][j].getRoomID();
				if (roomID >= 0)
					rooms.get(roomID).add(new Coordinates(i,j));
			}
	}

	/**
	 * Links each square to the adjacent ones
	 */
	private void connectSquares(){
		Square square;
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				for (CardinalDirection direction: CardinalDirection.values()) {
					square = map[i][j];
					if(square.getRoomID() != -1 && square.getPossibleDirections()[direction.ordinal()])
						square.addAdjacentSquare(getSquare(Coordinates.getDirectionCoordinates(new Coordinates(i,j), direction)));
				}
			}
		}
	}

	/**
	 * Receives the name of the map to generate and initialize the squares according to the file.
	 *
	 * FILE FORMAT:
	 * NUM_OF_ROWS, NUM_OF_COLUMNS
	 * NUM_OF_ROOMS
	 * AMMO_TYPE_ASSOCIATED, ROOMID, CAN_MOVE_UP, CAN_MOVE_RIGHT, CAN_MOVE_DOWN, CAN_MOVE_LEFT <= for each square
	 *
	 *
	 * The declaration of squares follows this order: [0,0], [0,1], ...., [0,NUM_OF_COLUMNS], [1,0], ..., [NUM_OF_ROWS, NUM_OF_COLUMNS].
	 *
	 * To declare that in a position there is no square the roomID has to be '-1'.
	 *
	 * If the square is a Ammo square the ammo type associated is "NONE", otherwise if it is a spawn square the AMMO_TYPE_ASSOCIATED is the color of the spawn.
	 *
	 * @param mapName: name of the map to load
	 */
	private void generateMap(String mapName) {

		String mapPath = System.getProperty("user.dir") + "/src/resources/maps/" + mapName;
		String line;
		String separator = ",";
		boolean[] possibleDirections = new boolean[4];

		try (BufferedReader bufReader = new BufferedReader(new FileReader(mapPath))) {

			line = bufReader.readLine();
			String[] elements = line.split(separator);

			numOfRows = Integer.parseInt(elements[0]);
			numOfColumns = Integer.parseInt(elements[1]);

			map = new Square[numOfRows][numOfColumns];

			line = bufReader.readLine();

			for (int i = 0; i < Integer.parseInt(line); i++)
				rooms.add(new ArrayList<>());

			for (int i = 0; i < numOfRows; i++){
				for (int j = 0; j < numOfColumns; j++){

					line = bufReader.readLine();
					elements = line.split(separator);

					possibleDirections[0] = Boolean.parseBoolean(elements[2]);
					possibleDirections[1] = Boolean.parseBoolean(elements[3]);
					possibleDirections[2] = Boolean.parseBoolean(elements[4]);
					possibleDirections[3] = Boolean.parseBoolean(elements[5]);

					addSquareToMap(new Coordinates(i, j), elements[0], Integer.parseInt(elements[1]), possibleDirections.clone());
				}
			}

		} catch (IOException e) {
			Utils.logError("Error in generateMap()", e);
		}
	}
}

/**
 * Thrown when some coordinates are out of the map.
 * @author MarcerAndrea
 */
class OutOfBoundariesException extends RuntimeException {

	/**
	 * Constructs an OutOfBoundariesException with the specified message.
	 * @param message the detail message.
	 */
	public OutOfBoundariesException(String message) {
		super(message);
	}
}

/**
 * Thrown when a player is not in the Map.
 * @author MarcerAndrea
 */
class PlayerNotInTheMapException extends RuntimeException {

	/**
	 * Constructs an PlayerNotInTheMapException with the specified message.
	 * @param message the detail message.
	 */
	public PlayerNotInTheMapException(String message) {
		super(message);
	}
}