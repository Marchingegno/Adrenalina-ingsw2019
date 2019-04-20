package it.polimi.se2019.network.client;

import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.client.rmi.RMIClient;
import it.polimi.se2019.network.client.socket.ClientSocket;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.CLIView;
import it.polimi.se2019.view.GUIView;
import it.polimi.se2019.view.ViewInterface;

import java.util.Scanner;


/**
 * Implements the Client.
 * @author Desno365
 * @author MarcerAndrea
 */
public class Client {

	private ConnectionToServerInterface clientMessageSender;
	private ViewInterface view;


	//TEMP
	private static Scanner scanner;


	public static void main(String[] args) {
		Client client;

		//TEMP
		scanner = new Scanner(System.in);

		System.out.println("You want CLI? [true/false]");
		boolean isCLI = Boolean.parseBoolean(scanner.nextLine()); // TODO if user requested CLI use CLI otherwise GUI
		if(isCLI)
			client = new Client(new CLIView());
		else
			client = new Client(new GUIView());


		System.out.println("You want RMI? [true/false]");
		boolean isRMI = Boolean.parseBoolean(scanner.nextLine()); // TODO if user requested RMI start RMI otherwise socket
		if(isRMI)
			client.startConnectionWithRMI();
		else
			client.startConnectionWithSocket();
	}


	/**
	 * Create a new client and associate it with the view.
	 * @param view the view to be associated with the client.
	 */
	private Client(ViewInterface view) {
		this.view = view;
	}


	/**
	 * Receive and process the message sent by the server both by socket or by RMI.
	 * @param message the message received.
	 */
	// TODO this is garbage
	@Deprecated
	public synchronized void processMessage(Message message) {
		switch (message.getMessageType()) {
			case NICKNAME:
				if(message.getMessageSubtype() == MessageSubtype.REQUEST) {
					String nickname = view.askNickname();
					clientMessageSender.sendMessage(new NicknameMessage(nickname, MessageSubtype.ANSWER));
				}
				if(message.getMessageSubtype() == MessageSubtype.ERROR) {
					view.displayText("The nickname already exists or is not valid, please use a different one.");
					String nickname = view.askNickname();
					clientMessageSender.sendMessage(new NicknameMessage(nickname, MessageSubtype.ANSWER));
				}
				if(message.getMessageSubtype() == MessageSubtype.OK) {
					String nickname = ((NicknameMessage)message).getContent();
					view.displayText("Nickname set to: \"" + nickname + "\".");
				}
				break;
			case WAITING_PLAYERS:
				if(message.getMessageSubtype() == MessageSubtype.INFO) {
					StringMessage stringMessage = (StringMessage) message;
					view.displayWaitingPlayers(stringMessage.getContent());
				}
				break;
			case TIMER_FOR_START:
				if(message.getMessageSubtype() == MessageSubtype.INFO) {
					TimerForStartMessage timerForStartMessage = (TimerForStartMessage) message;
					view.displayTimerStarted(timerForStartMessage.getDelayInMs());
				}
				if(message.getMessageSubtype() == MessageSubtype.ERROR) {
					view.displayText("Timer for starting the match cancelled.");
				}
				break;
			case GAME_CONFIG:
				if(message.getMessageSubtype() == MessageSubtype.REQUEST) {
					view.displayText("\n\nMatch ready to start. Select your preferred configuration.");
					int mapIndex = view.askMapToUse();
					int skulls = view.askSkullsForGame();
					view.displayText("Waiting for other clients to answer...");
					GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.ANSWER);
					gameConfigMessage.setMapIndex(mapIndex);
					gameConfigMessage.setSkulls(skulls);
					clientMessageSender.sendMessage(gameConfigMessage);
				}
				if(message.getMessageSubtype() == MessageSubtype.OK) {
					GameConfigMessage gameConfigMessage = (GameConfigMessage) message;
					view.displayText("Average of voted skulls: " + gameConfigMessage.getSkulls());
					view.displayText("Most voted map: " + GameConstants.MapType.values()[gameConfigMessage.getMapIndex()].getDescription());
					view.displayText("Match started!");
					view.displayGame();
				}
				break;
			case GAME_MAP_REP:
				if (message.getMessageSubtype() == MessageSubtype.INFO){
					view.updateGameMapRep((GameMapRep) message);
				}
				break;
			case GAME_BOARD_REP:
				if (message.getMessageSubtype() == MessageSubtype.INFO){
					view.updateGameBoardRep((GameBoardRep) message);
				}
				break;
			case PLAYER_REP:
				if (message.getMessageSubtype() == MessageSubtype.INFO){
					view.updatePlayerRep((PlayerRep) message);
				}
				break;
		}
	}

	/**
	 * Start a connection with the server, using RMI.
	 */
	public void startConnectionWithRMI() {
		try {
			clientMessageSender = new RMIClient(this);
		} catch (Exception e) {
			Utils.logError("Failed to connect to the server.", e);
		}
	}

	/**
	 * Start a connection with the server, using socket.
	 */
	public void startConnectionWithSocket() {
		clientMessageSender = new ClientSocket(this);
	}
}
