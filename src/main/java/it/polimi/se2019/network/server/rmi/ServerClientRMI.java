package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.rmi.RMIClientInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.AbstractConnectionToClient;
import it.polimi.se2019.network.server.ServerEventsListenerInterface;
import it.polimi.se2019.utils.Utils;

public class ServerClientRMI extends AbstractConnectionToClient {

	private ServerEventsListenerInterface serverEventsListener;
	private RMIClientInterface rmiClientInterface;


	public ServerClientRMI(ServerEventsListenerInterface serverEventsListener, RMIClientInterface rmiClientInterface) {
		this.serverEventsListener = serverEventsListener;
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
			} catch (Exception e) {
				Utils.logError("Connection lost.", e);
				serverEventsListener.onConnectionLost(this);
			}
		}, "CUSTOM: RMI Connection Listener").start();
	}
}
