package it.polimi.se2019.view;

import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.Utils;
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
		player1 = new Player("Test 1", 0, Utils.MAGENTA);
		player2 = new Player("Test 2", 1, Utils.YELLOW);
		player3 = new Player("Test 3", 2, Utils.BLUE);
		player4 = new Player("Test 4", 3, Utils.CYAN);
		player5 = new Player("Test 5", 4, Utils.RED);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		GameMap gameMap = new GameMap("MediumMap.txt", players);

		gameMap.movePlayerTo(player1, new Coordinates(2,2));
		gameMap.movePlayerTo(player2, new Coordinates(2,2));
		gameMap.movePlayerTo(player3, new Coordinates(2,2));
		gameMap.movePlayerTo(player4, new Coordinates(2,2));
		gameMap.movePlayerTo(player5, new Coordinates(2,2));

		CLIView cliView= new CLIView();
		cliView.updateGameMapRep(new GameMapRep(gameMap));
		cliView.updatePlayerRep(new PlayerRep(player1));
		cliView.updatePlayerRep(new PlayerRep(player2));
		cliView.updatePlayerRep(new PlayerRep(player3));
		cliView.updatePlayerRep(new PlayerRep(player4));
		cliView.updatePlayerRep(new PlayerRep(player5));

		cliView.displayGame();
	}
}