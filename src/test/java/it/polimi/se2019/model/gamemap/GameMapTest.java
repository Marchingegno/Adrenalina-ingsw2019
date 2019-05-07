package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameMapTest {

	private ArrayList<String> players;

	@Before
	public void setUp() throws Exception {
		Utils.setDebug(false);
		players = new ArrayList<>();
		players.add("Test 1");
		players.add("Test 2");
		players.add("Test 3");
		players.add("Test 4");
		players.add("Test 5");
	}

	@After
	public void tearDown() {
	}

	@Test
	public void getNumOfRows_correctInput_correcntOutput() {
		Model model = new Model("MediumMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		assertEquals(3, map.getNumOfRows());
	}

	@Test
	public void getNumOfColumn_correctInput_correcntOutput() {
		Model model = new Model("MediumMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		assertEquals(4, map.getNumOfColumns());
	}

	@Test(expected = PlayerNotInTheMapException.class)
	public void playerCoordinates_playerNotYetPlaced1_throwsPlayerNotInTheMapException() {
		Model model = new Model("MediumMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		map.getPlayerCoordinates(gameBoard.getPlayers().get(0));
	}

	@Test(expected = PlayerNotInTheMapException.class)
	public void playerCoordinates_playerNotYetPlaced4_throwsPlayerNotInTheMapException() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		map.getPlayerCoordinates(gameBoard.getPlayers().get(3));
	}

	@Test
	public void playerCoordinates_playerMoved_correctOutput() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		map.movePlayerTo(gameBoard.getPlayers().get(0), new Coordinates(2, 3));
		assertEquals(new Coordinates(2, 3), map.getPlayerCoordinates(gameBoard.getPlayers().get(0)));
	}

	@Test(expected = PlayerNotInTheMapException.class)
	public void playerSquare_playerNotYetPlaced1_throwsPlayerNotInTheMapException() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		map.getPlayerSquare(gameBoard.getPlayers().get(0));
	}

	@Test(expected = PlayerNotInTheMapException.class)
	public void playerSquare_playerNotYetPlaced4_throwsPlayerNotInTheMapException() {
		Model model = new Model("MediumMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		map.getPlayerCoordinates(gameBoard.getPlayers().get(3));
	}

	@Test
	public void playerSquare_playerMoved_correctOutput() {
		Model model = new Model("MediumMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		map.movePlayerTo(gameBoard.getPlayers().get(0), new Coordinates(2, 3));
		assertEquals(map.getSquare(new Coordinates(2, 3)), map.getPlayerSquare(gameBoard.getPlayers().get(0)));
	}

	@Test
	public void movePlayerTo_correctInput_correctOutput() {
		Model model = new Model("MediumMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		map.movePlayerTo(gameBoard.getPlayers().get(0), new Coordinates(1, 1));
		map.movePlayerTo(gameBoard.getPlayers().get(1), new Coordinates(0, 0));
		map.movePlayerTo(gameBoard.getPlayers().get(2), new Coordinates(2, 2));
		map.movePlayerTo(gameBoard.getPlayers().get(3), new Coordinates(2, 3));
		map.movePlayerTo(gameBoard.getPlayers().get(2), new Coordinates(1, 1));
		map.movePlayerTo(gameBoard.getPlayers().get(4), new Coordinates(2, 1));
		assertEquals(new Coordinates(1, 1), map.getPlayerCoordinates(gameBoard.getPlayers().get(0)));
		assertEquals(new Coordinates(0, 0), map.getPlayerCoordinates(gameBoard.getPlayers().get(1)));
		assertEquals(new Coordinates(1, 1), map.getPlayerCoordinates(gameBoard.getPlayers().get(2)));
		assertEquals(new Coordinates(2, 3), map.getPlayerCoordinates(gameBoard.getPlayers().get(3)));
		assertEquals(new Coordinates(2, 1), map.getPlayerCoordinates(gameBoard.getPlayers().get(4)));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void movePlayerTo_playerOutOfColumn_throwsOutOfBoundaryException() {
		Model model = new Model("MediumMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		map.movePlayerTo(gameBoard.getPlayers().get(0), new Coordinates(1, 5));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void movePlayerTo_playerOutOfRow_throwsOutOfBoundaryException() {
		Model model = new Model("MediumMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		map.movePlayerTo(gameBoard.getPlayers().get(0), new Coordinates(9, 1));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void movePlayerTo_playerOutOfMap_throwsOutOfBoundaryException() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		map.movePlayerTo(gameBoard.getPlayers().get(0), new Coordinates(2, 0));
	}

	@Test
	public void reachableCoordinates_zeroDistance_correctOutput() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		ArrayList<Coordinates> correctReachableSquares = new ArrayList<>();
		correctReachableSquares.add(new Coordinates(1, 1));
		assertEquals(correctReachableSquares, map.reachableCoordinates(new Coordinates(1, 1), 0));
	}

	@Test
	public void reachableCoordinates_oneDistance_correctOutput() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(1, 0));
		correctOutput.add(new Coordinates(0, 0));
		correctOutput.add(new Coordinates(1, 1));
		ArrayList<Coordinates> methodOutput = new ArrayList<>(map.reachableCoordinates(new Coordinates(1, 0), 1));
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
		Model model = new Model("BigMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
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
		ArrayList<Coordinates> methodOutput = new ArrayList<>(map.reachableCoordinates(new Coordinates(1, 2), 4));

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
		Model model = new Model("BigMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
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
		ArrayList<Coordinates> methodOutput = new ArrayList<>(map.reachableCoordinates(new Coordinates(1, 2), 5));

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void reachableCoordinates_maxDistance3_correctOutput() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(2, 2));
		correctOutput.add(new Coordinates(2, 1));
		correctOutput.add(new Coordinates(0, 2));
		correctOutput.add(new Coordinates(0, 0));
		correctOutput.add(new Coordinates(1, 3));
		correctOutput.add(new Coordinates(1, 0));
		correctOutput.add(new Coordinates(1, 2));
		correctOutput.add(new Coordinates(1, 1));
		ArrayList<Coordinates> methodOutput = new ArrayList<>(map.reachableCoordinates(new Coordinates(1, 1), 2));

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
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(2, 3));
		correctOutput.add(new Coordinates(2, 2));
		correctOutput.add(new Coordinates(2, 1));
		correctOutput.add(new Coordinates(0, 1));
		correctOutput.add(new Coordinates(0, 2));
		correctOutput.add(new Coordinates(0, 0));
		correctOutput.add(new Coordinates(1, 3));
		correctOutput.add(new Coordinates(1, 0));
		correctOutput.add(new Coordinates(1, 2));
		correctOutput.add(new Coordinates(1, 1));
		ArrayList<Coordinates> methodOutput = new ArrayList<>(map.reachableCoordinates(new Coordinates(1, 1), 9));

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void getRoomCoordinates_MediumMapCoordinates_correctOutput() {
		Model model = new Model("MediumMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();

		//--------------------------------------------FIRST ROOM---------------------------------
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(0, 0));
		correctOutput.add(new Coordinates(0, 2));
		correctOutput.add(new Coordinates(0, 1));
		ArrayList<Coordinates> methodOutput = new ArrayList<>(map.getRoomCoordinates(new Coordinates(0, 0)));

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);

		//--------------------------------------------SECOND ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(0, 3));
		methodOutput = new ArrayList<>(map.getRoomCoordinates(new Coordinates(0, 3)));

		assertEquals(correctOutput, methodOutput);

		//--------------------------------------------THIRD ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(1, 0));
		correctOutput.add(new Coordinates(1, 1));
		methodOutput = new ArrayList<>(map.getRoomCoordinates(new Coordinates(1, 0)));

		assertEquals(correctOutput.size(), methodOutput.size());

		valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);

		//--------------------------------------------FOURTH ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(1, 2));
		correctOutput.add(new Coordinates(1, 3));
		correctOutput.add(new Coordinates(2, 2));
		correctOutput.add(new Coordinates(2, 3));
		methodOutput = new ArrayList<>(map.getRoomCoordinates(new Coordinates(2, 3)));

		assertEquals(correctOutput.size(), methodOutput.size());

		valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);

		//--------------------------------------------FIFTH ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(2, 1));

		methodOutput = new ArrayList<>(map.getRoomCoordinates(new Coordinates(2, 1)));

		assertEquals(correctOutput, methodOutput);
	}

	@Test
	public void getRoomCoordinates_BigMapSquare_correctOutput() {
		Model model = new Model("BigMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();

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

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapCoordinatesOutOfTheMap_throwsOutOfBoundaryException() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		map.getRoomCoordinates(new Coordinates(5, 3));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapBlackCoordinates_throwsOutOfBoundaryException() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		map.getRoomCoordinates(new Coordinates(0, 3));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapBlackSquare_throwsOutOfBoundaryException() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(0, AmmoType.RED_AMMO, Color.CharacterColorType.YELLOW, possibleDirection, new Coordinates(0, 2), gameBoard));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapSquareOutOfTheMap1_throwsOutOfBoundaryException() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(0, AmmoType.RED_AMMO, Color.CharacterColorType.YELLOW, possibleDirection, new Coordinates(0, 9), gameBoard));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapSquareOutOfTheMap2_throwsOutOfBoundaryException() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(0, AmmoType.RED_AMMO, Color.CharacterColorType.DEFAULT, possibleDirection, new Coordinates(0, 0), gameBoard));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapSquareOutOfTheMap3_throwsOutOfBoundaryException() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(0, AmmoType.RED_AMMO, Color.CharacterColorType.YELLOW, possibleDirection, new Coordinates(0, 0), gameBoard));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapSquareOutOfTheMap4_throwsOutOfBoundaryException() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(0, AmmoType.RED_AMMO, Color.CharacterColorType.DEFAULT, possibleDirection, new Coordinates(9, 9), gameBoard));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapSquareOutOfTheMap_throwsOutOfBoundaryException() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(0, AmmoType.RED_AMMO, Color.CharacterColorType.YELLOW, possibleDirection, new Coordinates(9, 1), gameBoard));
	}


	@Test(expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapNewSquare_throwsOutOfBoundaryException() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(0, AmmoType.RED_AMMO, Color.CharacterColorType.YELLOW, possibleDirection, new Coordinates(0, 1), gameBoard));
	}

	@Test
	public void visible_playersInTheSameSquare_correctOutput() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		map.movePlayerTo(gameBoard.getPlayers().get(0), new Coordinates(1, 1));
		map.movePlayerTo(gameBoard.getPlayers().get(1), new Coordinates(1, 1));
		assertTrue(map.isVisible(gameBoard.getPlayers().get(0), gameBoard.getPlayers().get(1)));
	}

	@Test
	public void visible_playersNotVisible_correctOutput() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();

		map.movePlayerTo(gameBoard.getPlayers().get(2), new Coordinates(0, 0));
		map.movePlayerTo(gameBoard.getPlayers().get(3), new Coordinates(2, 3));
		assertFalse(map.isVisible(gameBoard.getPlayers().get(2), gameBoard.getPlayers().get(3)));
	}

	@Test
	public void visible_playersOnTheEdge_correctOutput() {
		Model model = new Model("SmallMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();

		map.movePlayerTo(gameBoard.getPlayers().get(0), new Coordinates(1, 2));
		map.movePlayerTo(gameBoard.getPlayers().get(1), new Coordinates(1, 3));
		map.movePlayerTo(gameBoard.getPlayers().get(2), new Coordinates(0, 0));
		map.movePlayerTo(gameBoard.getPlayers().get(3), new Coordinates(2, 2));
		map.movePlayerTo(gameBoard.getPlayers().get(4), new Coordinates(0, 1));
		assertTrue(map.isVisible(gameBoard.getPlayers().get(0), gameBoard.getPlayers().get(1)));
		assertTrue(map.isVisible(gameBoard.getPlayers().get(0), gameBoard.getPlayers().get(2)));
		assertFalse(map.isVisible(gameBoard.getPlayers().get(0), gameBoard.getPlayers().get(3)));
		assertTrue(map.isVisible(gameBoard.getPlayers().get(0), gameBoard.getPlayers().get(4)));
	}

	@Test
	public void getSpawnSquare_allAmmoTypes_allSpawnCoordinates() {
		Model model = new Model("BigMap", players, 8);
		GameBoard gameBoard = model.getGameBoard();
		GameMap map = gameBoard.getGameMap();
		assertEquals(new Coordinates(2, 3), map.getSpawnSquare(AmmoType.YELLOW_AMMO));
		assertEquals(new Coordinates(1, 0), map.getSpawnSquare(AmmoType.RED_AMMO));
		assertEquals(new Coordinates(0, 2), map.getSpawnSquare(AmmoType.BLUE_AMMO));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getCoordinates_squareNotInTheMap_throwsOutOfBoundariesException() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getCoordinates(new SpawnSquare(0, AmmoType.RED_AMMO, Color.CharacterColorType.YELLOW, possibleDirection, new Coordinates(2, 1), gameBoard));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getCoordinates_squareOutOfBoundaries_throwsOutOfBoundariesException() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getCoordinates(new SpawnSquare(0, AmmoType.RED_AMMO, Color.CharacterColorType.YELLOW, possibleDirection, new Coordinates(5, 1), gameBoard));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getCoordinates_blackSquare_throwsOutOfBoundariesException() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getCoordinates(new SpawnSquare(0, AmmoType.RED_AMMO, Color.CharacterColorType.DEFAULT, possibleDirection, new Coordinates(2, 1), gameBoard));
	}

	@Test
	public void getCoordinates_squareInTheMap_correctOutput() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		map.movePlayerTo(gameBoard.getPlayers().get(0), new Coordinates(2, 3));
		assertEquals(new Coordinates(2, 3), map.getCoordinates(map.getPlayerSquare(gameBoard.getPlayers().get(0))));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getSquare_coordinatesOutOfTheMap1_throwsOutOfBoundariesException() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		map.getSquare(new Coordinates(9, 2));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getSquare_coordinatesOutOfTheMap2_throwsOutOfBoundariesException() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		map.getSquare(new Coordinates(1, 5));
	}

	@Test(expected = OutOfBoundariesException.class)
	public void getSquare_blackCoordinates_throwsOutOfBoundariesException() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		map.getSquare(new Coordinates(2, 0));
	}

	@Test
	public void getSquare_coordinatesInTheMap_correctOutput() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		map.movePlayerTo(gameBoard.getPlayers().get(0), new Coordinates(1, 2));
		assertEquals(map.getPlayerSquare(gameBoard.getPlayers().get(0)), map.getSquare(new Coordinates(1, 2)));
	}

	@Test
	public void isSpawnSquare() {
		Model model = new Model("MediumMap", players, 8);
GameBoard gameBoard = model.getGameBoard();
GameMap map = gameBoard.getGameMap();
		assertTrue(map.isSpawnSquare(new Coordinates(0, 2)));
		assertTrue(map.isSpawnSquare(new Coordinates(1, 0)));
		assertTrue(map.isSpawnSquare(new Coordinates(2, 3)));
		assertFalse(map.isSpawnSquare(new Coordinates(2, 2)));
		assertFalse(map.isSpawnSquare(new Coordinates(8, 6)));
	}
}