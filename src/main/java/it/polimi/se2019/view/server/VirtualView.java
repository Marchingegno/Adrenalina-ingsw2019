package it.polimi.se2019.view.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.GameBoard;
import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.server.ConnectionToClientInterface;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.ViewInterface;

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
		Utils.logInfo("Added Game Board Observer");
		this.controller.getModel().getGameBoard().getGameMap().addObserver(new GameMapObserver());
		Utils.logInfo("Added Game Map Observer");
		for (Player player : this.controller.getModel().getPlayers()) {
			player.addObserver(new PlayerObserver());
			Utils.logInfo("Added Player Observer");
		}
	}

	public void displayPossibleActions(List<MacroAction> possibleActions) {
		for (MacroAction macroAction : possibleActions) {
			Utils.logInfo(macroAction.toString());
		}
	}

	@Override
	public void displayText(String text) {

	}

	@Override
	public void displayGame() {

	}

	@Override
	public void askAction() {

	}

	@Override
	public void showTargettablePlayers() {

	}

	@Override
	public void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate) {
		client.sendMessage(gameBoardRepToUpdate);
	}

	@Override
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {
		client.sendMessage(gameMapRepToUpdate);
	}

	@Override
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {
		client.sendMessage(playerRepToUpdate);
	}

	@Override
	public void showMessage(String stringToShow) {

	}


	private class GameBoardObserver implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			Utils.logInfo("Game Board Rep Created.");
			updateGameBoardRep(new GameBoardRep((GameBoard) observable));
		}
	}

	private class GameMapObserver implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			Utils.logInfo("Map Rep Created.");
			updateGameMapRep(new GameMapRep((GameMap) observable));
		}
	}

	private class PlayerObserver implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			Utils.logInfo("Player Rep Created.");
			updatePlayerRep(new PlayerRep((Player) observable));
		}
	}
}