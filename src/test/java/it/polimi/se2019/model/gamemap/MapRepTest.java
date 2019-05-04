package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.Model;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class MapRepTest {

	private ArrayList<String> players;
	GameMap gameMap;
	GameBoard gameBoard;

	@Before
	public void setUp() throws Exception {
		players = new ArrayList<>();
		players.add("Test 1");
		players.add("Test 2");
		players.add("Test 3");
		players.add("Test 4");
		players.add("Test 5");
		Model model = new Model("MediumMap.txt", players, 8);
		gameBoard = model.getGameBoard();
		gameMap = gameBoard.getGameMap();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getNumOfRows_correctInput_correctOutput() {
		assertEquals(3, new GameMapRep(gameMap).getNumOfRows());
	}

	@Test
	public void getNumOfColumns_correctInput_correctOutput() {
		assertEquals(4, new GameMapRep(gameMap).getNumOfColumns());
	}

	@Test
	public void getMapRep_correctInput_correctOutput() {
		GameMapRep gameMapRep = new GameMapRep(gameMap);
		MapSquareRep[][] mapRep = gameMapRep.getMapRep();
		MapSquareRep mapSquareRep;
		MapSquare mapSquare;

		for (int i = 0; i < gameMapRep.getNumOfRows(); i++) {
			for (int j = 0; j < gameMapRep.getNumOfColumns(); j++) {
				MapSquareRep squareRep = (MapSquareRep) gameMap.getSquareRep(new Coordinates(i,j));
				//mapSquareRep.getCards(); TODO
				assertEquals(mapRep[i][j].getCoordinates(), squareRep.getCoordinates());
				assertEquals(mapRep[i][j].getRoomID(), squareRep.getRoomID());
			}
		}
	}

	@Test
	public void getPlayersPositions_correctInput_correctOutput() {
		gameMap.movePlayerTo(gameBoard.getPlayers().get(0), new Coordinates(0,0));
		gameMap.movePlayerTo(gameBoard.getPlayers().get(3), new Coordinates(1,1));
		gameMap.movePlayerTo(gameBoard.getPlayers().get(2), new Coordinates(1,2));
		gameMap.movePlayerTo(gameBoard.getPlayers().get(3), new Coordinates(2,1));
		HashMap<String, Coordinates> playerCoordinates = new GameMapRep(gameMap).getPlayersCoordinates();
		playerCoordinates.equals(gameMap.getPlayersCoordinates());
	}
}