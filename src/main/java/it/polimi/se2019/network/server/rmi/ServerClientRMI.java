package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.rmi.RMIClientInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.network.message.RepMessage;
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
	private boolean active;


	/**
	 * Create a new ServerClientRMI instance.
	 * @param serverEventsListener the event listener to which all events are forwarded.
	 * @param rmiClientInterface the client to associate with this instance.
	 */
	public ServerClientRMI(ServerEventsListenerInterface serverEventsListener, RMIClientInterface rmiClientInterface) {
		active = true;
		this.serverEventsListener = serverEventsListener;
		this.rmiClientInterface = rmiClientInterface;
		Utils.logInfo("ServerClientRMI => ServerClientRMI(): a new connection to a client has been created with RMI.");
	}


	/**
	 * Send a message to the client.
	 * @param message the message to send.
	 */
	@Override
	public void sendMessage(Message message) {
		Utils.logInfo("ServerClientSocket -> sendMessage(): sending a message to " + hashCode() + " " + message +
				(message.getMessageType().equals(MessageType.UPDATE_REPS) ? " inner message " + ((RepMessage) message).getMessage() : ""));
		// Send the message in a thread so the server isn't put in wait.
		new Thread(() -> {
			try {
				rmiClientInterface.receiveMessage(message);
			} catch (Exception e) {
				Utils.logError("RMI: send message to client failed.", e);
			}
		}, "CUSTOM: RMI Message Sending").start();
	}

	@Override
	public void closeConnectionWithClient() {
		active = false;
	}


	/**
	 * Starts the thread that listens for a connection lost with the client.
	 * If a lost of connection is found reports it to the event listener.
	 */
	public void startConnectionListener() {
		new Thread(() -> {
			try {
				rmiClientInterface.connectionListenerSubjectInClient();

				// Executed when the client closes the connection manually, without generating any error.
				Utils.logWarning("Connection closed by the client.");
				closeConnectionWithClient();
				serverEventsListener.onConnectionLost(this);
			} catch (Exception e) {
				Utils.logError("Lost connection with the client.", e);

				// Executed when the connection with the server has been lost.
				closeConnectionWithClient();
				serverEventsListener.onConnectionLost(this);
			}
		}, "CUSTOM: RMI Connection Listener").start();
	}

	/**
	 * Returns true if and only if the connection with the client is active.
	 * @return true if and only if the connection with the client is active.
	 */
	public boolean isConnectionActive() {
		return active;
	}

	/**
	 * Method to use as subject to check for connectivity change.
	 * @throws InterruptedException
	 */
	public synchronized void connectionListenerSubjectInServer() throws InterruptedException {
		while(isConnectionActive())
			wait();
	}
}
