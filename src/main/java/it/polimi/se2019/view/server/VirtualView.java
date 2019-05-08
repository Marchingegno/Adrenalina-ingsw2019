package it.polimi.se2019.view.server;

import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.network.server.AbstractConnectionToClient;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.ViewInterface;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class VirtualView extends Observable implements ViewInterface {

	private AbstractConnectionToClient client;


	public VirtualView(AbstractConnectionToClient client) {
		this.client = client;
	}


	public GameBoardObserver getGameBoardObserver() {
		return new GameBoardObserver();
	}

	public GameMapObserver getGameMapObserver() {
		return new GameMapObserver();
	}

	public PlayerObserver getPlayerObserver() {
		return new PlayerObserver();
	}

	public String getPlayerName() {
		return client.getNickname();
	}

	public void onMessageReceived(Message message) {
		((ActionMessage)message).setVirtualView(this);
		switch (message.getMessageType()) {
			case EXAMPLE_ACTION: // TODO remove
				if (message.getMessageSubtype() == MessageSubtype.ANSWER) {
					IntMessage intMessage = (IntMessage) message;
					int answer = intMessage.getContent();
					Utils.logInfo("Received answer for Example Action: " + answer + ".");
					notifyObservers(/* MESSAGE HERE */);
				}
				break;
		default:
			Utils.logInfo("Received an " + message.getMessageType().toString() + "of subtype " + message.getMessageSubtype().toString());
			notifyObservers(message);
			//TODO: Uncomment. Utils.logError("Message of type " + message.getMessageType() + " not recognized!", new IllegalArgumentException("Message of type " + message.getMessageType() + " not recognized"));
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
	public void askAction() {
		client.sendMessage(new ActionMessage(MessageType.ACTION, MessageSubtype.REQUEST));
	}

	@Override
	public void askGrab() {
		client.sendMessage(new ActionMessage(MessageType.GRAB_AMMO, MessageSubtype.REQUEST));
	}

	@Override
	public void askMove() {
		client.sendMessage(new ActionMessage(MessageType.MOVE, MessageSubtype.REQUEST));
	}

	@Override
	public void askShoot() {
		client.sendMessage(new ActionMessage(MessageType.SHOOT, MessageSubtype.REQUEST));
	}

	@Override
	public void askReload() {
		client.sendMessage(new ActionMessage(MessageType.RELOAD, MessageSubtype.REQUEST));
	}

	@Override
	public void askSpawn() {
		client.sendMessage(new ActionMessage(MessageType.SPAWN, MessageSubtype.REQUEST));
	}

	@Override
	public void askEnd() {
		client.sendMessage(new ActionMessage(MessageType.END_TURN, MessageSubtype.REQUEST));
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
		Utils.logInfo("Sending Game Board rep to " + getPlayerName());
		client.sendMessage(gameBoardRepToUpdate);
	}

	@Override
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {
		Utils.logInfo("Sending Game Map rep to " + getPlayerName());
		client.sendMessage(gameMapRepToUpdate);
	}

	@Override
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {
		Utils.logInfo("Sending Player rep to " + getPlayerName());
		client.sendMessage(playerRepToUpdate);
	}


	private class GameBoardObserver implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			updateGameBoardRep((GameBoardRep) ((GameBoard) observable).getRep());
		}
	}

	private class GameMapObserver implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			updateGameMapRep((GameMapRep) ((GameMap) observable).getRep());
		}
	}

	private class PlayerObserver implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			updatePlayerRep((PlayerRep) ((Player) observable).getRep(getPlayerName()));
		}
	}
}