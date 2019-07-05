package it.polimi.se2019.model.gamemap;

import com.google.gson.*;
import it.polimi.se2019.model.Representable;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.utils.exceptions.OutOfBoundariesException;
import it.polimi.se2019.utils.exceptions.PlayerNotInTheMapException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * This class implements the game map
 *
 * @author MarcerAndrea
 */
public class GameMap extends Observable implements Representable {

	private String mapName;
	private int numOfRows;
	private int numOfColumns;
	private Square[][] map;
	private List<List<Coordinates>> rooms = new ArrayList<>();
	private HashMap<Player, Coordinates> playersPositions = new HashMap<>();
	private List<Coordinates> spawnSquaresCoordinates = new ArrayList<>();
	private GameMapRep gameMapRep;

	public GameMap(String mapName, GameBoard gameBoard) {

		generateMapJson(mapName, gameBoard);
		connectSquares();
		addSquaresToRooms();
		refillMap();

		for (Player playerToAdd : gameBoard.getPlayers()) {
			playersPositions.put(playerToAdd, null);
		}

		this.mapName = mapName;

		setChanged();
	}

	// ####################################
	// MAP METHODS
	// ####################################

	/**
	 * Returns the name of the map.
	 *
	 * @return the name of the map.
	 */
	public String getName() {
		return mapName;
	}

	/**
	 * Returns the number of rows.
	 *
	 * @return the number of rows.
	 */
	int getNumOfRows() {
		return numOfRows;
	}

	/**
	 * Returns the number of columns.
	 *
	 * @return the number of columns.
	 */
	int getNumOfColumns() {
		return numOfColumns;
	}

