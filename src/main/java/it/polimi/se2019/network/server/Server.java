package it.polimi.se2019.network.server;

import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.client.ClientInterface;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.network.message.UpperCaseInputMessage;
import it.polimi.se2019.network.message.UpperCaseOutputMessage;
import it.polimi.se2019.network.server.rmi.RMIServer;
import it.polimi.se2019.network.server.rmi.RMIServerInterface;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Server {

	private RMIServerInterface rmiServer;
	private ArrayList<ClientInterface> clients;


	public static void main(String[] args) {

		Server server = new Server();
		server.startRMIServer();
		server.startSocketServer();
	}


	public Server() {
		clients = new ArrayList<>();
	}


	public void onRegisterClient(ClientInterface client) {
		// TODO save the client, wait for other clients and start the game when ready

		// ********** JUST FOR TEST ************
		try {
			clients.add(client);
			Utils.logInfo("Registered new client.");
			client.processMessage(new Message(MessageType.REQUEST_FOR_UPPER_CASE));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void onReceiveMessage(Message message) {
		// TODO based on the message send it to the controller (or remoteview?)


		// ********** JUST FOR TEST ************
		if(message.getMessageType() == MessageType.INPUT_FOR_UPPER_CASE) {
			String messageString = ((UpperCaseInputMessage) message).getContent();
			for (ClientInterface client : clients) {
				asyncSendMessage(client, new UpperCaseOutputMessage(messageString.toUpperCase()));
				asyncSendMessage(client, new Message(MessageType.REQUEST_FOR_UPPER_CASE));
			}
		}
	}

	public void asyncSendMessage(ClientInterface client, Message message) {
		new Thread(() -> {
			try {
				client.processMessage(message);
			} catch (RemoteException e) {
				Utils.logError("Error while sending a message asynchronously.", e);
			}
		}).start();
	}

	public void startRMIServer() {
		try {
			rmiServer = new RMIServer(this);
		} catch (RemoteException e) {
			Utils.logError("Failed to start RMI server.", e);
		}
	}

	public void startSocketServer() {
		// TODO
	}

}
