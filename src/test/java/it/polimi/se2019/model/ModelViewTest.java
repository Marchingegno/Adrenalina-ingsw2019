package it.polimi.se2019.model;

import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ModelViewTest {

	private GameMap gameMap;

	private GameBoardRep gameBoardRep;
	private GameMapRep mapRep;
	private ArrayList<PlayerRep> playerReps;
	private ArrayList<Player> players;

	@Before
	public void setUp() throws Exception {
		ArrayList<String> playerNames = new ArrayList<>();
		playerNames.add("Test 1");
		playerNames.add("Test 2");
		playerNames.add("Test 3");
		GameBoard gameBoard = new GameBoard("MediumMap.txt", playerNames, 5);

		gameMap = gameBoard.getGameMap();

		mapRep = new GameMapRep(gameMap);
		gameBoardRep = new GameBoardRep(gameBoard);
		playerReps = new ArrayList<>();
		for (Player player : gameBoard.getPlayers())
			playerReps.add(new PlayerRep(player));
	}

	@Test
	public void getGameBoardRep_initialState_correctOutput() {
		ModelView modelView = new ModelView(gameBoardRep, gameMap, playerReps);
		assertEquals(gameBoardRep, modelView.getGameBoardRep());
	}

	/**
	 * TODO
	 * @Test
	public void getMapRep_initialState_correctOutput() {
		ModelView modelView = new ModelView(gameBoardRep, gameMap, playerReps);
		assertEquals(gameMap, modelView.getMapRep());
	}*/

	@Test
	public void getPlayerReps_initialState_correctOutput() {
		ModelView modelView = new ModelView(gameBoardRep, gameMap, playerReps);
		assertEquals(playerReps, modelView.getPlayerReps());
	}
}