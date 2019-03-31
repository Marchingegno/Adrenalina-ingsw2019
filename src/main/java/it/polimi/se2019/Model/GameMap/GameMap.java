package it.polimi.se2019.Model.GameMap;

import it.polimi.se2019.Model.Cards.Ammo.AmmoType;
import it.polimi.se2019.Model.Player.Player;

import java.util.*;

/**
 *
 */
public class GameMap {

	/**
	 * Default constructor
	 */
	public GameMap() {
	}

	/**
	 *
	 */
	private ArrayList<ArrayList<Square>> gameMap;

	/**
	 *
	 */
	private HashMap<Player, Coordinates> playersPositions;

	/**
	 *
	 */
	private ArrayList<SpawnSquare> spawnSquares;

	/**
	 * @param map
	 */
	public void GameMap(ArrayList<ArrayList<Square>> map) {
		// TODO implement here
	}

	/**
	 * @param playerToFind
	 * @return
	 */
	public Coordinates playerCoordinates(Player playerToFind) {
		// TODO implement here
		return null;
	}

	/**
	 * @param playerToMove
	 * @param coordinates
	 */
	public void movePlayerTo(Player playerToMove, Coordinates coordinates) {
		// TODO implement here
	}

	/**
	 * ricorsiva
	 *
	 * @param coordinates
	 * @param maxDistance
	 * @return
	 */
	public ArrayList<Coordinates> reachableSquares(Coordinates coordinates, int maxDistance) {
		// TODO implement here
		return null;
	}

	/**
	 * @param coordinates1
	 * @param coordinates2
	 * @return
	 */
	public boolean visible(Coordinates coordinates1, Coordinates coordinates2) {
		// TODO implement here
		return false;
	}

	/**
	 * @param square
	 * @return
	 */
	public Coordinates getPosition(Square square) {
		// TODO implement here
		return null;
	}

	/**
	 * @param ammoType
	 * @return
	 */
	public Coordinates getSpawnSquare(AmmoType ammoType) {
		// TODO implement here
		return null;
	}

	/**
	 * @param square
	 * @return
	 */
	public Coordinates getCoordinatesFromSquare(Square square) {
		// TODO implement here
		return null;
	}

	/**
	 * @param player1
	 * @param player2
	 * @return
	 */
	public int getDistance(Player player1, Player player2) {
		// TODO implement here
		return 0;
	}

	/**
	 * @param coordinates
	 * @return
	 */
	public Square getSquareFromCoordinates(Coordinates coordinates) {
		// TODO implement here
		return null;
	}

	/**
	 * @param coordinates
	 * @return
	 */
	public boolean isSpawnSquare(Coordinates coordinates) {
		// TODO implement here
		return false;
	}

	/**
	 * @param square
	 * @return
	 */
	public boolean isSpawnSquare(Square square) {
		// TODO implement here
		return false;
	}

}