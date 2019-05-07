package it.polimi.se2019.view.client;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.GameConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
		ArrayList<String> playersName = new ArrayList<>();
		playersName.add("Bob");
		playersName.add("Foo");
		playersName.add("Boo");
		playersName.add("Pippo");
		playersName.add("Pluto");
		Model model = new Model(GameConstants.MapType.MEDIUM_MAP.getMapName(), playersName, 5);
		Controller controller = new Controller(model);
		GameMap gameMap = model.getGameBoard().getGameMap();
		List<Player> players = model.getPlayers();

		gameMap.movePlayerTo(players.get(0), new Coordinates(2,2));
		gameMap.movePlayerTo(players.get(1), new Coordinates(1,2));
		gameMap.movePlayerTo(players.get(2), new Coordinates(2,3));
		gameMap.movePlayerTo(players.get(3), new Coordinates(2,2));
		gameMap.movePlayerTo(players.get(4), new Coordinates(2,2));

		players.get(0).getPlayerBoard().addDamage(players.get(1), 2);
		players.get(0).getPlayerBoard().addDamage(players.get(4), 4);
		players.get(1).getPlayerBoard().addDamage(players.get(2), 3);
		players.get(1).getPlayerBoard().addDamage(players.get(3), 4);
		players.get(1).getPlayerBoard().addDamage(players.get(0), 3);
		players.get(1).getPlayerBoard().addDamage(players.get(3), 1);
		players.get(2).getPlayerBoard().addDamage(players.get(4), 1);
		players.get(2).getPlayerBoard().addDamage(players.get(0), 2);
		players.get(3).getPlayerBoard().addDamage(players.get(2), 3);
		players.get(3).getPlayerBoard().addDamage(players.get(1), 2);
		players.get(4).getPlayerBoard().addDamage(players.get(0), 1);
		players.get(4).getPlayerBoard().addDamage(players.get(2), 3);

		players.get(0).getPlayerBoard().addMarks(players.get(1), 1);
		players.get(0).getPlayerBoard().addMarks(players.get(2), 2);
		players.get(0).getPlayerBoard().addMarks(players.get(3), 3);
		players.get(1).getPlayerBoard().addMarks(players.get(3), 3);
		players.get(1).getPlayerBoard().addMarks(players.get(4), 1);
		players.get(2).getPlayerBoard().addMarks(players.get(1), 2);
		players.get(2).getPlayerBoard().addMarks(players.get(0), 1);
		players.get(3).getPlayerBoard().addMarks(players.get(2), 1);
		players.get(3).getPlayerBoard().addMarks(players.get(4), 1);
		players.get(4).getPlayerBoard().addMarks(players.get(3), 3);
		players.get(4).getPlayerBoard().addMarks(players.get(2), 1);

		model.getGameBoard().addKillShot(players.get(0), false);
		model.getGameBoard().addKillShot(players.get(1),true);
		model.getGameBoard().addKillShot(players.get(3), false);
		model.getGameBoard().addKillShot(players.get(4), true);
		model.getGameBoard().addKillShot(players.get(0), true);
		model.getGameBoard().addKillShot(players.get(2), false);

		CLIView cliView= new CLIView();

		cliView.updateGameBoardRep(new GameBoardRep(model.getGameBoard()));
		cliView.updateGameMapRep(new GameMapRep(gameMap));
		cliView.updatePlayerRep(new PlayerRep(players.get(0)));
		cliView.updatePlayerRep(new PlayerRep(players.get(1)));
		cliView.updatePlayerRep(new PlayerRep(players.get(2)));
		cliView.updatePlayerRep(new PlayerRep(players.get(3)));
		cliView.updatePlayerRep(new PlayerRep(players.get(4)));

		cliView.displayGame();

		gameMap.movePlayerTo(players.get(0), new Coordinates(1,2));
		gameMap.movePlayerTo(players.get(1), new Coordinates(0,3));
		gameMap.movePlayerTo(players.get(2), new Coordinates(1,3));
		gameMap.movePlayerTo(players.get(3), new Coordinates(2,1));
		gameMap.movePlayerTo(players.get(4), new Coordinates(0,0));

		players.get(0).getPlayerBoard().addDamage(players.get(4), 2);
		players.get(2).getPlayerBoard().addDamage(players.get(0), 1);
		players.get(2).getPlayerBoard().addDamage(players.get(3), 2);
		players.get(3).getPlayerBoard().addDamage(players.get(0), 3);
		players.get(3).getPlayerBoard().addDamage(players.get(4), 2);
		players.get(4).getPlayerBoard().addDamage(players.get(3), 1);
		players.get(4).getPlayerBoard().addDamage(players.get(1), 3);

		players.get(0).getPlayerBoard().addMarks(players.get(2), 2);
		players.get(0).getPlayerBoard().addMarks(players.get(1), 3);
		players.get(1).getPlayerBoard().addMarks(players.get(0), 3);
		players.get(1).getPlayerBoard().addMarks(players.get(2), 1);
		players.get(2).getPlayerBoard().addMarks(players.get(1), 2);
		players.get(2).getPlayerBoard().addMarks(players.get(0), 1);
		players.get(3).getPlayerBoard().addMarks(players.get(2), 1);
		players.get(3).getPlayerBoard().addMarks(players.get(4), 3);
		players.get(4).getPlayerBoard().addMarks(players.get(1), 3);
		players.get(4).getPlayerBoard().addMarks(players.get(0), 1);

		cliView.updateGameBoardRep(new GameBoardRep(model.getGameBoard()));
		cliView.updateGameMapRep(new GameMapRep(gameMap));
		cliView.updatePlayerRep(new PlayerRep(players.get(0)));
		cliView.updatePlayerRep(new PlayerRep(players.get(1)));
		cliView.updatePlayerRep(new PlayerRep(players.get(2)));
		cliView.updatePlayerRep(new PlayerRep(players.get(3)));
		cliView.updatePlayerRep(new PlayerRep(players.get(4)));

		cliView.displayGame();

	}
}