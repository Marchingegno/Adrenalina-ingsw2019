package it.polimi.se2019.view;

import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CLIViewTest {

	private Player player1, player2, player3, player4, player5;
	private ArrayList<Player> players;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void askNickname() {
	}

	@Test
	public void displayWaitingPlayers() {
	}

	@Test
	public void displayTimerStarted() {
	}

	@Test
	public void displayText() {
	}

	@Test
	public void displayGame() {
	}

	@Test
	public void askMapToUse() {
	}

	@Test
	public void askSkullsForGame() {
	}

	@Test
	public void askAction() {
	}

	@Test
	public void showTargettablePlayers() {
	}

	@Test
	public void updateGameMapRep() {
	}

	@Test
	public void updateGameBoardRep() {
	}

	@Test
	public void updatePlayerRep() {
	}

	@Test
	public void showMessage() {
	}

	@Test
	public void displayMapTest() {
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
		GameMap gameMap = new GameMap("SmallMap.txt", players);
		CLIView cliView= new CLIView();
		cliView.updateGameMapRep(new GameMapRep(gameMap));

		cliView.displayMap();
	}
}