package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		player1 = new Player("Test 1", 0, Utils.CharacterColorType.GREEN);
		player2 = new Player("Test 2", 1, Utils.CharacterColorType.YELLOW);
		player3 = new Player("Test 3", 2, Utils.CharacterColorType.BLUE);
		player4 = new Player("Test 4", 3, Utils.CharacterColorType.RED);
		player5 = new Player("Test 5", 4, Utils.CharacterColorType.MAGENTA);
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
		MapSquareRep[][] mapRep = gameMapRep.getMapRep();
		MapSquareRep mapSquareRep;
		MapSquare mapSquare;

		for (int i = 0; i < gameMapRep.getNumOfRows(); i++) {
			for (int j = 0; j < gameMapRep.getNumOfColumns(); j++) {
				mapSquareRep = mapRep[i][j];
				try{
					mapSquare = gameMap.getSquare(new Coordinates(i,j));
				}catch(OutOfBoundariesException e){
					mapSquare = new AmmoSquare(-1, new boolean[4], new Coordinates(i,j));
				}
				//mapSquareRep.getCards(); TODO
				assertEquals(mapSquareRep.getCoordinates(), mapSquare.getCoordinates());
				assertEquals(mapSquareRep.getRoomID(), mapSquare.getRoomID());
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