package it.polimi.se2019.network.client;

import it.polimi.se2019.network.client.rmi.RMIClient;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.NicknameMessage;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.CLIView;
import it.polimi.se2019.view.GUIView;
import it.polimi.se2019.view.RemoteViewInterface;

import java.rmi.RemoteException;

public class Client implements ClientInterface{

	private ConnectionInterface connection;
	private RemoteViewInterface view;


	public static void main(String[] args) {
		Client client;

		boolean isCLI = true; // TODO if user requested CLI use CLI otherwise GUI
		if(isCLI)
			client = new Client(new CLIView());
		else
			client = new Client(new GUIView());

		boolean isRMI = true; // TODO if user requested RMI start RMI otherwise socket
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
		// TODO process the message and update the view

		try {
			switch (message.getMessageType()) {
				case NICKNAME:
					if(message.getMessageSubtype() == MessageSubtype.REQUEST) {
						String nickname = view.askNickname();
						connection.sendMessage(new NicknameMessage(nickname, MessageSubtype.ANSWER));
					}
					if(message.getMessageSubtype() == MessageSubtype.ERROR) {
						String nickname = view.askNickname();
						connection.sendMessage(new NicknameMessage(nickname, MessageSubtype.ANSWER));
					}
					if(message.getMessageSubtype() == MessageSubtype.OK) {
						String nickname = ((NicknameMessage)message).getContent();
						view.displayText("Nickname set to: \"" + nickname + "\".");
					}
					break;

				default:
					break;

			}
		} catch (RemoteException e) {
			Utils.logError("Error while sending a message to the server.", e);
		}
	}

	/**
	 * Start a connection with the server, using RMI.
	 */
	public void startConnectionWithRMI() {
		try {
			connection = new RMIClient(this);
			connection.registerClient();
		} catch (Exception e) {
			Utils.logError("Failed connection to server.", e);
		}
	}

	/**
	 * Start a connection with the server, using socket.
	 */
	public void startConnectionWithSocket() {
		// TODO create a socket connection and save it in the attribute "connection"
	}
}
