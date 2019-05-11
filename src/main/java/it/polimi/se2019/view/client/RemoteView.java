package it.polimi.se2019.view.client;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.client.Client;
import it.polimi.se2019.network.client.ConnectionToServerInterface;
import it.polimi.se2019.network.client.MessageReceiverInterface;
import it.polimi.se2019.network.client.rmi.RMIClient;
import it.polimi.se2019.network.client.socket.ClientSocket;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.ViewInterface;

import java.util.List;

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
					WaitingPlayersMessage waitingPlayersMessage = (WaitingPlayersMessage) message;
					displayWaitingPlayers(waitingPlayersMessage.getWaitingPlayersNames());
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
					Utils.logInfo("RemoteView => processMessage(): Updating Game Map rep");
					updateGameMapRep((GameMapRep) message);
				}
				break;
			case GAME_BOARD_REP:
				if (message.getMessageSubtype() == MessageSubtype.INFO){
					Utils.logInfo("RemoteView => processMessage(): Updating Game Board rep");
					updateGameBoardRep((GameBoardRep) message);
				}
				break;
			case PLAYER_REP:
				if (message.getMessageSubtype() == MessageSubtype.INFO){
					Utils.logInfo("RemoteView => processMessage(): Updating " + ((PlayerRep) message).getPlayerName() + " rep");
					updatePlayerRep((PlayerRep) message);
				}
				break;
			case EXAMPLE_ACTION: // TODO remove
				if (message.getMessageSubtype() == MessageSubtype.REQUEST)
					askActionExample(); // This method will be processed by the CLI or by the GUI.
				break;
			case ACTION:
				askAction();
				break;
			case SHOOT:
				askShoot();
				break;
			case RELOAD:
				askReload();
				break;
			case MOVE:
				askMove();
				break;
			case END_TURN:
				askEnd();
				break;
			case GRAB_AMMO:
				askGrab();
				break;
			case GRAB_WEAPON:
				askGrab();
				break;
			case SPAWN:
				askSpawn();
				break;
			default:
				Utils.logInfo("Received an unrecognized message of type " + message.getMessageType() + " and subtype: " + message.getMessageSubtype() + ".");
				break;
		}
	}

	/**
	 * Called when the connection with the server (by socket or by RMI) has failed.
	 */
	@Override
	public void failedConnection() {
		failedConnectionToServer();
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
		connectionToServer = new RMIClient(this);
	}

	/**
	 * Start a connection with the server, using socket.
	 */
	public void startConnectionWithSocket() {
		connectionToServer = new ClientSocket(this);
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

	/**
	 * Closes the program.
	 */
	public void closeProgram() {
		Utils.logInfo("Closing the program...");
		if(connectionToServer != null)
			connectionToServer.closeConnectionWithServer();
		Client.terminateClient();
	}

	public abstract void askForConnectionAndStartIt();

	public abstract void failedConnectionToServer();

	public abstract void lostConnectionToServer();

	public abstract void askNickname();

	public abstract void askNicknameError();

	public abstract void nicknameIsOk(String nickname);

	public abstract void displayWaitingPlayers(List<String> waitingPlayers);

	public abstract void displayTimerStarted(long delayInMs);

	public abstract void displayTimerStopped();

	public abstract void askMapAndSkullsToUse();

	public abstract void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType);
}
