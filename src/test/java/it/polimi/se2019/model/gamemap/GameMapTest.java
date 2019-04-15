package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameMapTest {

	private Player player1, player2, player3, player4, player5;
	private ArrayList<Player> players;

	@Before
	public void setUp(){
		players = new ArrayList<>();
		player1 = new Player("Test 1", 0, Color.GREEN);
		player2 = new Player("Test 2", 1, Color.YELLOW);
		player3 = new Player("Test 3", 2, Color.BLUE);
		player4 = new Player("Test 4", 3, Color.GRAY);
		player5 = new Player("Test 5", 4, Color.MAGENTA);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
	}

	@After
	public void tearDown(){
	}


	@Test
	public void getNumOfRows_correctInput_correcntOutput(){
		GameMap map = new GameMap("MediumMap.txt", players);
		assertEquals(3, map.getNumOfRows());
	}

	@Test
	public void getNumOfColumn_correctInput_correcntOutput(){
		GameMap map = new GameMap("MediumMap.txt", players);
		assertEquals(4, map.getNumOfColumns());
	}

	@Test (expected = PlayerNotInTheMapException.class)
	public void playerCoordinates_playerNotYetPlaced1_throwsPlayerNotInTheMapException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.playerCoordinates(player1);
	}

	@Test (expected = PlayerNotInTheMapException.class)
	public void playerCoordinates_playerNotYetPlaced4_throwsPlayerNotInTheMapException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.playerCoordinates(player4);
	}

	@Test
	public void playerCoordinates_playerMoved_correctOutput() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.movePlayerTo(player1, new Coordinates(2, 3));
		assertEquals(new Coordinates(2, 3), map.playerCoordinates(player1));
	}

	@Test (expected = PlayerNotInTheMapException.class)
	public void playerSquare_playerNotYetPlaced1_throwsPlayerNotInTheMapException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.playerSquare(player1);
	}

	@Test (expected = PlayerNotInTheMapException.class)
	public void playerSquare_playerNotYetPlaced4_throwsPlayerNotInTheMapException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.playerCoordinates(player4);
	}

	@Test
	public void playerSquare_playerMoved_correctOutput() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.movePlayerTo(player1, new Coordinates(2, 3));
		assertEquals(map.getSquare(new Coordinates(2, 3)), map.playerSquare(player1));
	}

	@Test
	public void movePlayerTo_correctInput_correctOutput() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.movePlayerTo(player1, new Coordinates(1,1));
		map.movePlayerTo(player2, new Coordinates(0,0));
		map.movePlayerTo(player3, new Coordinates(2,2));
		map.movePlayerTo(player4, new Coordinates(2,3));
		map.movePlayerTo(player3, new Coordinates(1,1));
		map.movePlayerTo(player5, new Coordinates(2,1));
		assertEquals(new Coordinates(1,1), map.playerCoordinates(player1));
		assertEquals(new Coordinates(0,0), map.playerCoordinates(player2));
		assertEquals(new Coordinates(1,1), map.playerCoordinates(player3));
		assertEquals(new Coordinates(2,3), map.playerCoordinates(player4));
		assertEquals(new Coordinates(2,1), map.playerCoordinates(player5));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void movePlayerTo_playerOutOfColumn_throwsOutOfBoundaryException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.movePlayerTo(player1, new Coordinates(1,5));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void movePlayerTo_playerOutOfRow_throwsOutOfBoundaryException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.movePlayerTo(player1, new Coordinates(9,1));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void movePlayerTo_playerOutOfMap_throwsOutOfBoundaryException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.movePlayerTo(player1, new Coordinates(2,0));
	}

	@Test
	public void reachableCoordinates_zeroDistance_correctOutput() {
		GameMap map = new GameMap("MediumMap.txt", players);
		ArrayList<Coordinates> correctReachableSquares = new ArrayList<>();
		correctReachableSquares.add(new Coordinates(1,1));
		assertEquals(correctReachableSquares , map.reachableCoordinates(new Coordinates(1,1), 0));
	}

	@Test
	public void reachableCoordinates_oneDistance_correctOutput() {
		GameMap map = new GameMap("MediumMap.txt", players);
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(1,0));
		correctOutput.add(new Coordinates(0,0));
		correctOutput.add(new Coordinates(1,1));
		ArrayList<Coordinates> methodOutput = map.reachableCoordinates(new Coordinates(1,0), 1);

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if(!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void reachableCoordinates_maxDistance1_correctOutput() {
		GameMap map = new GameMap("BigMap.txt", players);
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(2,3));
		correctOutput.add(new Coordinates(2,2));
		correctOutput.add(new Coordinates(2,1));
		correctOutput.add(new Coordinates(2,0));
		correctOutput.add(new Coordinates(0,1));
		correctOutput.add(new Coordinates(0,2));
		correctOutput.add(new Coordinates(0,3));
		correctOutput.add(new Coordinates(0,0));
		correctOutput.add(new Coordinates(1,3));
		correctOutput.add(new Coordinates(1,0));
		correctOutput.add(new Coordinates(1,2));
		correctOutput.add(new Coordinates(1,1));
		ArrayList<Coordinates> methodOutput = map.reachableCoordinates(new Coordinates(1,2), 4);

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if(!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void reachableCoordinates_maxDistance2_correctOutput() {
		GameMap map = new GameMap("BigMap.txt", players);
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(2,3));
		correctOutput.add(new Coordinates(2,2));
		correctOutput.add(new Coordinates(2,1));
		correctOutput.add(new Coordinates(2,0));
		correctOutput.add(new Coordinates(0,1));
		correctOutput.add(new Coordinates(0,2));
		correctOutput.add(new Coordinates(0,3));
		correctOutput.add(new Coordinates(0,0));
		correctOutput.add(new Coordinates(1,3));
		correctOutput.add(new Coordinates(1,0));
		correctOutput.add(new Coordinates(1,2));
		correctOutput.add(new Coordinates(1,1));
		ArrayList<Coordinates> methodOutput = map.reachableCoordinates(new Coordinates(1,2), 5);

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if(!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void reachableCoordinates_maxDistance3_correctOutput() {
		GameMap map = new GameMap("SmallMap.txt", players);
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(2,2));
		correctOutput.add(new Coordinates(2,1));
		correctOutput.add(new Coordinates(0,2));
		correctOutput.add(new Coordinates(0,0));
		correctOutput.add(new Coordinates(1,3));
		correctOutput.add(new Coordinates(1,0));
		correctOutput.add(new Coordinates(1,2));
		correctOutput.add(new Coordinates(1,1));
		ArrayList<Coordinates> methodOutput = map.reachableCoordinates(new Coordinates(1,1), 2);

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if(!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void reachableCoordinates_overMaxDistance_correctOutput() {
		GameMap map = new GameMap("SmallMap.txt", players);
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(2,3));
		correctOutput.add(new Coordinates(2,2));
		correctOutput.add(new Coordinates(2,1));
		correctOutput.add(new Coordinates(0,1));
		correctOutput.add(new Coordinates(0,2));
		correctOutput.add(new Coordinates(0,0));
		correctOutput.add(new Coordinates(1,3));
		correctOutput.add(new Coordinates(1,0));
		correctOutput.add(new Coordinates(1,2));
		correctOutput.add(new Coordinates(1,1));
		ArrayList<Coordinates> methodOutput = map.reachableCoordinates(new Coordinates(1,1), 9);

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if(!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test
	public void getRoomCoordinates_MediumMapCoordinates_correctOutput() {
		GameMap map = new GameMap("MediumMap.txt", players);

		//--------------------------------------------FIRST ROOM---------------------------------
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(0,0));
		correctOutput.add(new Coordinates(0,2));
		correctOutput.add(new Coordinates(0,1));
		ArrayList<Coordinates> methodOutput = map.getRoomCoordinates(new Coordinates(0,0));

		assertEquals(correctOutput.size(), methodOutput.size());

		boolean valid = true;
		for (Coordinates coordinates : correctOutput) {
			if(!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);

		//--------------------------------------------SECOND ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(0,3));
		methodOutput = map.getRoomCoordinates(new Coordinates(0,3));

		assertEquals(correctOutput, methodOutput);

		//--------------------------------------------THIRD ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(1,0));
		correctOutput.add(new Coordinates(1,1));
		methodOutput = map.getRoomCoordinates(new Coordinates(1,0));

		assertEquals(correctOutput.size(), methodOutput.size());

		valid = true;
		for (Coordinates coordinates : correctOutput) {
			if(!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);

		//--------------------------------------------FOURTH ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(1,2));
		correctOutput.add(new Coordinates(1,3));
		correctOutput.add(new Coordinates(2,2));
		correctOutput.add(new Coordinates(2,3));
		methodOutput = map.getRoomCoordinates(new Coordinates(2,3));

		assertEquals(correctOutput.size(), methodOutput.size());

		valid = true;
		for (Coordinates coordinates : correctOutput) {
			if(!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);

		//--------------------------------------------FIFTH ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(2,1));

		methodOutput = map.getRoomCoordinates(new Coordinates(2,1));

		assertEquals(correctOutput, methodOutput);
	}

	@Test
	public void getRoomCoordinates_BigMapSquare_correctOutput() {
		GameMap map = new GameMap("BigMap.txt", players);

		//--------------------------------------------FIRST ROOM---------------------------------
		ArrayList<Coordinates> correctOutput = new ArrayList<>();
		correctOutput.add(new Coordinates(1, 0));
		correctOutput.add(new Coordinates(0, 0));
		ArrayList<Coordinates> methodOutput = map.getRoomCoordinates(map.getSquare(new Coordinates(0, 0)));

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
		methodOutput = map.getRoomCoordinates(map.getSquare(new Coordinates(0, 2)));

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
		methodOutput = map.getRoomCoordinates(map.getSquare(new Coordinates(0, 3)));

		assertEquals(correctOutput, methodOutput);

		//--------------------------------------------FOURTH ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(1, 1));
		methodOutput = map.getRoomCoordinates(map.getSquare(new Coordinates(1, 1)));

		assertEquals(correctOutput, methodOutput);

		//--------------------------------------------FIFTH ROOM---------------------------------
		correctOutput.clear();
		correctOutput.add(new Coordinates(2, 2));
		correctOutput.add(new Coordinates(2, 3));
		correctOutput.add(new Coordinates(1, 2));
		correctOutput.add(new Coordinates(1, 3));
		methodOutput = map.getRoomCoordinates(map.getSquare(new Coordinates(1, 3)));

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
		methodOutput = map.getRoomCoordinates(map.getSquare(new Coordinates(2, 0)));

		assertEquals(correctOutput.size(), methodOutput.size());

		valid = true;
		for (Coordinates coordinates : correctOutput) {
			if (!(methodOutput.contains(coordinates)))
				valid = false;
		}
		assertTrue(valid);
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapCoordinatesOutOfTheMap_throwsOutOfBoundaryException() {
		GameMap map = new GameMap("SmallMap.txt", players);
		map.getRoomCoordinates(new Coordinates(5, 3));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapBlackCoordinates_throwsOutOfBoundaryException() {
		GameMap map = new GameMap("SmallMap.txt", players);
		map.getRoomCoordinates(new Coordinates(0, 3));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapBlackSquare_throwsOutOfBoundaryException() {
		GameMap map = new GameMap("SmallMap.txt", players);
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(AmmoType.RED_AMMO, -1, possibleDirection, new Coordinates(0,2)));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapSquareOutOfTheMap1_throwsOutOfBoundaryException() {
		GameMap map = new GameMap("SmallMap.txt", players);
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(AmmoType.RED_AMMO, 4, possibleDirection, new Coordinates(0,9)));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapSquareOutOfTheMap2_throwsOutOfBoundaryException() {
		GameMap map = new GameMap("SmallMap.txt", players);
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(AmmoType.RED_AMMO, -1, possibleDirection, new Coordinates(0,0)));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapSquareOutOfTheMap3_throwsOutOfBoundaryException() {
		GameMap map = new GameMap("SmallMap.txt", players);
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(AmmoType.RED_AMMO, 4, possibleDirection, new Coordinates(0,0)));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapSquareOutOfTheMap4_throwsOutOfBoundaryException() {
		GameMap map = new GameMap("SmallMap.txt", players);
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(AmmoType.RED_AMMO, -1, possibleDirection, new Coordinates(9,9)));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapSquareOutOfTheMap_throwsOutOfBoundaryException() {
		GameMap map = new GameMap("SmallMap.txt", players);
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(AmmoType.RED_AMMO, 4, possibleDirection, new Coordinates(9,1)));
	}


	@Test (expected = OutOfBoundariesException.class)
	public void getRoomCoordinates_SmallMapNewSquare_throwsOutOfBoundaryException() {
		GameMap map = new GameMap("SmallMap.txt", players);
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getRoomCoordinates(new SpawnSquare(AmmoType.RED_AMMO, 2, possibleDirection, new Coordinates(0, 1)));
	}

	@Test
	public void visible_playersInTheSameSquare_correctOutput() {
		GameMap map = new GameMap("SmallMap.txt", players);
		map.movePlayerTo(player1, new Coordinates(1,1));
		map.movePlayerTo(player2, new Coordinates(1,1));
		assertTrue(map.visible(player1, player2));
	}

	@Test
	public void visible_playersNotVisible_correctOutput() {
		GameMap map = new GameMap("SmallMap.txt", players);

		map.movePlayerTo(player3, new Coordinates(0,0));
		map.movePlayerTo(player4, new Coordinates(2,3));
		assertFalse(map.visible(player3, player4));
	}

	@Test
	public void visible_playersOnTheEdge_correctOutput() {
		GameMap map = new GameMap("SmallMap.txt", players);

		map.movePlayerTo(player1, new Coordinates(1,2));
		map.movePlayerTo(player2, new Coordinates(1,3));
		map.movePlayerTo(player3, new Coordinates(0,0));
		map.movePlayerTo(player4, new Coordinates(2,2));
		map.movePlayerTo(player5, new Coordinates(0,1));
		assertTrue(map.visible(player1, player2));
		assertTrue(map.visible(player1, player3));
		assertFalse(map.visible(player1, player4));
		assertTrue(map.visible(player1, player5));
	}

	@Test
	public void getSpawnSquare_allAmmoTypes_allSpawnCoordinates() {
		GameMap map = new GameMap("BigMap.txt", players);
		assertEquals(new Coordinates(2,3) , map.getSpawnSquare(AmmoType.YELLOW_AMMO));
		assertEquals(new Coordinates(1,0) , map.getSpawnSquare(AmmoType.RED_AMMO));
		assertEquals(new Coordinates(0,2) , map.getSpawnSquare(AmmoType.BLUE_AMMO));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getCoordinates_squareNotInTheMap_throwsOutOfBoundariesException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getCoordinates(new SpawnSquare(AmmoType.RED_AMMO, 4, possibleDirection, new Coordinates(2,1)));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getCoordinates_squareOutOfBoundaries_throwsOutOfBoundariesException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getCoordinates(new SpawnSquare(AmmoType.RED_AMMO, 4, possibleDirection, new Coordinates(5,1)));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getCoordinates_blackSquare_throwsOutOfBoundariesException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		boolean[] possibleDirection = new boolean[4];
		possibleDirection[0] = true;
		possibleDirection[1] = true;
		possibleDirection[2] = true;
		possibleDirection[3] = true;
		map.getCoordinates(new SpawnSquare(AmmoType.RED_AMMO, -1, possibleDirection, new Coordinates(2,1)));
	}

	@Test
	public void getCoordinates_squareInTheMap_correctOutput() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.movePlayerTo(player1, new Coordinates(2,3));
		assertEquals(new Coordinates(2,3), map.getCoordinates(map.playerSquare(player1)));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getSquare_coordinatesOutOfTheMap1_throwsOutOfBoundariesException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.getSquare(new Coordinates(9,2));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getSquare_coordinatesOutOfTheMap2_throwsOutOfBoundariesException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.getSquare(new Coordinates(1,5));
	}

	@Test (expected = OutOfBoundariesException.class)
	public void getSquare_blackCoordinates_throwsOutOfBoundariesException() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.getSquare(new Coordinates(2,0));
	}

	@Test
	public void getSquare_coordinatesInTheMap_correctOutput() {
		GameMap map = new GameMap("MediumMap.txt", players);
		map.movePlayerTo(player1, new Coordinates(1,2));
		assertEquals(map.playerSquare(player1), map.getSquare(new Coordinates(1,2)));
	}

	@Test
	public void isSpawnSquare() {
		GameMap map = new GameMap("BigMap.txt", players);
		assertTrue(map.isSpawnSquare(new Coordinates(0,2)));
		assertTrue(map.isSpawnSquare(new Coordinates(1,0)));
		assertTrue(map.isSpawnSquare(new Coordinates(2,3)));
		assertFalse(map.isSpawnSquare(new Coordinates(2,2)));
		assertFalse(map.isSpawnSquare(new Coordinates(8,6)));
	}
}