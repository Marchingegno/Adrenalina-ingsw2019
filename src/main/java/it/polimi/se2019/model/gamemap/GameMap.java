package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class implements the game map
 * @author MarcerAndrea
 */
public class GameMap {

	int numOfRows, numOfColumns;
	private Square[][] gameMap;
	private HashMap<Player, Coordinates> playersPositions;
	private ArrayList<Coordinates> spawnSquaresCoordinates = new ArrayList<>();

	public GameMap(String mapName) {
		generateMap(mapName);
	}

	public Coordinates playerCoordinates(Player playerToFind) {
		return null;
	}

	public void movePlayerTo(Player playerToMove, Coordinates coordinates) {
	}

	public ArrayList<Coordinates> reachableSquares(Coordinates coordinates, int maxDistance) {
		return null;
	}

	public boolean visible(Coordinates coordinates1, Coordinates coordinates2) {
		return false;
	}

	public Coordinates getCoordinates(Square square) {
		return null;
	}

	public Coordinates getSpawnSquare(AmmoType ammoType) {
		return null;
	}

	public Coordinates getCoordinatesFromSquare(Square square) {
		return null;
	}

	public int getDistance(Player player1, Player player2) {
		return 0;
	}

	public Square getSquareFromCoordinates(Coordinates coordinates) {
		return null;
	}

	/**
	 * @param coordinates: coordinates of the square to check
	 * @return returns true if and only if at the coordinates there is a Spawn square
	 */
	public boolean isSpawnSquare(Coordinates coordinates) { return spawnSquaresCoordinates.contains(coordinates);	}

	/**
	 * Adds a Square to the map according to the specified coordinates.
	 * @param coordinatesOfTheSquare: coordinates where the square has to be added
	 * @param ammoType: ammo associated with the square
	 * @param roomID: the roomID of the room to which the square belongs to
	 * @param possibleDirections: array that specify the directions in which the player can move from the square
	 */
	private void addSquare(Coordinates coordinatesOfTheSquare, String ammoType, int roomID, boolean[] possibleDirections) {
		if(!ammoType.equals("NONE")){
			gameMap[coordinatesOfTheSquare.getRow()][coordinatesOfTheSquare.getColumn()] = new SpawnSquare(AmmoType.valueOf(ammoType), roomID, possibleDirections);
			spawnSquaresCoordinates.add(coordinatesOfTheSquare);
		}
		else
			gameMap[coordinatesOfTheSquare.getRow()][coordinatesOfTheSquare.getColumn()] = new AmmoSquare(roomID, possibleDirections);
	}

	/**
	 * Receives the name of the map to generate and initialize the squares according to the file.
	 *
	 * FILE FORMAT:
	 * NUM_OF_ROWS, NUM_OF_COLUMNS
	 * AMMO_TYPE_ASSOCIATED, ROOMID, CAN_MOVE_UP, CAN_MOVE_RIGHT, CAN_MOVE_DOWN, CAN_MOVE_LEFT <= for each square
	 *
	 *
	 * The declaration of squares follows this order: [0,0], [0,1], ...., [0,NUM_OF_COLUMNS], [1,0], ..., [NUM_OF_ROWS, NUM_OF_COLUMNS].
	 *
	 * To declare that in a position there is no square the roomID has to be '0'.
	 *
	 * If the square is a Ammo square the ammo type associated is "NONE", otherwise if it is a spawn square the AMMO_TYPE_ASSOCIATED is the color of the spawn.
	 *
	 * @param mapName: name of the map to load
	 */
	private void generateMap(String mapName) {

		String mapPath = System.getProperty("user.dir") + "\\src\\resources\\maps\\" + mapName;
		String line;
		String separator = ",";
		boolean[] possibleDirections = new boolean[4];

		try (BufferedReader bufReader = new BufferedReader(new FileReader(mapPath))) {

			line = bufReader.readLine();
			String[] elements = line.split(separator);

			numOfRows = Integer.parseInt(elements[0]);
			numOfColumns = Integer.parseInt(elements[1]);

			for (int i = 0; i < numOfRows; i++){
				for (int j = 0; j < numOfColumns; j++){

					line = bufReader.readLine();
					elements = line.split(separator);

					possibleDirections[0] = Boolean.parseBoolean(elements[2]);
					possibleDirections[1] = Boolean.parseBoolean(elements[3]);
					possibleDirections[2] = Boolean.parseBoolean(elements[4]);
					possibleDirections[3] = Boolean.parseBoolean(elements[5]);

					addSquare(new Coordinates(i, j), elements[0], Integer.parseInt(elements[1]), possibleDirections);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}