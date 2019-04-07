package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMap {

	private ArrayList<ArrayList<Square>> gameMap;
	private HashMap<Player, Coordinates> playersPositions;
	private ArrayList<SpawnSquare> spawnSquares;


	public GameMap(String mapPath) {
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

	public Coordinates getPosition(Square square) {
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

	public boolean isSpawnSquare(Coordinates coordinates) {
		return false;
	}

	public boolean isSpawnSquare(Square square) {
		return false;
	}

	private void addSquare(boolean isSpawn, int roomI, ArrayList<Boolean> possibleDirections) {
	}

	private void setDimentions(int row, int column) {
	}

	private void initializeMap(String mapPath) {
	}

}