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
	private RepMessage repMessage;

	public VirtualView(AbstractConnectionToClient client) {
		repMessage = new RepMessage();
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
		Utils.logInfo("\tThe VirtualView of " + getPlayerName() + "\" is processing a message of type: " + message.getMessageType() + ", and subtype: " + message.getMessageSubtype() + ".");
		Event event = new Event(this, message);
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
				Utils.logInfo("Virtual View : Received a message of type " + message.getMessageType().toString() + " and subtype " + message.getMessageSubtype().toString());
				setChanged();
				notifyObservers(event);
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

	public void sendReps() {
		sendMessage(null); // if i want to send only the reps the message must be null
	}

	@Override
	public void askAction() {
		sendMessage(new Message(MessageType.ACTION, MessageSubtype.REQUEST));
	}

	@Override
	public void askGrab() {
		sendMessage(new Message(MessageType.GRAB_AMMO, MessageSubtype.REQUEST));
	}

	@Override
	public void askMove() {
		sendMessage(new Message(MessageType.MOVE, MessageSubtype.REQUEST));
	}

	@Override
	public void askShoot() {
		sendMessage(new Message(MessageType.SHOOT, MessageSubtype.REQUEST));
	}

	@Override
	public void askReload() {
		sendMessage(new Message(MessageType.RELOAD, MessageSubtype.REQUEST));
	}

	@Override
	public void askSpawn() {
		sendMessage(new Message(MessageType.SPAWN, MessageSubtype.REQUEST));
	}

	@Override
	public void askEnd() {
		sendMessage(new Message(MessageType.END_TURN, MessageSubtype.REQUEST));
	}


	@Override
	public void displayPossibleActions(List<MacroAction> possibleActions) {
		for (MacroAction macroAction : possibleActions) {
			Utils.logInfo(macroAction.toString());
		}
		// TODO send a message with the possible actions
	}

	private void sendMessage(Message message) {
		if (repMessage.hasReps()) {
			repMessage.addMessage(message);
			client.sendMessage(repMessage);
			repMessage = new RepMessage();
		} else
			client.sendMessage(message);
	}

	@Override
	public void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate) {
		repMessage.addGameBoardRep(gameBoardRepToUpdate);
		Utils.logInfo("Added to " + getPlayerName() + "'s packet the Game Board rep");
	}

	@Override
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {
		repMessage.addGameMapRep(gameMapRepToUpdate);
		Utils.logInfo("Added to " + getPlayerName() + "'s packet the Game Board rep");
	}

	@Override
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {
		repMessage.addPlayersRep(playerRepToUpdate);
		Utils.logInfo("Added to " + getPlayerName() + "'s packet the Player rep of " + playerRepToUpdate.getPlayerName());
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