	/**
	 * Returns the list of all the coordinates in the map.
	 *
	 * @return the list of all the coordinates in the map.
	 */
	public List<Coordinates> getAllCoordinates() {
		List<Coordinates> coordinatesList = new ArrayList<>();
		Coordinates coordinates;
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				coordinates = new Coordinates(i, j);
				if (isIn(coordinates))
					coordinatesList.add(coordinates);
			}
		}
		Utils.logInfo("GameMap -> getAllCoordinates(): Map coordinates are: " + coordinatesList);
		return coordinatesList;
	}

	/**
	 * Returns the list of all the coordinates in the map except the player one.
	 *
	 * @param player player whom coordinates need to be removed from the list.
	 * @return the list of all the coordinates in the map except the player one.
	 */
	public List<Coordinates> getAllCoordinatesExceptPlayer(Player player) {
		List<Coordinates> result = getAllCoordinates();
		result.remove(getPlayerCoordinates(player));
		Utils.logInfo("GameMap -> getAllCoordinatesExceptPlayer(): Map coordinates without " + player.getPlayerName() + " are: " + result);
		return result;
	}

	/**
	 * Removes from the square where the player is the card in the specified index.
	 *
	 * @param coordinates coordinates of the square.
	 * @param index       index of the card's solt to grab.
	 * @return the card grabbed.
	 */
	public Card grabCard(Coordinates coordinates, int index) {
		setChanged();
		return getSquare(coordinates).grabCard(index);
	}

	/**
	 * Adds the card to the square's slot.
	 *
	 * @param coordinates coordinates of the square where the card needs to be added.
	 * @param cardToAdd   the card to add.
	 */
	public void addCard(Coordinates coordinates, Card cardToAdd) {
		setChanged();
		getSquare(coordinates).addCard(cardToAdd);
	}

	/**
	 * For each square in the map if it has an empty slot
	 * it gets refilled.
	 */
	public void refillMap() {
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				map[i][j].refillCards();
			}
		}
		setChanged();
		Utils.logInfo("GameMap -> refillMap(): Map completely filled");
	}

	/**
	 * Returns all the coordinates of the doors nearby the player.
	 *
	 * @param player player to check.
	 * @return all the coordinates of the doors nearby the player.
	 */
	public List<Coordinates> getDoors(Player player) {
		Square playerSquare = getPlayerSquare(player);
		List<Coordinates> doors = new ArrayList<>();
		for (Square square : playerSquare.getAdjacentSquares()) {
			if (square.getRoomID() != playerSquare.getRoomID())
				doors.add(square.getCoordinates());
		}
		Utils.logInfo("GameMap -> getDoors(): Doors for " + player.getPlayerName() + " in " + getPlayerCoordinates(player) + " are: " + doors);
		return doors;
	}

	/**
	 * Returns the set of all the squares belonging to the same room of the specified square.
	 *
	 * @param square a square
	 * @return the set of all the squares belonging to the same room of the specified square
	 * @throws OutOfBoundariesException when the square does not belong to the map
	 */
	public List<Coordinates> getRoomCoordinates(Square square) {
		if (isIn(square))
			return new ArrayList<>(rooms.get(square.getRoomID()));
		throw new OutOfBoundariesException("the square does not belong to the map " + getCoordinates(square));
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
		throw new OutOfBoundariesException("the coordinates do not belong to the map " + coordinates);
	}

	/**
	 * Returns the coordinates of the spawn square associated with the ammo type or null if it could not find it
	 *
	 * @param ammoType ammo type of the spawn we want
	 * @return the coordinates of the spawn square associated with the ammo type or null if it could not find it
	 */
	public Coordinates getSpawnCoordinates(AmmoType ammoType) {
		for (Coordinates spawnCoordinates : spawnSquaresCoordinates) {
			if (((SpawnSquare) getSquare(spawnCoordinates)).getAmmoType().equals(ammoType))
				return spawnCoordinates;
		}
		return null;
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
	 * If is possible to move in the direction from the coordinates returns the coordinates in that direction, null otherwise.
	 *
	 * @param coordinates starting coordinates.
	 * @param direction   the direction towards to move.
	 * @return the coordinates in the specified direction starting fomr the coordinates, null otherwise.
	 */
	public Coordinates getCoordinatesFromDirection(Coordinates coordinates, CardinalDirection direction) {
		if (getSquare(coordinates).getPossibleDirections()[direction.ordinal()])
			return Coordinates.getDirectionCoordinates(coordinates, direction);
		return null;
	}

	/**
	 * Returns all the Coordinates of the adjacent rooms.
	 *
	 * @param coordinates starting coordinates.
	 * @return all the Coordinates of the adjacent rooms.
	 */
	public List<Coordinates> getAdjacentRoomsCoordinates(Coordinates coordinates) {
		List<Coordinates> coordinatesOfAdjacentRooms = new ArrayList<>();
		List<Integer> adjacentRooms = new ArrayList<>();
		for (Square square : getSquare(coordinates).getAdjacentSquares()) {
			if (!(adjacentRooms.contains(square.getRoomID()) || square.getRoomID() == getSquare(coordinates).getRoomID()))
				adjacentRooms.add(square.getRoomID());
		}
		for (Integer roomID : adjacentRooms) {
			coordinatesOfAdjacentRooms.addAll(rooms.get(roomID));
		}
		return coordinatesOfAdjacentRooms;
	}
	// ####################################
	// PLAYER METHODS
	// ####################################

	/**
	 * Returns the position of all players.
	 *
	 * @return the position of all players.
	 */
	public Map<Player, Coordinates> getPlayersCoordinates() {
		return new HashMap<>(playersPositions);
	}

	/**
	 * Returns the player's coordinates.
	 *
	 * @param playerToFind player to find.
	 * @return the player's coordinates.
	 * @throws PlayerNotInTheMapException when the player is not in the map.
	 */
	public Coordinates getPlayerCoordinates(Player playerToFind) {
		Coordinates playerCoordinates = playersPositions.get(playerToFind);
		if (playerCoordinates != null)
			return playerCoordinates;
		else
			throw new PlayerNotInTheMapException("player position is null");
	}

	public boolean isInTheMap(Player player) {
		return playersPositions.get(player) != null;
	}

	/**
	 * Returns the player's square.
	 *
	 * @param playerToFind player to find.
	 * @return the player's square.
	 * @throws PlayerNotInTheMapException when the player is not in the map.
	 */
	public Square getPlayerSquare(Player playerToFind) {
		Coordinates playerCoordinates = playersPositions.get(playerToFind);
		if (playerCoordinates != null)
			return map[playerCoordinates.getRow()][playerCoordinates.getColumn()];
		else
			throw new PlayerNotInTheMapException("player position is null");
	}

	/**
	 * Returns the list of players that are in the specified coordinates.
	 *
	 * @param coordinates to check.
	 * @return the list of players that are in the specified coordinates.
	 */
	public List<Player> getPlayersFromCoordinates(Coordinates coordinates) {
		List<Player> players = new ArrayList<>();
		if (coordinates == null)
			throw new NullPointerException();
		for (Map.Entry entry : playersPositions.entrySet()) {
			Player player = (Player) entry.getKey();
			if (isInTheMap(player) && getPlayerCoordinates(player).equals(coordinates))
				players.add(player);
		}

		return players;
	}

	/**
	 * Moves the player in the specified coordinates.
	 *
	 * @param playerToMove player to move
	 * @param coordinates  coordinates where the player has to be moved to
	 * @throws OutOfBoundariesException when the player is moved to a square not in the map
	 */
	public void movePlayerTo(Player playerToMove, Coordinates coordinates) {
		if (isIn(coordinates)) {
			playersPositions.replace(playerToMove, coordinates);
			setChanged();
			Utils.logInfo("GameMap -> movePlayerTo(): " + playerToMove.getPlayerName() + " moved to " + coordinates);
		} else {
			throw new OutOfBoundariesException("tried to move the player out of the map" + coordinates.toString());
		}
	}

	/**
	 * Returns all the players in one direction, included the starting coordinates.
	 *
	 * @param coordinates starting coordinates.
	 * @param direction   towards to move.
	 * @return all the players in one direction, included the starting coordinates.
	 */
	public List<Player> getPlayersInDirection(Coordinates coordinates, CardinalDirection direction) {
		List<Player> players = new ArrayList<>();
		Coordinates coordinatesToCheck = coordinates;
		while (coordinatesToCheck != null && isIn(coordinatesToCheck)) {
			players.addAll(getPlayersFromCoordinates(coordinatesToCheck));
			coordinatesToCheck = Coordinates.getDirectionCoordinates(coordinatesToCheck, direction);
		}
		return players;
	}

	/**
	 * Returns the list of all reachable coordinates by a player within the specified distance.
	 *
	 * @param player      the player that wants to move.
	 * @param maxDistance distance the player wants to move.
	 * @return the list of reachable coordinates.
	 */
	public List<Coordinates> reachableCoordinates(Player player, int maxDistance) {
		List<Coordinates> reachableCoordinates = new ArrayList<>();
		reachableSquares(getSquare(playersPositions.get(player)), maxDistance, reachableCoordinates);
		Utils.logInfo("GameMap -> reachableCoordinates(): " + player.getPlayerName() + " can reach in " + maxDistance + " moves: " + reachableCoordinates);
		return reachableCoordinates;
	}


	/**
	 * Returns the list of all reachable coordinates from the specified coordinates within the specified distance.
	 *
	 * @param coordinates starting coordinates.
	 * @param maxDistance distance the player wants to move.
	 * @return the list of reachable coordinates.
	 */
	public List<Coordinates> reachableCoordinates(Coordinates coordinates, int maxDistance) {
		ArrayList<Coordinates> reachableCoordinates = new ArrayList<>();
		reachableSquares(getSquare(coordinates), maxDistance, reachableCoordinates);
		Utils.logInfo("GameMap -> reachableCoordinates(): From " + coordinates + " is possible to reach in " + maxDistance + " moves: " + reachableCoordinates);
		return reachableCoordinates;
	}

	/**
	 * Returns a list of all coordinates reachable with a number of moves but following only the cardinal directions.
	 *
	 * @param player   the player that wants to move.
	 * @param distance the maximum amount of moves.
	 * @return a list of all coordinates reachable with the number of moves specified but following only cardinal directions.
	 */
	public List<Coordinates> reachablePerpendicularCoordinates(Player player, int distance) {
		ArrayList<Coordinates> possibleMoves = new ArrayList<>();
		Coordinates currentCoordinates;
		Coordinates nextCoordinates;
		for (CardinalDirection direction : CardinalDirection.values()) {
			int currentDistance = 1;
			boolean canMove = true;
			currentCoordinates = getPlayerCoordinates(player);
			while (currentDistance <= distance && canMove) {
				nextCoordinates = getCoordinatesFromDirection(currentCoordinates, direction);
				if (nextCoordinates != null) {
					possibleMoves.add(nextCoordinates);
					currentCoordinates = nextCoordinates;
					currentDistance++;
				} else {
					canMove = false;
				}
			}
		}
		return possibleMoves;
	}

	/**
	 * Returns the list of Coordinates where the player can grab.
	 *
	 * @param player      the player who is grabbing.
	 * @param maxDistance maximum distance the player can move.
	 * @return the list of Coordinates where the player can grab.
	 */
	public List<Coordinates> getCoordinatesWhereCurrentPlayerCanGrab(Player player, int maxDistance) {
		List<Coordinates> reachableCoordinates = reachableCoordinates(getPlayerCoordinates(player), maxDistance);
		List<Coordinates> reachableAndNotEmptyCoordinates = new ArrayList<>();
		for (Coordinates coordinates : reachableCoordinates) {
			if (getSquare(coordinates).canGrab(player))
				reachableAndNotEmptyCoordinates.add(coordinates);
		}
		return reachableAndNotEmptyCoordinates;
	}


	/**
	 * Returns true if and only if the player2 is visible from the player1. This is done looking at the rooms ID of the squares where the player are.
	 *
	 * @param watchingPlayer player how is observing.
	 * @param otherPlayer    player target.
	 * @return true if and only if the player2 is visible from the player1.
	 */
	public boolean isVisible(Player watchingPlayer, Player otherPlayer) {
		Coordinates watchingPlayerPostition = playersPositions.get(watchingPlayer);
		Coordinates otherPlayerPostition = playersPositions.get(otherPlayer);

		if (watchingPlayerPostition == null)
			throw new PlayerNotInTheMapException(watchingPlayer.getPlayerName() + " not in the map");

		if (otherPlayerPostition == null)
			return false;

		if (watchingPlayer.getPlayerName().equals(otherPlayer.getPlayerName()))
			throw new IllegalArgumentException("players must be different");

		return isVisible(watchingPlayerPostition, otherPlayerPostition);
	}

	/**
	 * Returns true if and only if the coordinate1 can see coordinate2. This is done looking at the rooms ID of the coordinates.
	 *
	 * @param coordinates1 starting coordinates.
	 * @param coordinates2 end coordinates.
	 * @return true if and only if the player2 is visible from the player1.
	 */
	public boolean isVisible(Coordinates coordinates1, Coordinates coordinates2) {

		if (coordinates1 == null || coordinates2 == null)
			throw new NullPointerException();

		if (coordinates1.equals(coordinates2))
			return true;

		Square square1 = getSquare(coordinates1);
		Square square2 = getSquare(coordinates2);

		if (square1.getRoomID() == square2.getRoomID()) {
			Utils.logInfo("GameMap -> isVisible(): " + square1.getCoordinates() + " can see " + square2.getCoordinates());
			return true;
		}

		for (Square adjacentSquare : square1.getAdjacentSquares()) {
			if (adjacentSquare.getRoomID() == square2.getRoomID()) {
				Utils.logInfo("GameMap -> isVisible(): " + square1.getCoordinates() + " can see " + square2.getCoordinates());
				return true;
			}
		}
		Utils.logInfo("GameMap -> isVisible():  " + square1.getCoordinates() + " cannot see " + square2.getCoordinates());
		return false;
	}

	/**
	 * Returns all visible players from the specified coordinate.
	 *
	 * @param coordinates starting coordinates.
	 * @return all visible players from the specified coordinate.
	 */
	public List<Player> getVisiblePlayers(Coordinates coordinates) {
		List<Player> visiblePlayers = new ArrayList<>();

		for (Player player : playersPositions.keySet()) {
			if (isInTheMap(player) && isVisible(coordinates, getPlayerCoordinates(player)))
				visiblePlayers.add(player);
		}
		return visiblePlayers;
	}

	/**
	 * Returns all visible players.
	 *
	 * @param mainPlayer player who is watching.
	 * @return all visible players.
	 */
	public List<Player> getVisiblePlayers(Player mainPlayer) {
		List<Player> visiblePlayers = new ArrayList<>();

		for (Player player : playersPositions.keySet()) {
			if (isInTheMap(player) && !player.getPlayerName().equals(mainPlayer.getPlayerName()) && isVisible(mainPlayer, player))
				visiblePlayers.add(player);
		}
		return visiblePlayers;
	}

	/**
	 * Returns all visible coordinates.
	 *
	 * @param player player who is watching.
	 * @return all visible coordinates.
	 */
	public List<Coordinates> getVisibleCoordinates(Player player) {
		List<Integer> visibleRoomsIndexes = new ArrayList<>();
		Square squarePlayer = getSquare(playersPositions.get(player));

		visibleRoomsIndexes.add(getSquare(playersPositions.get(player)).getRoomID());

		for (Square adjacentSquare : squarePlayer.getAdjacentSquares()) {
			if (!visibleRoomsIndexes.contains(adjacentSquare.getRoomID())) {
				visibleRoomsIndexes.add(adjacentSquare.getRoomID());
			}
		}

		List<Coordinates> visibleCoordinates = new ArrayList<>();

		for (Integer roomID : visibleRoomsIndexes) {
			visibleCoordinates.addAll(rooms.get(roomID));
		}
		Utils.logInfo("GameMap -> getVisibleCoordinates(): Player con see");
		for (Coordinates squareCoordinates : visibleCoordinates) {
			System.out.print(" " + squareCoordinates);
		}
		System.out.print("\n");

		return visibleCoordinates;
	}

	/**
	 * Returns all the players that the specified player can reach within the specified distance.
	 *
	 * @param player   player who is moving.
	 * @param distance distance the player can move.
	 * @return all the players that the specified player can reach within the specified distance.
	 */
	public List<Player> reachablePlayers(Player player, int distance) {
		List<Player> reachablePlayers = new ArrayList<>();
		List<Coordinates> reachableCoordinates = reachableCoordinates(player, distance);
		if (reachableCoordinates.isEmpty())
			throw new IllegalStateException();
		for (Coordinates coordinates : reachableCoordinates) {
			reachablePlayers.addAll(getPlayersFromCoordinates(coordinates));
		}
		reachablePlayers.remove(player);
		return reachablePlayers;
	}

	/**
	 * Returns all the visible players that the specified player can reach within the specified distance.
	 *
	 * @param player   player who is moving.
	 * @param distance distance the player can move.
	 * @return all the visible players that the specified player can reach within the specified distance.
	 */
	public List<Player> reachableAndVisiblePlayers(Player player, int distance) {
		List<Player> reachablePlayers = reachablePlayers(player, distance);
		List<Player> reachableAndVisiblePlayers = new ArrayList<>();
		for (Player otherPlayer : reachablePlayers) {
			if (isVisible(player, otherPlayer))
				reachableAndVisiblePlayers.add(otherPlayer);
		}
		return reachableAndVisiblePlayers;
	}


	// ####################################
	// PRIVATE METHODS
	// ####################################

	/**
	 * Given the square returns its coordinates in the map.
	 *
	 * @param square square we want to know thw coordinates of
	 * @return the coordinates of the square
	 * @throws OutOfBoundariesException if the square does not belong to the map.
	 */
	Coordinates getCoordinates(Square square) {
		if (isIn(square))
			return square.getCoordinates();
		else
			throw new OutOfBoundariesException("square does not belong to the map");
	}

	/**
	 * Given the coordinates of the map returns the associated square.
	 *
	 * @param coordinates coordinates of the square we want
	 * @return the square in the specified coordinates
	 * @throws OutOfBoundariesException if the coordinates do not belong to the map
	 */
	Square getSquare(Coordinates coordinates) {
		if (isIn(coordinates))
			return map[coordinates.getRow()][coordinates.getColumn()];
		else
			throw new OutOfBoundariesException("the coordinates do not belong to the map " + coordinates);
	}

	/**
	 * Returns the set of all reachable squares from the coordinates and distance at most max distance.
	 *
	 * @param square      square of the starting point
	 * @param maxDistance maximum distance
	 * @return the set of all reachable squares from the coordinates and distance at most max distance
	 */
	private List<Coordinates> reachableSquares(Square square, int maxDistance, List<Coordinates> reachableCoordinates) {
		if (maxDistance != 0) {
			for (Square adjacentSquare : square.getAdjacentSquares()) {
				reachableSquares(adjacentSquare, maxDistance - 1, reachableCoordinates);
			}
		}

		if (!(reachableCoordinates.contains(getCoordinates(square))))
			reachableCoordinates.add(getCoordinates(square));
		return reachableCoordinates;
	}

	/**
	 * Returns true if and only if the coordinates belong to the map
	 *
	 * @param coordinates coordinates to check
	 * @return true if and only if the coordinates belong to the map
	 */
	private boolean isIn(Coordinates coordinates) {
		return !((coordinates.getRow() >= numOfRows) || (coordinates.getColumn() >= numOfColumns)) &&
				map[coordinates.getRow()][coordinates.getColumn()].getRoomID() != -1;
	}

	/**
	 * Returns true if and only if the square belong to the map
	 *
	 * @param square square to check
	 * @return true if and only if the square belong to the map
	 */
	private boolean isIn(Square square) {
		Coordinates coordinates = square.getCoordinates();
		return isIn(square.getCoordinates()) && map[coordinates.getRow()][coordinates.getColumn()].equals(square);
	}

	/**
	 * Adds a Square to the map according to the specified coordinates.
	 *
	 * @param square JsonObject to parse.
	 */
	private void addSquareToMap(JsonObject square, GameBoard gameBoard) throws IOException {

		Coordinates squareCoordinates = new Coordinates(square.get("row").getAsInt(), square.get("column").getAsInt());

		switch (square.get("type").getAsString()) {
			case "AmmoSquare":
				map[squareCoordinates.getRow()][squareCoordinates.getColumn()] = new AmmoSquare(square.get("roomID").getAsInt(), Color.CharacterColorType.valueOf(square.get("squareColor").getAsString()), getBooleanArray(square.getAsJsonArray("possibleDirection")), squareCoordinates, gameBoard);
				break;

			case "SpawnSquare":
				map[squareCoordinates.getRow()][squareCoordinates.getColumn()] = new SpawnSquare(square.get("roomID").getAsInt(), AmmoType.valueOf(square.get("ammoType").getAsString()), Color.CharacterColorType.valueOf(square.get("squareColor").getAsString()), getBooleanArray(square.getAsJsonArray("possibleDirection")), squareCoordinates, gameBoard);
				spawnSquaresCoordinates.add(squareCoordinates);
				break;

			case "VoidSquare":
				map[squareCoordinates.getRow()][squareCoordinates.getColumn()] = new VoidSquare(squareCoordinates);
				break;

			default:
				throw new IOException("failed to add the map");
		}
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
		Square square;
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				for (CardinalDirection direction : CardinalDirection.values()) {
					square = map[i][j];
					if (square.getRoomID() != -1 && square.getPossibleDirections()[direction.ordinal()]) {
						square.addAdjacentSquare(getSquare(Coordinates.getDirectionCoordinates(new Coordinates(i, j), direction)));
						Utils.logInfo("GameMap -> connectSquares(): Adding " + getSquare(Coordinates.getDirectionCoordinates(new Coordinates(i, j), direction)).getCoordinates() + " to adjacent squares of " + square.getCoordinates());
					}

				}
			}
		}
	}

	/**
	 * Receives the name of the map to generate and initialize the squares according to the file.
	 *
	 * @param mapName: name of the map to load
	 */
	private void generateMapJson(String mapName, GameBoard gameBoard) {
		Utils.logInfo("Loading map:" + mapName);
		Reader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/maps/" + mapName + ".json")));
		try {
			JsonParser parser = new JsonParser();
			JsonObject rootObject = parser.parse(reader).getAsJsonObject();
			numOfRows = rootObject.get("row").getAsInt();
			numOfColumns = rootObject.get("column").getAsInt();

			map = new Square[numOfRows][numOfColumns];

			for (int i = 0; i < Color.CharacterColorType.values().length; i++)
				rooms.add(new ArrayList<>());

			JsonArray squares = rootObject.getAsJsonArray("squares");
			for (JsonElement entry : squares) {
				addSquareToMap(entry.getAsJsonObject(), gameBoard);
			}
		} catch (IOException | JsonParseException e) {
			Utils.logError("Cannot parse " + mapName, e);
		}
	}

	private boolean[] getBooleanArray(JsonArray ja) {
		boolean[] result = new boolean[4];

		for (int i = 0; i < ja.size(); i++) {
			result[i] = ja.get(i).getAsBoolean();
		}

		return result;
	}

	// ####################################
	// REPRESENTATION METHODS
	// ####################################

	/**
	 * Returns the squares's representation in the specified coordinates.
	 *
	 * @param coordinates coordinates of the square.
	 * @return the squares's representation in the specified coordinates.
	 */
	SquareRep getSquareRep(Coordinates coordinates) {
		return (SquareRep) map[coordinates.getRow()][coordinates.getColumn()].getRep();
	}

	/**
	 * Updates the game map's representation.
	 */
	public void updateRep() {
		if (gameMapRep == null || hasChanged()) {
			gameMapRep = new GameMapRep(this);
			if (Utils.DEBUG_REPS)
				Utils.logInfo("GameMap -> updateRep(): The game map representation has been updated");
		} else if (Utils.DEBUG_REPS) {
			Utils.logInfo("GameMap -> updateRep(): The game map representation is already up to date");
		}
	}

	/**
	 * Forces the update of the game board.
	 */
	public void forceUpdateOfReps() {
		gameMapRep = new GameMapRep(this);
		setChanged();
		if (Utils.DEBUG_REPS)
			Utils.logInfo("GameMap -> forceUpdateOfReps(): The game map representation has been updated");
	}

	/**
	 * Returns the representation of the game map.
	 *
	 * @return the representation of the game map.
	 */
	@Override
	public Representation getRep() {
		return gameMapRep;
	}
}

