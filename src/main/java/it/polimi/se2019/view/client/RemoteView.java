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
import it.polimi.se2019.utils.Pair;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.ViewInterface;

import java.util.List;
import java.util.UUID;

public abstract class RemoteView implements ViewInterface, MessageReceiverInterface {

	private ConnectionToServerInterface connectionToServer;
	private String nickname;
	private ModelRep modelRep = new ModelRep();


	/**
	 * Receive and process the message sent by the server both by socket or by RMI.
	 * @param message the message received.
	 */
	@Override // Of MessageReceiverInterface.
	public synchronized void processMessage(Message message) {
		Utils.logInfo("RemoteView -> processMessage(): Received a message of type: " + message.getMessageType() + ", and subtype: " + message.getMessageSubtype() + ".");
		switch (message.getMessageType()) {
			case NICKNAME:
				if(message.getMessageSubtype() == MessageSubtype.REQUEST) {
					if (Utils.DEBUG_BYPASS_CONFIGURATION) {
						String randomNickname = UUID.randomUUID().toString().substring(0, 3).replace("-", "");
						sendMessage(new NicknameMessage(randomNickname, MessageSubtype.ANSWER));
						return;
					}
					askNickname();
				}
				if(message.getMessageSubtype() == MessageSubtype.ERROR)
					askNicknameError();
				if(message.getMessageSubtype() == MessageSubtype.OK) {
					String nickname = ((NicknameMessage)message).getContent();
					this.nickname = nickname;
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
				if(message.getMessageSubtype() == MessageSubtype.REQUEST) {
					if(Utils.DEBUG_BYPASS_CONFIGURATION){
						GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.ANSWER);
						gameConfigMessage.setMapIndex(0);
						gameConfigMessage.setSkulls(5);
						sendMessage(gameConfigMessage);
						return;
					}
					askMapAndSkullsToUse();
				}
				if(message.getMessageSubtype() == MessageSubtype.OK) {
					GameConfigMessage gameConfigMessage = (GameConfigMessage) message;
					showMapAndSkullsInUse(gameConfigMessage.getSkulls(), GameConstants.MapType.values()[gameConfigMessage.getMapIndex()]);
				}
				break;
			case UPDATE_REPS:
				if (message.getMessageSubtype() == MessageSubtype.INFO){
					Utils.logInfo("\tRemoteView -> processMessage(): Updating reps.");
					RepMessage repMessage = (RepMessage) message;
					updateReps(repMessage);
					if (repMessage.getMessage() != null)
						processMessage(repMessage.getMessage());
				}
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
				askMove(((MoveActionMessage) message).getCoordinates());
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
			case WEAPON:
				String question = ((AskOptionsMessage)message).getQuestion();
				List<String> options = ((AskOptionsMessage)message).getOptions();
				askChoice(question, options);
			default:
				Utils.logInfo("Received an unrecognized message of type " + message.getMessageType() + " and subtype: " + message.getMessageSubtype() + ".");
				break;
		}
	}

	@Override // Of ViewInterface.
	public String getNickname() {
		if(nickname == null)
			Utils.logWarning("Attribute nickname is null.");
		return nickname;
	}

	@Override // Of ViewInterface.
	public void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate) {
		if (gameBoardRepToUpdate != null)
			modelRep.setGameBoardRep(gameBoardRepToUpdate);
	}

	@Override // Of ViewInterface.
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {
		if (gameMapRepToUpdate != null)
			modelRep.setGameMapRep(gameMapRepToUpdate);
	}

	@Override // Of ViewInterface.
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {
		if (playerRepToUpdate != null)
			modelRep.setPlayerRep(playerRepToUpdate);
	}

	/**
	 * Called when the connection with the server (by socket or by RMI) has failed.
	 */
	@Override // Of MessageReceiverInterface.
	public void failedConnection() {
		failedConnectionToServer();
	}

	/**
	 * Called when the connection with the server (by socket or by RMI) has been lost.
	 */
	@Override // Of MessageReceiverInterface.
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

	public ModelRep getModelRep() {
		return modelRep;
	}

	public void updateReps(RepMessage repMessage) {
		updateGameMapRep(repMessage.getGameMapRep());
		updateGameBoardRep(repMessage.getGameBoardRep());
		for (PlayerRep playerRep : repMessage.getPlayersRep()) {
			updatePlayerRep(playerRep);
		}
		updateDisplay();
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


	public abstract void updateDisplay();

	public abstract void askForConnectionAndStartIt();

	public abstract void failedConnectionToServer();

	public abstract void lostConnectionToServer();

	public abstract void askNickname();

	public abstract void askNicknameError();

	public abstract void displayWaitingPlayers(List<String> waitingPlayers);

	public abstract void displayTimerStarted(long delayInMs);

	public abstract void displayTimerStopped();

	public abstract void askMapAndSkullsToUse();

	public abstract void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType);
}
