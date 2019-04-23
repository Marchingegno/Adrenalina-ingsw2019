package it.polimi.se2019.model;

import it.polimi.se2019.model.player.KillShotRep;
import it.polimi.se2019.utils.Utils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Desno365
 */
public class GameBoardRepTest {

	private static GameBoard gameBoard;

	private GameBoardRep gameBoardRep;

	@BeforeClass
	public static void oneTimeSetUp() {
		ArrayList<String> playerNames = new ArrayList<>();
		playerNames.add("Test 1");
		playerNames.add("Test 2");
		playerNames.add("Test 3");
		gameBoard = new GameBoard("MediumMap.txt", playerNames, 5);
		gameBoard.addKillShot(gameBoard.getCurrentPlayer(), true);
		gameBoard.addKillShot(gameBoard.getCurrentPlayer(), true); // double kill
		gameBoard.nextPlayerTurn();
		gameBoard.addKillShot(gameBoard.getCurrentPlayer(), false);
		gameBoard.nextPlayerTurn();
		gameBoard.addKillShot(gameBoard.getCurrentPlayer(), true);
	}

	@Before
	public void setUp() throws Exception {
		gameBoardRep = new GameBoardRep(gameBoard);
	}

	@Test
	public void getRemainingSkulls_initialState_correctOutput() {
		assertEquals(gameBoard.getRemainingSkulls(), gameBoardRep.getRemainingSkulls());
	}

	@Test
	public void getDoubleKills_initialState_correctOutput() {
		List<Utils.CharacterColorType> doubleKills = gameBoardRep.getDoubleKills();
		for (int i = 0; i < doubleKills.size(); i++)
			assertEquals(gameBoard.getDoubleKills().get(i).getPlayerColor(), doubleKills.get(i));
	}

	@Test
	public void getKillShoots_initialState_correctOutput() {
		List<KillShotRep> killShotReps = gameBoardRep.getKillShoots();
		for (int i = 0; i < killShotReps.size(); i++) {
			assertEquals(gameBoard.getKillShots().get(i).getPlayer().getPlayerName(), killShotReps.get(i).getPlayerName());
			assertEquals(gameBoard.getKillShots().get(i).isOverkill(), killShotReps.get(i).isOverkill());
		}
	}

	@Test
	public void getCurrentPlayer_initialState_correctOutput() {
		assertEquals(gameBoard.getCurrentPlayer().getPlayerName(), gameBoardRep.getCurrentPlayer());
	}
}