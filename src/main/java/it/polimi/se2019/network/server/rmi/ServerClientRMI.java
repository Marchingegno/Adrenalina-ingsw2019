package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.rmi.RMIClientInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.AbstractConnectionToClient;
import it.polimi.se2019.network.server.ServerMessageHandler;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;

public class ServerClientRMI extends AbstractConnectionToClient {

	private ServerMessageHandler serverMessageHandler;
	private RMIClientInterface rmiClientInterface;


	public ServerClientRMI(ServerMessageHandler serverMessageHandler, RMIClientInterface rmiClientInterface) {
		this.serverMessageHandler = serverMessageHandler;
		this.rmiClientInterface = rmiClientInterface;
	}


	@Override
	public void sendMessage(Message message) {
		// Send the message in a thread so the server isn't put in wait.
		new Thread(() -> {
			try {
				rmiClientInterface.receiveMessage(message);
			} catch (Exception e) {
				Utils.logInfo("Send message to client failed.");
			}
		}, "CUSTOM: RMI Message Sending").start();
	}

	public void startConnectionListener() {
		new Thread(() -> {
			try {
				rmiClientInterface.connectionListenerSubject();
			} catch (RemoteException | InterruptedException e) {
				Utils.logError("Connection lost.", e);
				serverMessageHandler.onConnectionLost(this);
			}
		}, "CUSTOM: RMI Connection Listener").start();
	}
}
