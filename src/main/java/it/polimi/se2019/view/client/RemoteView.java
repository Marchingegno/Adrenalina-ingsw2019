package it.polimi.se2019.view.client;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.client.ConnectionToServerInterface;
import it.polimi.se2019.network.client.MessageReceiverInterface;
import it.polimi.se2019.network.client.rmi.RMIClient;
import it.polimi.se2019.network.client.socket.ClientSocket;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.ViewInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public abstract class RemoteView implements ViewInterface, MessageReceiverInterface {

	private ConnectionToServerInterface connectionToServer;

	/**
	 * Receive and process the message sent by the server both by socket or by RMI.
	 * @param message the message received.
	 */
	@Override
	public synchronized void processMessage(Message message) {
		switch (message.getMessageType()) {
			case NICKNAME:
				if(message.getMessageSubtype() == MessageSubtype.REQUEST)
					askNickname();
				if(message.getMessageSubtype() == MessageSubtype.ERROR)
					askNicknameError();
				if(message.getMessageSubtype() == MessageSubtype.OK) {
					String nickname = ((NicknameMessage)message).getContent();
					nicknameIsOk(nickname);
				}
				break;
			case WAITING_PLAYERS:
				if(message.getMessageSubtype() == MessageSubtype.INFO) {
					StringMessage stringMessage = (StringMessage) message;
					displayWaitingPlayers(stringMessage.getContent());
				}
				break;
			case TIMER_FOR_START:
				if(message.getMessageSubtype() == MessageSubtype.INFO) {
					TimerForStartMessage timerForStartMessage = (TimerForStartMessage) message;
					displayTimerStarted(timerForStartMessage.getDelayInMs());
				}
				if(message.getMessageSubtype() == MessageSubtype.ERROR) {
					displayTimerStopped();
				}
				break;
			case GAME_CONFIG:
				if(message.getMessageSubtype() == MessageSubtype.REQUEST)
					askMapAndSkullsToUse();
				if(message.getMessageSubtype() == MessageSubtype.OK) {
					GameConfigMessage gameConfigMessage = (GameConfigMessage) message;
					showMapAndSkullsInUse(gameConfigMessage.getSkulls(), GameConstants.MapType.values()[gameConfigMessage.getMapIndex()]);
				}
				break;
			case GAME_MAP_REP:
				if (message.getMessageSubtype() == MessageSubtype.INFO){
					updateGameMapRep((GameMapRep) message);
					Utils.logInfo("Updated Game Map rep");
				}
				break;
			case GAME_BOARD_REP:
				if (message.getMessageSubtype() == MessageSubtype.INFO){
					updateGameBoardRep((GameBoardRep) message);
					Utils.logInfo("Updated Game Board rep");
				}
				break;
			case PLAYER_REP:
				if (message.getMessageSubtype() == MessageSubtype.INFO){
					updatePlayerRep((PlayerRep) message);
					Utils.logInfo("Updated " + ((PlayerRep) message).getPlayerName() + " rep");
				}
				break;
			case EXAMPLE_ACTION: // TODO remove
				if (message.getMessageSubtype() == MessageSubtype.REQUEST)
					askActionExample(); // This method will be processed by the CLI or by the GUI.
				break;
			case ACTION:
				askAction();
				break;
			default:
				Utils.logInfo("Received an unrecognized message of type " + message.getMessageType() + " and subtype: " + message.getMessageSubtype() + ".");
				break;
		}
	}

	/**
	 * Called when the connection with the server (by socket or by RMI) has been lost.
	 */
	@Override
	public void lostConnection() {
		lostConnectionToServer();
	}

	/**
	 * Start a connection with the server, using RMI.
	 */
	public void startConnectionWithRMI() {
		try {
			connectionToServer = new RMIClient(this);
		} catch (RemoteException | NotBoundException e) {
			Utils.logError("Failed to connect to the server.", e);
			failedConnectionToServer();
		}
	}

	/**
	 * Start a connection with the server, using socket.
	 */
	public void startConnectionWithSocket() {
		try {
			connectionToServer = new ClientSocket(this);
		} catch (IOException e) {
			Utils.logError("Failed to connect to the server.", e);
			failedConnectionToServer();
		}
	}

	/**
	 * Sends a message to the server.
	 * @param message the message to send.
	 */
	public void sendMessage(Message message) {
		if(connectionToServer == null)
			throw new IllegalStateException("Before sending any message, a connection must be started!");
		connectionToServer.sendMessage(message);
	}

	public abstract void askForConnectionAndStartIt();

	public abstract void failedConnectionToServer();

	public abstract void lostConnectionToServer();

	public abstract void askNickname();

	public abstract void askNicknameError();

	public abstract void nicknameIsOk(String nickname);

	public abstract void displayWaitingPlayers(String waitingPlayers);

	public abstract void displayGame();

	public abstract void displayTimerStarted(long delayInMs);

	public abstract void displayTimerStopped();

	public abstract void askMapAndSkullsToUse();

	public abstract void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType);
}
