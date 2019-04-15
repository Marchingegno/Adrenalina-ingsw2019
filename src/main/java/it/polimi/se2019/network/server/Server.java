package it.polimi.se2019.network.server;

import it.polimi.se2019.network.message.*;
import it.polimi.se2019.network.client.ClientInterface;
import it.polimi.se2019.network.server.rmi.RMIServer;
import it.polimi.se2019.network.server.rmi.RMIServerInterface;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

	private final static int NICKNAME_MAX_LENGTH = 16;

	private RMIServerInterface rmiServer;
	private ArrayList<ClientInterface> clients;
	private HashMap<ClientInterface, String> clientNickanames;


	public static void main(String[] args) {
		Server server = new Server();
		server.startRMIServer();
		server.startSocketServer();
	}


	public Server() {
		clients = new ArrayList<>();
		clientNickanames = new HashMap<>();
	}


	public void onRegisterClient(ClientInterface client) {
		// TODO wait for other clients and start the game when ready
		clients.add(client);
		Utils.logInfo("Registered new client.");
		asyncSendMessage(client, new Message(MessageType.NICKNAME, MessageSubtype.REQUEST));
	}

	public void onReceiveMessage(ClientInterface client, Message message) {
		// TODO based on the message send it to the controller (or remoteview?)

		Utils.logInfo("Received message of type: " + message.getMessageType() + ", and subtype: " + message.getMessageSubtype() + ".");
		switch (message.getMessageType()) {
			case NICKNAME:
				if(message.getMessageSubtype() == MessageSubtype.ANSWER) {
					String nickname = ((NicknameMessage) message).getContent().replaceAll("\\s","");
					int maxLength = (nickname.length() < NICKNAME_MAX_LENGTH) ? nickname.length() : NICKNAME_MAX_LENGTH;
					nickname = nickname.substring(0, maxLength);
					clientNickanames.put(client, nickname); // TODO check if nickname isn't already used
					asyncSendMessage(client, new NicknameMessage(nickname, MessageSubtype.OK));
				}
				break;

			default:
				break;

		}
	}

	private void asyncSendMessage(ClientInterface client, Message message) {
		new Thread(() -> {
			try {
				client.processMessage(message);
			} catch (RemoteException e) {
				Utils.logError("Error while sending a message asynchronously.", e);
			}
		}).start();
	}

	private void startRMIServer() {
		try {
			rmiServer = new RMIServer(this);
		} catch (RemoteException e) {
			Utils.logError("Failed to start RMI server.", e);
		}
	}

	private void startSocketServer() {
		// TODO
	}

}
