package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.player.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class MapRepTest {

	private Player player1, player2, player3, player4, player5;
	private ArrayList<Player> players;
	GameMap gameMap;

	@Before
	public void setUp() throws Exception {
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
		gameMap = new GameMap("MediumMap.txt", players);
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
		SquareRep[][] mapRep = gameMapRep.getMapRep();
		SquareRep squareRep;
		Square square;

		for (int i = 0; i < gameMapRep.getNumOfRows(); i++) {
			for (int j = 0; j < gameMapRep.getNumOfColumns(); j++) {
				squareRep = mapRep[i][j];
				try{
					square = gameMap.getSquare(new Coordinates(i,j));
				}catch(OutOfBoundariesException e){
					square = new AmmoSquare(-1, new boolean[4], new Coordinates(i,j));
				}
				//squareRep.getCards(); TODO
				assertEquals(squareRep.getCoordinates(), square.getCoordinates());
				assertEquals(squareRep.getRoomID(), square.getRoomID());
			}
		}
	}

	@Test
	public void getPlayersPositions_correctInput_correctOutput() {
		gameMap.movePlayerTo(player1, new Coordinates(0,0));
		gameMap.movePlayerTo(player2, new Coordinates(1,1));
		gameMap.movePlayerTo(player3, new Coordinates(1,2));
		gameMap.movePlayerTo(player4, new Coordinates(2,1));
		HashMap<String, Coordinates> playerCoordinates = new GameMapRep(gameMap).getPlayersCoordinates();
		playerCoordinates.equals(gameMap.getPlayersCoordinates());
	}
}