package it.polimi.se2019.view;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.GameBoard;
import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.message.StringMessage;
import it.polimi.se2019.network.server.ConnectionToClientInterface;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.Utils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class VirtualView implements ViewInterface {

	private ConnectionToClientInterface client;
	private Controller controller;

	public VirtualView(Controller controller, ConnectionToClientInterface client) {
		this.client = client;
		this.controller = controller;

		this.controller.getModel().getGameBoard().addObserver(new GameBoardObserver());
		System.out.println("Added Game Board Observer");
		this.controller.getModel().getGameBoard().getGameMap().addObserver(new GameMapObserver());
		System.out.println("Added Game Map Observer");
		for (Player player : this.controller.getModel().getPlayers()) {
			player.addObserver(new PlayerObserver());
			System.out.println("Added Player Observer");
		}
	}

	public void displayPossibleActions(List<MacroAction> possibleActions) {
		for (MacroAction macroAction : possibleActions) {
			Utils.logInfo(macroAction.toString());
		}
	}

	@Override
	public String askNickname() {
		return null;
	}

	@Override
	public void displayWaitingPlayers(String waitingPlayers) {

	}

	@Override
	public void displayTimerStarted(long delayInMs) {

	}

	@Override
	public void displayText(String text) {

	}

	@Override
	public void displayGame() {

	}

	@Override
	public int askMapToUse() {
		return 0;
	}

	@Override
	public int askSkullsForGame() {
		return 0;
	}

	@Override
	public void askAction() {

	}

	@Override
	public void showTargettablePlayers() {

	}

	@Override
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {

	}

	@Override
	public void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate) {

	}

	@Override
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {

	}

	@Override
	public void showMessage(String stringToShow) {

	}


	private class GameBoardObserver implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			System.out.println("Game Map Rep Created");
			client.sendMessage(new GameBoardRep((GameBoard) observable));
		}
	}

	private class GameMapObserver implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			System.out.println("Map Rep Created");
			client.sendMessage(new GameMapRep((GameMap) observable));
		}
	}

	private class PlayerObserver implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			System.out.println("Player Rep Created");
			client.sendMessage(new PlayerRep((Player) observable));
		}
	}
}