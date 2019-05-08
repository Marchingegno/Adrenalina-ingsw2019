package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.rmi.RMIClientInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.AbstractConnectionToClient;
import it.polimi.se2019.network.server.ServerEventsListenerInterface;
import it.polimi.se2019.utils.Utils;

/**
 * Represents an RMI client in the server. By using this class the server can send messages to the client and listen to a lost of connection.
 * @author Desno365
 */
public class ServerClientRMI extends AbstractConnectionToClient {

	private ServerEventsListenerInterface serverEventsListener;
	private RMIClientInterface rmiClientInterface;


	public ServerClientRMI(ServerEventsListenerInterface serverEventsListener, RMIClientInterface rmiClientInterface) {
		this.serverEventsListener = serverEventsListener;
		this.rmiClientInterface = rmiClientInterface;
	}


	/**
	 * Send a message to the client.
	 * @param message the message to send.
	 */
	@Override
	public void sendMessage(Message message) {
		// Send the message in a thread so the server isn't put in wait.
		new Thread(() -> {
			try {
				rmiClientInterface.receiveMessage(message);
			} catch (Exception e) {
				Utils.logError("RMI: send message to client failed.", e);
			}
		}, "CUSTOM: RMI Message Sending").start();
	}


	/**
	 * Starts the thread that listens for a connection lost with the client.
	 * If a lost of connection is found reports it to the event listener.
	 */
	public void startConnectionListener() {
		new Thread(() -> {
			try {
				rmiClientInterface.connectionListenerSubjectInClient();
			} catch (Exception e) {
				Utils.logError("Connection lost for an exception.", e);
			} finally {
				serverEventsListener.onConnectionLost(this);
			}
		}, "CUSTOM: RMI Connection Listener").start();
	}
}
