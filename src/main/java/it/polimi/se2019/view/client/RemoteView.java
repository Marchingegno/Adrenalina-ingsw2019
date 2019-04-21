package it.polimi.se2019.view.client;

import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.client.ConnectionToServerInterface;
import it.polimi.se2019.network.client.MessageReceiverInterface;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.view.ViewInterface;

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
				}
				break;
			case GAME_BOARD_REP:
				if (message.getMessageSubtype() == MessageSubtype.INFO){
					updateGameBoardRep((GameBoardRep) message);
				}
				break;
			case PLAYER_REP:
				if (message.getMessageSubtype() == MessageSubtype.INFO){
					updatePlayerRep((PlayerRep) message);
				}
				break;
		}
	}

	public void setConnectionToServer(ConnectionToServerInterface connectionToServer) {
		this.connectionToServer = connectionToServer;
	}

	public void sendMessage(Message message) {
		if(connectionToServer == null)
			throw new IllegalStateException("Before sending any message, a connection must be set!");
		connectionToServer.sendMessage(message);
	}

	public abstract void askNickname();

	public abstract void askNicknameError();

	public abstract void nicknameIsOk(String nickname);

	public abstract void displayWaitingPlayers(String waitingPlayers);

	public abstract void displayTimerStarted(long delayInMs);

	public abstract void displayTimerStopped();

	public abstract void askMapAndSkullsToUse();

	public abstract void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType);
}
