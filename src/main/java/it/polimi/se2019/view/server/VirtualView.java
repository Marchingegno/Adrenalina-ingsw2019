package it.polimi.se2019.view.server;

import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.network.server.AbstractConnectionToClient;
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

	public void onMessageReceived(Message message) {
		Utils.logInfo("\tThe VirtualView of " + getNickname() + "\" is processing a message of type: " + message.getMessageType() + ", and subtype: " + message.getMessageSubtype() + ".");

		setChanged();
		notifyObservers(new Event(this, message)); // Attach the VirtualView itself to the Event sent to Observer(s) (Controller).
	}

	public void onClientDisconnected() {
		// TODO inform controller/model and suspend the player
	}

	public void sendReps() {
		sendMessage(null); // if i want to send only the reps the message must be null
	}


	@Override
	public String getNickname() {
		return client.getNickname();
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
	public void askMove(List<Coordinates> reachableCoordinates) {
		sendMessage(new MoveActionMessage(reachableCoordinates, MessageSubtype.REQUEST));
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
	public void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate) {
		repMessage.addGameBoardRep(gameBoardRepToUpdate);
		Utils.logInfo("Added to " + getNickname() + "'s packet the Game Board rep");
	}

	@Override
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {
		repMessage.addGameMapRep(gameMapRepToUpdate);
		Utils.logInfo("Added to " + getNickname() + "'s packet the Game Board rep");
	}

	@Override
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {
		repMessage.addPlayersRep(playerRepToUpdate);
		Utils.logInfo("Added to " + getNickname() + "'s packet the Player rep of " + playerRepToUpdate.getPlayerName());
	}


	private void sendMessage(Message message) {
		if (repMessage.hasReps()) {
			Utils.logInfo("VirtualView -> sendMessage(): sending the reps with the message for " + getNickname() + ".");
			repMessage.addMessage(message);
			client.sendMessage(repMessage);
			repMessage = new RepMessage();
		} else if(message != null) {
			Utils.logInfo("VirtualView -> sendMessage(): no reps to send for " + getNickname() + ".");
			client.sendMessage(message);
		} else {
			Utils.logInfo("VirtualView -> sendMessage(): nothing to send for " + getNickname() + " (null message and no reps).");
		}
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
			updatePlayerRep((PlayerRep) ((Player) observable).getRep(getNickname()));
		}
	}
}