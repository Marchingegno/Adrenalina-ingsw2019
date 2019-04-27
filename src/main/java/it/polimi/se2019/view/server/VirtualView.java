package it.polimi.se2019.view.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.GameBoard;
import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.message.IntMessage;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
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
	private String playerName;

	public VirtualView(Controller controller, ConnectionToClientInterface client, String playerName) {
		this.client = client;
		this.playerName = playerName;
		this.controller = controller;

		this.controller.getModel().getGameBoard().addObserver(new GameBoardObserver());
		Utils.logInfo(playerName + " now observes Game Board");
		this.controller.getModel().getGameBoard().getGameMap().addObserver(new GameMapObserver());
		Utils.logInfo(playerName + " now observes Game Map");
		for (Player player : this.controller.getModel().getPlayers()) {
			player.addObserver(new PlayerObserver());
			Utils.logInfo(playerName + " now observes " + player.getPlayerName());
		}
	}

	public void onMessageReceived(Message message) {
		switch (message.getMessageType()) {
			case EXAMPLE_ACTION: // TODO remove
				if (message.getMessageSubtype() == MessageSubtype.ANSWER) {
					IntMessage intMessage = (IntMessage) message;
					int answer = intMessage.getContent();
					Utils.logInfo("Received answer for Example Action: " + answer + ".");
					//controller.doExampleAction(answer);
				}
				break;
			default:
				Utils.logError("Message of type " + message.getMessageType() + " not recognized!", new IllegalArgumentException("Message of type " + message.getMessageType() + " not recognized"));
				break;

		}
	}

	public void onClientDisconnected() {
		// TODO inform controller/model and supend the player
	}

	// TODO remove
	@Override
	public void askActionExample() { // This method in overridden from the ViewInterface.
		// Send a message that represents asking the "example action". The client will process it in the RemoteView class.
		client.sendMessage(new Message(MessageType.EXAMPLE_ACTION, MessageSubtype.REQUEST));
	}

	@Override
	public void displayPossibleActions(List<MacroAction> possibleActions) {
		for (MacroAction macroAction : possibleActions) {
			Utils.logInfo(macroAction.toString());
		}
		// TODO send a message with the possible actions
	}

	@Override
	public void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate) {
		Utils.logInfo("Sending Game Board rep to " + playerName);
		client.sendMessage(gameBoardRepToUpdate);
	}

	@Override
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {
		Utils.logInfo("Sending Game Map rep to " + playerName);
		client.sendMessage(gameMapRepToUpdate);
	}

	@Override
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {
		Utils.logInfo("Sending Player rep to " + playerName);
		client.sendMessage(playerRepToUpdate);
	}


	private class GameBoardObserver implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			updateGameBoardRep(((GameBoard) observable).getRep());
		}
	}

	private class GameMapObserver implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			updateGameMapRep(((GameMap) observable).getRep());
		}
	}

	private class PlayerObserver implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			updatePlayerRep(((Player) observable).getRep(playerName));
		}
	}
}