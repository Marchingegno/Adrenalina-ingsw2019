package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.ModelDriver;
import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.utils.exceptions.OutOfBoundariesException;
import it.polimi.se2019.utils.exceptions.PlayerNotInTheMapException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameMapTest {

	ModelDriver mediumModel;
	GameMap mediumMap;
	ModelDriver bigModel;
	GameMap bigMap;
	GameMap bigMapWithOutBLueSpawn;
	GameMap mediumMapToTestAdjacentRooms;
	private ArrayList<String> players;

	@Before
	public void setUp() {
		Utils.setLogEnabled(false);
		players = new ArrayList<>();
		players.add("Test 1");
		players.add("Test 2");
		players.add("Test 3");
		players.add("Test 4");
		players.add("Test 5");
		mediumModel = new ModelDriver(GameConstants.MapType.MEDIUM_MAP.getMapName(), players, 8);
		mediumMap = mediumModel.getGameBoard().getGameMap();
		bigModel = new ModelDriver(GameConstants.MapType.BIG_MAP.getMapName(), players, 8);
		bigMap = bigModel.getGameBoard().getGameMap();
		bigMapWithOutBLueSpawn = new GameMap("BigMapWithOutBLueSpawn", new ModelDriver(GameConstants.MapType.MEDIUM_MAP.getMapName(), players, 8).getGameBoard());
		mediumMapToTestAdjacentRooms = new GameMap("MediumMapToTestAdjacentRooms", new ModelDriver(GameConstants.MapType.MEDIUM_MAP.getMapName(), players, 8).getGameBoard());
	}

	@Test
	public void getNamOfRows_mediumMap_correctOutput() {
		assertEquals(3, mediumMap.getNumOfRows());
	}

	@Test
	public void getNumOfColumn_mediumMap_correctOutput() {
		assertEquals(4, mediumMap.getNumOfColumns());
	}

	@Test
	public void getAllCoordinates_mediumMap_correctOutput() {
		List<Coordinates> correctCoordinates = new ArrayList<>();
		correctCoordinates.add(new Coordinates(0, 0));
		correctCoordinates.add(new Coordinates(0, 1));
		correctCoordinates.add(new Coordinates(0, 2));
		correctCoordinates.add(new Coordinates(0, 3));
		correctCoordinates.add(new Coordinates(1, 0));
		correctCoordinates.add(new Coordinates(1, 1));
		correctCoordinates.add(new Coordinates(1, 2));
		correctCoordinates.add(new Coordinates(1, 3));
		correctCoordinates.add(new Coordinates(2, 1));
		correctCoordinates.add(new Coordinates(2, 2));
		correctCoordinates.add(new Coordinates(2, 3));
		assertEquals(correctCoordinates, mediumMap.getAllCoordinates());
	}

	@Test
	public void getAllCoordinatesExceptPlayer_mediumMap_correctOutput() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(1, 1));
		List<Coordinates> correctCoordinates = new ArrayList<>();
		correctCoordinates.add(new Coordinates(0, 0));
		correctCoordinates.add(new Coordinates(0, 1));
		correctCoordinates.add(new Coordinates(0, 2));
		correctCoordinates.add(new Coordinates(0, 3));
		correctCoordinates.add(new Coordinates(1, 0));
		correctCoordinates.add(new Coordinates(1, 2));
		correctCoordinates.add(new Coordinates(1, 3));
		correctCoordinates.add(new Coordinates(2, 1));
		correctCoordinates.add(new Coordinates(2, 2));
		correctCoordinates.add(new Coordinates(2, 3));
		assertEquals(correctCoordinates, mediumMap.getAllCoordinatesExceptPlayer(player));
	}

	@Test
	public void grabCard_mediumMap_correctOutput() {
		List<Coordinates> allCoordinates = mediumMap.getAllCoordinates();
		for (Coordinates coordinates : allCoordinates) {
			assertEquals(mediumMap.getSquare(coordinates).getCards().get(0), mediumMap.grabCard(coordinates, 0));
			assertFalse(mediumMap.getSquare(coordinates).isFilled());
		}
	}

	@Test
	public void addCard_mediumMap_correctOutput() {
		Coordinates ammoCoordinates = new Coordinates(0, 0);
		Coordinates spawnCoordinates = new Coordinates(0, 2);

		mediumMap.grabCard(ammoCoordinates, 0);
		Card ammoCard = mediumModel.getGameBoard().getAmmoDeck().drawCard();
		mediumMap.addCard(ammoCoordinates, ammoCard);
		assertEquals(mediumMap.getSquare(ammoCoordinates).getCards().get(0), ammoCard);

		mediumMap.grabCard(spawnCoordinates, 1);
		Card weaponCard = mediumModel.getGameBoard().getWeaponDeck().drawCard();
		mediumMap.addCard(spawnCoordinates, weaponCard);
		assertTrue(mediumMap.getSquare(spawnCoordinates).getCards().contains(weaponCard));
	}

	@Test
	public void refillMap_mediumMap_correctOutput() {
		List<Coordinates> allCoordinates = mediumMap.getAllCoordinates();
		for (Coordinates coordinates : allCoordinates) {
			mediumMap.grabCard(coordinates, 0);
		}
		mediumMap.refillMap();
		for (Coordinates coordinates : allCoordinates) {
			assertTrue(mediumMap.getSquare(coordinates).isFilled());
		}
	}

	@Test
	public void getDoors_mediumMap_correctOutput() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(0, 2));
		List<Coordinates> correctCoordinates = new ArrayList<>();
		correctCoordinates.add(new Coordinates(0, 3));
		correctCoordinates.add(new Coordinates(1, 2));
		assertEquals(correctCoordinates, mediumMap.getDoors(player));

		mediumMap.movePlayerTo(player, new Coordinates(2, 3));
		assertTrue(mediumMap.getDoors(player).isEmpty());
	}


	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_mediumMapVoidCoordinates_throwsOutOfBoundariesException() {
		mediumMap.getRoomCoordinates(new Coordinates(2, 0));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_mediumMapSquareOutOfTheMap1_throwsOutOfBoundariesException() {
		mediumMap.getRoomCoordinates(new AmmoSquare(1, Color.CharacterColorType.RED, new boolean[4], new Coordinates(5, 5), mediumModel.getGameBoard()));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_mediumMapSquareOutOfTheMap2_throwsOutOfBoundariesException() {
		mediumMap.getRoomCoordinates(new AmmoSquare(1, Color.CharacterColorType.RED, new boolean[4], new Coordinates(5, 1), mediumModel.getGameBoard()));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_mediumMapSquareOutOfTheMap3_throwsOutOfBoundariesException() {
		mediumMap.getRoomCoordinates(new AmmoSquare(1, Color.CharacterColorType.RED, new boolean[4], new Coordinates(1, 5), mediumModel.getGameBoard()));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_mediumMapSquareNotInTheMap_throwsOutOfBoundariesException() {
		mediumMap.getRoomCoordinates(new AmmoSquare(1, Color.CharacterColorType.RED, new boolean[4], new Coordinates(1, 0), mediumModel.getGameBoard()));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_mediumMapSquareVoidSquare_throwsOutOfBoundariesException() {
		mediumMap.getRoomCoordinates(new VoidSquare(new Coordinates(1, 1)));
	}

	@Test
	public void getRoomCoordinates_mediumMapSquare_correctOutput() {
		List<Coordinates> answer = mediumMap.getRoomCoordinates(mediumMap.getSquare(new Coordinates(0, 0)));
		assertEquals(mediumMap.getRoomCoordinates(new Coordinates(0, 0)), answer);
	}

	@Test
	public void getRoomCoordinates_BigMapSquare_correctOutput() {
		GameMap map = bigMap;

		//--------------------------------------------FIRST ROOM---------------------------------
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(1, 0));
		correctOutput.add(new Coordinates(0, 0));
		ArrayList<Coordinates> methodOutput = new ArrayList<>(map.getRoomCoordinates(map.getSquare(new Coordinates(0, 0))));

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);

		//--------------------------------------------SECOND ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(0, 2));
		correctOutput.add(new Coordinates(0, 1));
		methodOutput = new ArrayList<>(map.getRoomCoordinates(map.getSquare(new Coordinates(0, 2))));

		assertEquals(correctOutput.size(), methodOutput.size());

		valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);

		//--------------------------------------------THIRD ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(0, 3));
		methodOutput = new ArrayList<>(map.getRoomCoordinates(map.getSquare(new Coordinates(0, 3))));

		assertEquals(correctOutput, methodOutput);

		//--------------------------------------------FOURTH ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(1, 1));
		methodOutput = new ArrayList<>(map.getRoomCoordinates(map.getSquare(new Coordinates(1, 1))));

		assertEquals(correctOutput, methodOutput);

		//--------------------------------------------FIFTH ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(2, 2));
		correctOutput.add(new Coordinates(2, 3));
		correctOutput.add(new Coordinates(1, 2));
		correctOutput.add(new Coordinates(1, 3));
		methodOutput = new ArrayList<>(map.getRoomCoordinates(map.getSquare(new Coordinates(1, 3))));

		assertEquals(correctOutput.size(), methodOutput.size());

		valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);

		//--------------------------------------------SIXTH ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(2, 0));
		correctOutput.add(new Coordinates(2, 1));
		methodOutput = new ArrayList<>(map.getRoomCoordinates(map.getSquare(new Coordinates(2, 0))));

		assertEquals(correctOutput.size(), methodOutput.size());

		valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void getSpawnCoordinates_mediumMap_correctOutput() {
		List<Coordinates> correctCoordinates = new ArrayList<>();
		correctCoordinates.add(new Coordinates(1, 2));
		correctCoordinates.add(new Coordinates(1, 3));
		correctCoordinates.add(new Coordinates(2, 2));
		correctCoordinates.add(new Coordinates(2, 3));
		assertEquals(correctCoordinates, mediumMap.getRoomCoordinates(new Coordinates(1, 2)));
	}

	@Test
	public void getSpawnSquare_bigMapAllAmmoTypes_allSpawnCoordinates() {
		assertEquals(new Coordinates(2, 3), bigMap.getSpawnCoordinates(AmmoType.YELLOW_AMMO));
		assertEquals(new Coordinates(1, 0), bigMap.getSpawnCoordinates(AmmoType.RED_AMMO));
		assertEquals(new Coordinates(0, 2), bigMap.getSpawnCoordinates(AmmoType.BLUE_AMMO));
	}

	@Test
	public void getSpawnSquare_bigMapWithoutBlueSpawn_shouldReturnNull() {
		assertNull(bigMapWithOutBLueSpawn.getSpawnCoordinates(AmmoType.BLUE_AMMO));
	}

	@Test
	public void isSpawnSquare_mediumMap_correctOutput() {
		assertTrue(mediumMap.isSpawnSquare(new Coordinates(0, 2)));
		assertTrue(mediumMap.isSpawnSquare(new Coordinates(1, 0)));
		assertTrue(mediumMap.isSpawnSquare(new Coordinates(2, 3)));
		assertFalse(mediumMap.isSpawnSquare(new Coordinates(2, 2)));
		assertFalse(mediumMap.isSpawnSquare(new Coordinates(8, 6)));
	}

	@Test
	public void getCoordinatesFromDirection_mediumMap_correctOutput() {
		assertEquals(new Coordinates(0, 2), mediumMap.getCoordinatesFromDirection(new Coordinates(1, 2), CardinalDirection.UP));
		assertEquals(new Coordinates(1, 3), mediumMap.getCoordinatesFromDirection(new Coordinates(1, 2), CardinalDirection.RIGHT));
		assertEquals(new Coordinates(2, 2), mediumMap.getCoordinatesFromDirection(new Coordinates(1, 2), CardinalDirection.DOWN));
		assertEquals(new Coordinates(1, 2), mediumMap.getCoordinatesFromDirection(new Coordinates(1, 3), CardinalDirection.LEFT));
		assertNull(mediumMap.getCoordinatesFromDirection(new Coordinates(1, 1), CardinalDirection.UP));
		assertNull(mediumMap.getCoordinatesFromDirection(new Coordinates(1, 1), CardinalDirection.RIGHT));
		assertNull(mediumMap.getCoordinatesFromDirection(new Coordinates(2, 1), CardinalDirection.DOWN));
		assertNull(mediumMap.getCoordinatesFromDirection(new Coordinates(2, 1), CardinalDirection.LEFT));
	}

	@Test
	public void getAdjacentRoomsCoordinates_mediumMap_correctOutput() {
		List<Coordinates> answer = mediumMapToTestAdjacentRooms.getAdjacentRoomsCoordinates(new Coordinates(1, 2));
		List<Coordinates> correctCoordinates = new ArrayList<>();
		correctCoordinates.add(new Coordinates(0, 0));
		correctCoordinates.add(new Coordinates(0, 1));
		correctCoordinates.add(new Coordinates(0, 2));
		correctCoordinates.add(new Coordinates(1, 1));
		correctCoordinates.add(new Coordinates(2, 1));
		correctCoordinates.add(new Coordinates(2, 2));

		for (Coordinates coordinates : correctCoordinates) {
			assertTrue(answer.contains(coordinates));
		}

		assertEquals(6, answer.size());

		assertTrue(mediumMap.getAdjacentRoomsCoordinates(new Coordinates(2, 3)).isEmpty());
	}


	@Test(expected = PlayerNotInTheMapException.class)
	public void getPlayersCoordinates_playerNotYetPlaced_throwsPlayerNotInTheMapException() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.getPlayerCoordinates(player);
	}

	@Test
	public void getPlayersCoordinates_playerMoved_correctOutput() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(2, 3));
		assertEquals(new Coordinates(2, 3), mediumMap.getPlayerCoordinates(player));
	}

	@Test(expected = PlayerNotInTheMapException.class)
	public void playerSquare_playerNotYetPlaced_throwsPlayerNotInTheMapException() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.getPlayerSquare(player);
	}

	@Test
	public void playerSquare_playerMoved_correctOutput() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(2, 3));
		assertEquals(mediumMap.getSquare(new Coordinates(2, 3)), mediumMap.getPlayerSquare(player));
	}

	@Test
	public void getPlayersFromCoordinates_mediumMap_correctOutput() {
		List<Player> answer;

		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		Player player2 = mediumModel.getGameBoard().getPlayers().get(1);
		Player player3 = mediumModel.getGameBoard().getPlayers().get(2);
		Player player4 = mediumModel.getGameBoard().getPlayers().get(3);
		Player player5 = mediumModel.getGameBoard().getPlayers().get(4);
		mediumMap.movePlayerTo(player1, new Coordinates(2, 3));
		mediumMap.movePlayerTo(player2, new Coordinates(2, 3));
		mediumMap.movePlayerTo(player3, new Coordinates(0, 0));
		mediumMap.movePlayerTo(player4, new Coordinates(0, 1));
		mediumMap.movePlayerTo(player5, new Coordinates(0, 3));

		answer = mediumMap.getPlayersFromCoordinates(new Coordinates(2, 3));
		assertTrue(answer.contains(player1));
		assertTrue(answer.contains(player2));

		answer = mediumMap.getPlayersFromCoordinates(new Coordinates(0, 0));
		assertTrue(answer.contains(player3));

		answer = mediumMap.getPlayersFromCoordinates(new Coordinates(2, 1));
		assertTrue(answer.isEmpty());

	}

	@Test
	public void movePlayerTo_correctInput_correctOutput() {
		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		Player player2 = mediumModel.getGameBoard().getPlayers().get(1);
		Player player3 = mediumModel.getGameBoard().getPlayers().get(2);
		Player player4 = mediumModel.getGameBoard().getPlayers().get(3);
		Player player5 = mediumModel.getGameBoard().getPlayers().get(4);
		mediumMap.movePlayerTo(player1, new Coordinates(1, 1));
		mediumMap.movePlayerTo(player2, new Coordinates(0, 0));
		mediumMap.movePlayerTo(player3, new Coordinates(2, 2));
		mediumMap.movePlayerTo(player4, new Coordinates(2, 3));
		mediumMap.movePlayerTo(player5, new Coordinates(2, 1));
		mediumMap.movePlayerTo(player3, new Coordinates(1, 1));
		assertEquals(new Coordinates(1, 1), mediumMap.getPlayerCoordinates(player1));
		assertEquals(new Coordinates(0, 0), mediumMap.getPlayerCoordinates(player2));
		assertEquals(new Coordinates(1, 1), mediumMap.getPlayerCoordinates(player3));
		assertEquals(new Coordinates(2, 3), mediumMap.getPlayerCoordinates(player4));
		assertEquals(new Coordinates(2, 1), mediumMap.getPlayerCoordinates(player5));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void movePlayerTo_playerOutOfColumn_throwsOutOfBoundaryException() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(1, 5));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void movePlayerTo_playerOutOfRow_throwsOutOfBoundaryException() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(9, 1));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void movePlayerTo_playerOutOfMap_throwsOutOfBoundaryException() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(2, 0));
	}

	@Test
	public void reachableCoordinates_zeroDistance_correctOutput() {
		ModelDriver model = new ModelDriver("MediumMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		ArrayList<Coordinates> correctReachableSquares = new ArrayList<>();
		correctReachableSquares.add(new Coordinates(1, 1));
		assertEquals(correctReachableSquares, map.reachableCoordinates(new Coordinates(1, 1), 0));
	}

	@Test
	public void getPlayersInDirection_moveUntilMapsEndsLeft_correctOutput() {
		List<Player> correctAnswer = new ArrayList<>();
		List<Player> answer;
		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		Player player2 = mediumModel.getGameBoard().getPlayers().get(1);
		Player player3 = mediumModel.getGameBoard().getPlayers().get(2);
		Player player4 = mediumModel.getGameBoard().getPlayers().get(3);
		Player player5 = mediumModel.getGameBoard().getPlayers().get(4);
		mediumMap.movePlayerTo(player1, new Coordinates(1, 0));
		mediumMap.movePlayerTo(player2, new Coordinates(1, 0));
		mediumMap.movePlayerTo(player3, new Coordinates(1, 2));
		mediumMap.movePlayerTo(player4, new Coordinates(1, 3));
		mediumMap.movePlayerTo(player5, new Coordinates(2, 2));

		correctAnswer.add(player1);
		correctAnswer.add(player2);
		correctAnswer.add(player3);
		answer = mediumMap.getPlayersInDirection(new Coordinates(1, 2), CardinalDirection.LEFT);
		for (Player player : correctAnswer) {
			assertTrue(answer.contains(player));
		}
	}

	@Test
	public void getPlayersInDirection_moveUntilMapsEndsUp_correctOutput() {
		List<Player> correctAnswer = new ArrayList<>();
		List<Player> answer;
		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		Player player2 = mediumModel.getGameBoard().getPlayers().get(1);
		Player player3 = mediumModel.getGameBoard().getPlayers().get(2);
		Player player4 = mediumModel.getGameBoard().getPlayers().get(3);
		Player player5 = mediumModel.getGameBoard().getPlayers().get(4);
		mediumMap.movePlayerTo(player1, new Coordinates(1, 0));
		mediumMap.movePlayerTo(player2, new Coordinates(1, 0));
		mediumMap.movePlayerTo(player3, new Coordinates(1, 2));
		mediumMap.movePlayerTo(player4, new Coordinates(1, 3));
		mediumMap.movePlayerTo(player5, new Coordinates(2, 2));

		correctAnswer.add(player4);
		answer = mediumMap.getPlayersInDirection(new Coordinates(2, 3), CardinalDirection.UP);
		assertEquals(correctAnswer.size(), answer.size());
		for (Player player : correctAnswer) {
			assertTrue(answer.contains(player));
		}
	}

	@Test
	public void getPlayersInDirection_moveUntilMapsEndsRight_correctOutput() {
		List<Player> correctAnswer = new ArrayList<>();
		List<Player> answer;
		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		Player player2 = mediumModel.getGameBoard().getPlayers().get(1);
		Player player3 = mediumModel.getGameBoard().getPlayers().get(2);
		Player player4 = mediumModel.getGameBoard().getPlayers().get(3);
		Player player5 = mediumModel.getGameBoard().getPlayers().get(4);
		mediumMap.movePlayerTo(player1, new Coordinates(1, 0));
		mediumMap.movePlayerTo(player2, new Coordinates(1, 0));
		mediumMap.movePlayerTo(player3, new Coordinates(1, 2));
		mediumMap.movePlayerTo(player4, new Coordinates(1, 3));
		mediumMap.movePlayerTo(player5, new Coordinates(2, 2));

		correctAnswer.add(player1);
		correctAnswer.add(player2);
		correctAnswer.add(player3);
		correctAnswer.add(player4);
		answer = mediumMap.getPlayersInDirection(new Coordinates(1, 0), CardinalDirection.RIGHT);
		assertEquals(correctAnswer.size(), answer.size());
		for (Player player : correctAnswer) {
			assertTrue(answer.contains(player));
		}
	}

	@Test
	public void getPlayersInDirection_moveUntilMapsEndsdown_correctOutput() {
		List<Player> correctAnswer = new ArrayList<>();
		List<Player> answer;
		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		Player player2 = mediumModel.getGameBoard().getPlayers().get(1);
		Player player3 = mediumModel.getGameBoard().getPlayers().get(2);
		Player player4 = mediumModel.getGameBoard().getPlayers().get(3);
		Player player5 = mediumModel.getGameBoard().getPlayers().get(4);
		mediumMap.movePlayerTo(player1, new Coordinates(1, 0));
		mediumMap.movePlayerTo(player2, new Coordinates(1, 0));
		mediumMap.movePlayerTo(player3, new Coordinates(1, 2));
		mediumMap.movePlayerTo(player4, new Coordinates(1, 3));
		mediumMap.movePlayerTo(player5, new Coordinates(2, 2));

		correctAnswer.add(player1);
		correctAnswer.add(player2);
		answer = mediumMap.getPlayersInDirection(new Coordinates(0, 0), CardinalDirection.DOWN);
		assertEquals(correctAnswer.size(), answer.size());
		for (Player player : correctAnswer) {
			assertTrue(answer.contains(player));
		}
	}

	@Test
	public void reachableCoordinates_oneDistance_correctOutput() {
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(1, 0));
		correctOutput.add(new Coordinates(0, 0));
		correctOutput.add(new Coordinates(1, 1));
		ArrayList<Coordinates> methodOutput = new ArrayList<>(mediumMap.reachableCoordinates(new Coordinates(1, 0), 1));
		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void reachableCoordinates_maxDistance1_correctOutput() {
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(2, 3));
		correctOutput.add(new Coordinates(2, 2));
		correctOutput.add(new Coordinates(2, 1));
		correctOutput.add(new Coordinates(2, 0));
		correctOutput.add(new Coordinates(0, 1));
		correctOutput.add(new Coordinates(0, 2));
		correctOutput.add(new Coordinates(0, 3));
		correctOutput.add(new Coordinates(0, 0));
		correctOutput.add(new Coordinates(1, 3));
		correctOutput.add(new Coordinates(1, 0));
		correctOutput.add(new Coordinates(1, 2));
		correctOutput.add(new Coordinates(1, 1));
		ArrayList<Coordinates> methodOutput = new ArrayList<>(bigMap.reachableCoordinates(new Coordinates(1, 2), 4));

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void reachableCoordinates_maxDistance2_correctOutput() {
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(2, 3));
		correctOutput.add(new Coordinates(2, 2));
		correctOutput.add(new Coordinates(2, 1));
		correctOutput.add(new Coordinates(2, 0));
		correctOutput.add(new Coordinates(0, 1));
		correctOutput.add(new Coordinates(0, 2));
		correctOutput.add(new Coordinates(0, 3));
		correctOutput.add(new Coordinates(0, 0));
		correctOutput.add(new Coordinates(1, 3));
		correctOutput.add(new Coordinates(1, 0));
		correctOutput.add(new Coordinates(1, 2));
		correctOutput.add(new Coordinates(1, 1));
		ArrayList<Coordinates> methodOutput = new ArrayList<>(bigMap.reachableCoordinates(new Coordinates(1, 2), 5));

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void reachableCoordinates_overMaxDistance_correctOutput() {
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(2, 3));
		correctOutput.add(new Coordinates(2, 2));
		correctOutput.add(new Coordinates(2, 1));
		correctOutput.add(new Coordinates(0, 1));
		correctOutput.add(new Coordinates(0, 2));
		correctOutput.add(new Coordinates(0, 3));
		correctOutput.add(new Coordinates(0, 0));
		correctOutput.add(new Coordinates(1, 3));
		correctOutput.add(new Coordinates(1, 0));
		correctOutput.add(new Coordinates(1, 2));
		correctOutput.add(new Coordinates(1, 1));
		ArrayList<Coordinates> methodOutput = new ArrayList<>(mediumMap.reachableCoordinates(new Coordinates(1, 1), 9));

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void reachableCoordinates_maxDistance1Player_correctOutput() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(1, 0));
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(1, 0));
		correctOutput.add(new Coordinates(0, 0));
		correctOutput.add(new Coordinates(1, 1));
		ArrayList<Coordinates> methodOutput = new ArrayList<>(mediumMap.reachableCoordinates(player, 1));
		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void reachablePerpendicularCoordinates_distance0_correctOutput() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(0, 2));
		List<Coordinates> answer = mediumMap.reachablePerpendicularCoordinates(player, 0);
		assertEquals(0, answer.size());
	}

	@Test
	public void reachablePerpendicularCoordinates_distance1_correctOutput() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(0, 2));
		ArrayList<Coordinates> correctAnswer = new ArrayList<>();
		correctAnswer.add(new Coordinates(0, 1));
		correctAnswer.add(new Coordinates(1, 2));
		correctAnswer.add(new Coordinates(0, 3));
		List<Coordinates> answer = mediumMap.reachablePerpendicularCoordinates(player, 1);
		assertEquals(correctAnswer.size(), answer.size());
		for (Coordinates coordinates : correctAnswer) {
			assertTrue(answer.contains(coordinates));
		}
	}

	@Test
	public void reachablePerpendicularCoordinates_distanceMax_correctOutput() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(0, 2));
		ArrayList<Coordinates> correctAnswer = new ArrayList<>();
		correctAnswer.add(new Coordinates(0, 0));
		correctAnswer.add(new Coordinates(0, 1));
		correctAnswer.add(new Coordinates(2, 2));
		correctAnswer.add(new Coordinates(1, 2));
		correctAnswer.add(new Coordinates(0, 3));
		List<Coordinates> answer = mediumMap.reachablePerpendicularCoordinates(player, 5);
		assertEquals(correctAnswer.size(), answer.size());
		for (Coordinates coordinates : correctAnswer) {
			assertTrue(answer.contains(coordinates));
		}
	}

	@Test
	public void getCoordinatesWhereCurrentPlayerCanGrab_distanceMax_correctOutput() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(0, 2));
		mediumMap.grabCard(new Coordinates(0, 2), 0);
		mediumMap.grabCard(new Coordinates(0, 1), 0);

		ArrayList<Coordinates> correctAnswer = new ArrayList<>();
		correctAnswer.add(new Coordinates(0, 0));
		correctAnswer.add(new Coordinates(0, 2));
		correctAnswer.add(new Coordinates(0, 3));
		correctAnswer.add(new Coordinates(1, 2));
		correctAnswer.add(new Coordinates(1, 3));
		correctAnswer.add(new Coordinates(2, 2));
		List<Coordinates> answer = mediumMap.getCoordinatesWhereCurrentPlayerCanGrab(player, 2);
		assertEquals(correctAnswer.size(), answer.size());
		for (Coordinates coordinates : correctAnswer) {
			assertTrue(answer.contains(coordinates));
		}
	}

	@Test
	public void getCoordinatesWhereCurrentPlayerCanGrab_noGrabbableCards_shouldBeEmpty() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player, new Coordinates(2, 1));
		mediumMap.grabCard(new Coordinates(1, 1), 0);
		mediumMap.grabCard(new Coordinates(2, 1), 0);
		mediumMap.grabCard(new Coordinates(2, 2), 0);
		List<Coordinates> answer = mediumMap.getCoordinatesWhereCurrentPlayerCanGrab(player, 1);
		assertTrue(answer.isEmpty());
	}

	@Test
	public void visible_playersInTheSameSquare_correctOutput() {
		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		Player player2 = mediumModel.getGameBoard().getPlayers().get(1);
		mediumMap.movePlayerTo(player1, new Coordinates(1, 1));
		mediumMap.movePlayerTo(player2, new Coordinates(1, 1));
		assertTrue(mediumMap.isVisible(player1, player2));
	}

	@Test
	public void visible_playersNotVisible_correctOutput() {
		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		Player player2 = mediumModel.getGameBoard().getPlayers().get(1);
		mediumMap.movePlayerTo(player1, new Coordinates(0, 0));
		mediumMap.movePlayerTo(player2, new Coordinates(2, 3));
		assertFalse(mediumMap.isVisible(player1, player2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void visible_samePlayer_expectIllegalArgumentException() {
		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		mediumMap.movePlayerTo(player1, new Coordinates(0, 0));
		mediumMap.isVisible(player1, player1);
	}

	@Test
	public void visible_playersOnTheEdgeOfTheRoom_correctOutput() {
		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		Player player2 = mediumModel.getGameBoard().getPlayers().get(1);
		Player player3 = mediumModel.getGameBoard().getPlayers().get(2);
		Player player4 = mediumModel.getGameBoard().getPlayers().get(3);
		Player player5 = mediumModel.getGameBoard().getPlayers().get(4);

		mediumMap.movePlayerTo(player1, new Coordinates(1, 2));
		mediumMap.movePlayerTo(player2, new Coordinates(1, 3));
		mediumMap.movePlayerTo(player3, new Coordinates(0, 0));
		mediumMap.movePlayerTo(player4, new Coordinates(2, 2));
		mediumMap.movePlayerTo(player5, new Coordinates(1, 1));
		assertTrue(mediumMap.isVisible(player1, player2));
		assertTrue(mediumMap.isVisible(player1, player3));
		assertTrue(mediumMap.isVisible(player1, player4));
		assertFalse(mediumMap.isVisible(player1, player5));
	}

	@Test
	public void getVisiblePlayers_mediumMap_correctOutput() {
		List<Player> correctAnswer = new ArrayList<>();
		List<Player> answer;
		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		Player player2 = mediumModel.getGameBoard().getPlayers().get(1);
		Player player3 = mediumModel.getGameBoard().getPlayers().get(2);
		Player player4 = mediumModel.getGameBoard().getPlayers().get(3);
		Player player5 = mediumModel.getGameBoard().getPlayers().get(4);

		mediumMap.movePlayerTo(player1, new Coordinates(1, 2));
		mediumMap.movePlayerTo(player2, new Coordinates(1, 3));
		mediumMap.movePlayerTo(player3, new Coordinates(0, 0));
		mediumMap.movePlayerTo(player4, new Coordinates(2, 2));
		mediumMap.movePlayerTo(player5, new Coordinates(1, 1));

		correctAnswer.add(player3);
		correctAnswer.add(player2);
		correctAnswer.add(player4);
		answer = mediumMap.getVisiblePlayers(player1);
		assertEquals(correctAnswer.size(), answer.size());
		for (Player player : correctAnswer) {
			assertTrue(answer.contains(player));
		}
	}

	@Test
	public void getVisibleCoordinates_mediumMap_correctOutput() {
		Player player = mediumModel.getGameBoard().getPlayers().get(0);
		List<Coordinates> correctAnswer = new ArrayList<>();
		mediumMap.movePlayerTo(player, new Coordinates(1, 2));

		List<Coordinates> answer = mediumMap.getVisibleCoordinates(player);

		correctAnswer.add(new Coordinates(0, 0));
		correctAnswer.add(new Coordinates(0, 1));
		correctAnswer.add(new Coordinates(0, 2));
		correctAnswer.add(new Coordinates(1, 2));
		correctAnswer.add(new Coordinates(1, 3));
		correctAnswer.add(new Coordinates(2, 2));
		correctAnswer.add(new Coordinates(2, 3));

		assertEquals(correctAnswer.size(), answer.size());
		for (Coordinates coordinates : correctAnswer) {
			assertTrue(answer.contains(coordinates));
		}
	}

	@Test
	public void reachablePlayers_mediumMap_correctOutput() {
		List<Player> correctAnswer = new ArrayList<>();
		List<Player> answer;
		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		Player player2 = mediumModel.getGameBoard().getPlayers().get(1);
		Player player3 = mediumModel.getGameBoard().getPlayers().get(2);
		Player player4 = mediumModel.getGameBoard().getPlayers().get(3);
		Player player5 = mediumModel.getGameBoard().getPlayers().get(4);

		mediumMap.movePlayerTo(player1, new Coordinates(1, 2));
		mediumMap.movePlayerTo(player2, new Coordinates(1, 3));
		mediumMap.movePlayerTo(player3, new Coordinates(1, 0));
		mediumMap.movePlayerTo(player4, new Coordinates(2, 2));
		mediumMap.movePlayerTo(player5, new Coordinates(1, 1));

		correctAnswer.add(player2);
		correctAnswer.add(player4);
		correctAnswer.add(player5);
		answer = mediumMap.reachablePlayers(player1, 3);
		assertEquals(correctAnswer.size(), answer.size());
		for (Player player : correctAnswer) {
			assertTrue(answer.contains(player));
		}
	}
	
	@Test
	public void reachableAndVisiblePlayers_mediumMap_correctOutput() {
		List<Player> correctAnswer = new ArrayList<>();
		List<Player> answer;
		Player player1 = mediumModel.getGameBoard().getPlayers().get(0);
		Player player2 = mediumModel.getGameBoard().getPlayers().get(1);
		Player player3 = mediumModel.getGameBoard().getPlayers().get(2);
		Player player4 = mediumModel.getGameBoard().getPlayers().get(3);
		Player player5 = mediumModel.getGameBoard().getPlayers().get(4);

		mediumMap.movePlayerTo(player1, new Coordinates(1, 2));
		mediumMap.movePlayerTo(player2, new Coordinates(1, 3));
		mediumMap.movePlayerTo(player3, new Coordinates(1, 0));
		mediumMap.movePlayerTo(player4, new Coordinates(2, 2));
		mediumMap.movePlayerTo(player5, new Coordinates(1, 1));

		correctAnswer.add(player2);
		correctAnswer.add(player4);
		answer = mediumMap.reachableAndVisiblePlayers(player1, 3);
		assertEquals(correctAnswer.size(), answer.size());
		for (Player player : correctAnswer) {
			assertTrue(answer.contains(player));
		}
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getSquare_mediumMap_() {
		mediumMap.getSquare(new Coordinates(2, 0));
	}

	@Test
	public void updateRep_correctInput_correctOutput() {
		mediumMap.updateRep();
		assertNotNull(mediumMap.getRep());
		mediumMap.grabCard(new Coordinates(1, 1), 0);
		mediumMap.updateRep();
		mediumMap.notifyObservers();
		mediumMap.updateRep();
	}
}