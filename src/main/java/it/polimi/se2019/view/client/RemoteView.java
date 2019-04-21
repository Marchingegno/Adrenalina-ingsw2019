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
				if(message.getMessageSubtype() == MessageSubtype.REQUEST) {
					String nickname = askNickname();
					sendMessage(new NicknameMessage(nickname, MessageSubtype.ANSWER));
				}
				if(message.getMessageSubtype() == MessageSubtype.ERROR) {
					displayText("The nickname already exists or is not valid, please use a different one.");
					String nickname = askNickname();
					sendMessage(new NicknameMessage(nickname, MessageSubtype.ANSWER));
				}
				if(message.getMessageSubtype() == MessageSubtype.OK) {
					String nickname = ((NicknameMessage)message).getContent();
					displayText("Nickname set to: \"" + nickname + "\".");
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
					displayText("Timer for starting the match cancelled.");
				}
				break;
			case GAME_CONFIG:
				if(message.getMessageSubtype() == MessageSubtype.REQUEST) {
					displayText("\n\nMatch ready to start. Select your preferred configuration.");
					int mapIndex = askMapToUse();
					int skulls = askSkullsForGame();
					displayText("Waiting for other clients to answer...");
					GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.ANSWER);
					gameConfigMessage.setMapIndex(mapIndex);
					gameConfigMessage.setSkulls(skulls);
					sendMessage(gameConfigMessage);
				}
				if(message.getMessageSubtype() == MessageSubtype.OK) {
					GameConfigMessage gameConfigMessage = (GameConfigMessage) message;
					displayText("Average of voted skulls: " + gameConfigMessage.getSkulls());
					displayText("Most voted map: " + GameConstants.MapType.values()[gameConfigMessage.getMapIndex()].getDescription());
					displayText("Match started!");
					displayGame();
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

	public abstract String askNickname();

	public abstract void displayWaitingPlayers(String waitingPlayers);

	public abstract void displayTimerStarted(long delayInMs);

	public abstract int askMapToUse();

	public abstract int askSkullsForGame();
}
