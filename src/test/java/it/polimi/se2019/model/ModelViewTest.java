package it.polimi.se2019.model;

import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import org.junit.Before;

import java.util.ArrayList;

public class ModelViewTest {

	private GameBoard gameBoard;
	private GameMap gameMap;
	private ArrayList<Player> players;

	@Before
	public void setUp() throws Exception {
		ArrayList<String> playerNames = new ArrayList<>();
		playerNames.add("Test 1");
		playerNames.add("Test 2");
		playerNames.add("Test 3");
		gameBoard = new GameBoard("MediumMap.txt",playerNames, 8);
	}
/**
	@Test
	public void getGameBoardRep_initialState_correctOutput() {
		ModelRep modelRep = new ModelRep(gameBoard, gameMap, players);
		assertEquals(gameBoard, modelRep.getGameBoardRep());
	}

	/**
	 * TODO
	 * @Test
	public void getMapRep_initialState_correctOutput() {
		ModelView modelView = new ModelView(gameBoardRep, gameMap, playerReps);
		assertEquals(gameMap, modelView.getMapRep());
	}

	@Test
	public void getPlayerReps_initialState_correctOutput() {
		ModelRep modelRep = new ModelRep(gameBoard, gameMap, players);
		assertEquals(players, modelRep.getPlayerReps());
	}*/
}