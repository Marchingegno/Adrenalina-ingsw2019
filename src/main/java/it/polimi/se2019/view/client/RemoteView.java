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

import java.util.List;
import java.util.UUID;

/**
 * Remote views must extend this class.
 *
 * @author Marchingegno
 * @author Desno365
 * @author MArcerAndrea
 */
public abstract class RemoteView implements ViewInterface, MessageReceiverInterface {

	private ConnectionToServerInterface connectionToServer;
	private String nickname;
	private ModelRep modelRep = new ModelRep();


	/**
	 * Receive and process the message sent by the server both by socket or by RMI.
	 *
	 * @param message the message received.
	 */
	@Override // Of MessageReceiverInterface.
	public synchronized void processMessage(Message message) {
		Utils.logInfo("RemoteView -> processMessage(): Received a message of type: " + message.getMessageType() + ", and subtype: " + message.getMessageSubtype() + ".");
		switch (message.getMessageType()) {
			case NICKNAME:
				if (message.getMessageSubtype() == MessageSubtype.REQUEST) {
					if (Utils.DEBUG_BYPASS_CONFIGURATION) {
						String randomNickname = UUID.randomUUID().toString().substring(0, 3).replace("-", "");
						sendMessage(new NicknameMessage(randomNickname, MessageSubtype.ANSWER));
						return;
					}
					askNickname();
				}
				if (message.getMessageSubtype() == MessageSubtype.ERROR)
					askNicknameError();
				if (message.getMessageSubtype() == MessageSubtype.OK)
					this.nickname = ((NicknameMessage) message).getContent();
				break;
			case WAITING_PLAYERS:
				if (message.getMessageSubtype() == MessageSubtype.INFO) {
					WaitingPlayersMessage waitingPlayersMessage = (WaitingPlayersMessage) message;
					displayWaitingPlayers(waitingPlayersMessage.getWaitingPlayersNames());
				}
				break;
			case TIMER_FOR_START:
				if (message.getMessageSubtype() == MessageSubtype.INFO) {
					TimerForStartMessage timerForStartMessage = (TimerForStartMessage) message;
					displayTimerStarted(timerForStartMessage.getDelayInMs());
				}
				if (message.getMessageSubtype() == MessageSubtype.ERROR) {
					displayTimerStopped();
				}
				break;
			case GAME_CONFIG:
				if (message.getMessageSubtype() == MessageSubtype.REQUEST) {
					if (Utils.DEBUG_BYPASS_CONFIGURATION) {
						GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.ANSWER);
						gameConfigMessage.setMapIndex(0);
						gameConfigMessage.setSkulls(GameConstants.MIN_SKULLS);
						sendMessage(gameConfigMessage);
						return;
					}
					askMapAndSkullsToUse();
				}
				if (message.getMessageSubtype() == MessageSubtype.OK) {
					GameConfigMessage gameConfigMessage = (GameConfigMessage) message;
					showMapAndSkullsInUse(gameConfigMessage.getSkulls(), GameConstants.MapType.values()[gameConfigMessage.getMapIndex()]);
				}
				break;
			case UPDATE_REPS:
				if (message.getMessageSubtype() == MessageSubtype.INFO) {
					Utils.logInfo("\tRemoteView -> processMessage(): Updating reps.");
					RepMessage repMessage = (RepMessage) message;
					updateReps(repMessage);
					if (repMessage.getMessage() != null)
						processMessage(repMessage.getMessage());
				}
				break;
			case ACTION:
				if (message.getMessageSubtype() == MessageSubtype.REQUEST) {
					ActionRequestMessage arm = (ActionRequestMessage) message;
					askAction(arm.isActivablePowerups(), arm.isActivableWeapons());
				}
				break;
			case ACTIVATE_WEAPON:
				if (message.getMessageSubtype() == MessageSubtype.REQUEST)
					askShoot(((RequestChoiceInArrayMessage) message).getAvailableIndexes());
				break;
			case RELOAD:
				if (message.getMessageSubtype() == MessageSubtype.REQUEST) {
					askReload(((RequestChoiceInArrayMessage) message).getAvailableIndexes());
				}
				break;
			case MOVE:
				askMove(((CoordinatesRequestMessage) message).getCoordinates());
				break;
			case END_TURN:
				if (message.getMessageSubtype() == MessageSubtype.REQUEST)
					askEnd(((EndRequestMessage) message).isActivablePowerups());
				break;
			case GRAB_AMMO:
				sendMessage(new IntMessage(0, MessageType.GRAB_AMMO, MessageSubtype.ANSWER));
				break;
			case GRAB_WEAPON:
				askGrabWeapon(((RequestChoiceInArrayMessage) message).getAvailableIndexes());
				break;
			case SWAP_WEAPON:
				askSwapWeapon(((RequestChoiceInArrayMessage) message).getAvailableIndexes());
				break;
			case SPAWN:
				askSpawn();
				break;
			case PAYMENT:
				PaymentMessage paymentMessage = (PaymentMessage) message;
				askToPay(paymentMessage.getPriceToPay(), paymentMessage.canAffordAlsoWithAmmo());
				break;
			case WEAPON:
				if (message.getMessageSubtype() == MessageSubtype.REQUEST)
					askWeaponChoice(((AskOptionsMessage) message).getQuestionContainer());
				break;
			case ACTIVATE_ON_TURN_POWERUP:
				if (message.getMessageSubtype() == MessageSubtype.REQUEST) {
					askPowerupActivation(((RequestChoiceInArrayMessage) message).getAvailableIndexes());
				}
				break;
			case ACTIVATE_ON_DAMAGE_POWERUP:
				if (message.getMessageSubtype() == MessageSubtype.REQUEST) {
					askOnDamagePowerupActivation(((RequestChoiceWithExtraInfo) message).getAvailableIndexes(), ((RequestChoiceWithExtraInfo) message).getInfo());
				}
				break;
			case POWERUP:
				if (message.getMessageSubtype() == MessageSubtype.REQUEST)
					askPowerupChoice(((AskOptionsMessage) message).getQuestionContainer());
				break;
			case END_GAME:
				if (message.getMessageSubtype() == MessageSubtype.INFO)
					endOfGame(((EndGameMessage) message).getFinalPlayersInfo());
				break;
			default:
				Utils.logInfo("Received an unrecognized message of type " + message.getMessageType() + " and subtype: " + message.getMessageSubtype() + ".");
				break;
		}
	}

	@Override // Of ViewInterface.
	public String getNickname() {
		if (nickname == null)
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
	public void startConnectionWithRMI(String ipAddress) {
		connectionToServer = new RMIClient(this, ipAddress);
	}

	/**
	 * Start a connection with the server, using socket.
	 */
	public void startConnectionWithSocket(String ipAddress) {
		connectionToServer = new ClientSocket(this, ipAddress);
	}

	/**
	 * Sends a message to the server.
	 *
	 * @param message the message to send.
	 */
	public void sendMessage(Message message) {
		if (connectionToServer == null)
			throw new IllegalStateException("Before sending any message, a connection must be started!");
		connectionToServer.sendMessage(message);
	}


	protected ModelRep getModelRep() {
		return modelRep;
	}

	/**
	 * Updates the Representations contained in the RepMessage.
	 *
	 * @param repMessage the message containing the Representations.
	 */
	private void updateReps(RepMessage repMessage) {
		updateGameMapRep(repMessage.getGameMapRep());
		updateGameBoardRep(repMessage.getGameBoardRep());
		for (PlayerRep playerRep : repMessage.getPlayersRep()) {
			updatePlayerRep(playerRep);
		}
		Utils.logInfo("Reps Updated");
		updateDisplay();
	}

	/**
	 * Updates the display.
	 */
	public abstract void updateDisplay();

	/**
	 * Asks which type of connection the user would like to use.
	 */
	public abstract void askForConnectionAndStartIt();

	/**
	 * Called when the client failed the connection to the server.
	 */
	public abstract void failedConnectionToServer();

	/**
	 * Called when the client lost the connection to the server.
	 */
	public abstract void lostConnectionToServer();

	/**
	 * Asks to the user which nickname to use.
	 */
	public abstract void askNickname();

	/**
	 * Called when the user has entered a nickname already present in the server.
	 * It re-asks the user which nickname to use.
	 */
	public abstract void askNicknameError();

	/**
	 * Displays the waiting players in the lobby.
	 *
	 * @param waitingPlayers the waiting players.
	 */
	public abstract void displayWaitingPlayers(List<String> waitingPlayers);

	/**
	 * Tells the user that a timer has started.
	 *
	 * @param delayInMs the time left.
	 */
	public abstract void displayTimerStarted(long delayInMs);

	/**
	 * Tells the user that the timer has stopped.
	 */
	public abstract void displayTimerStopped();

	/**
	 * Asks to the user which map and how many skulls he would like to play with.
	 */
	public abstract void askMapAndSkullsToUse();

	/**
	 * Shows to the user which map and which skulls will be used for the game.
	 *
	 * @param skulls  the number of skulls.
	 * @param mapType the type of the map.
	 */
	public abstract void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType);
}
