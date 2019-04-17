package it.polimi.se2019.network.client;

import it.polimi.se2019.network.ConnectionInterface;
import it.polimi.se2019.network.client.rmi.RMIClient;
import it.polimi.se2019.network.client.socket.ClientSocket;
import it.polimi.se2019.network.message.GameConfigMessage;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.NicknameMessage;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.CLIView;
import it.polimi.se2019.view.GUIView;
import it.polimi.se2019.view.RemoteViewInterface;



//TEMP
import java.util.Scanner;


/**
 * Implements the Client.
 * @author Desno365
 * @author MarcerAndrea
 */
public class Client implements ConnectionInterface {

	private ClientMessageSenderInterface clientMessageSender;
	private RemoteViewInterface view;


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
	private Client(RemoteViewInterface view) {
		this.view = view;
	}


	/**
	 * Receive and process the message sent by the server both by socket or by RMI.
	 * @param message the message received.
	 */
	@Override
	public void processMessage(Message message) {
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
					view.displayText("Waiting for other clients to connect...");
				}
				break;
			case GAME_CONFIG:
				if(message.getMessageSubtype() == MessageSubtype.REQUEST) {
					int mapIndex = view.askMapToUse();
					int skulls = view.askSkullsForGame();
					GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.ANSWER);
					gameConfigMessage.setMapIndex(mapIndex);
					gameConfigMessage.setSkulls(skulls);
					clientMessageSender.sendMessage(gameConfigMessage);
					view.displayText("Waiting for other clients to answer...");
				}
				if(message.getMessageSubtype() == MessageSubtype.OK) {
					GameConfigMessage gameConfigMessage = (GameConfigMessage) message;
					view.displayText("Average of voted skulls: " + gameConfigMessage.getSkulls());
					view.displayText("Most voted map: " + GameConstants.MapType.values()[gameConfigMessage.getMapIndex()].getDescription());
					view.displayText("Match started!");
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
			clientMessageSender.registerClient();
		} catch (Exception e) {
			Utils.logError("Failed to connect to the server.", e);
		}
	}

	/**
	 * Start a connection with the server, using socket.
	 */
	public void startConnectionWithSocket() {
		clientMessageSender = new ClientSocket(this);
		clientMessageSender.registerClient();
	}
}
