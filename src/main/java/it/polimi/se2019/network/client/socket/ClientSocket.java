package it.polimi.se2019.network.client.socket;

import it.polimi.se2019.network.client.ConnectionToServerInterface;
import it.polimi.se2019.network.client.MessageReceiverInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Socket that receives messages from the server and forwards them to the message handler.
 * Also sends client's messages to the server.
 *
 * @author MarcerAndrea
 */
public class ClientSocket extends Thread implements ConnectionToServerInterface {

	private Socket socketClient;
	private MessageReceiverInterface messageReceiver;
	private ObjectInputStream objInStream;
	private ObjectOutputStream objOutStream;
	private boolean active;


	/**
	 * Creates a socket between the client and the server.
	 *
	 * @param messageReceiver the associated interface that will receive the messages.
	 */
	public ClientSocket(MessageReceiverInterface messageReceiver, String ipAddress) {
		super("CUSTOM: Socket Connection to Server"); // Give a name to the thread for debugging purposes.
		this.messageReceiver = messageReceiver;
		active = true;
		String ipAdd;

		if (ipAddress == null)
			ipAdd = Utils.getServerConfig().getHost();
		else
			ipAdd = ipAddress;

		try {
			socketClient = new Socket(ipAdd, Utils.getServerConfig().getSocketPort());
			objOutStream = new ObjectOutputStream(socketClient.getOutputStream());
			objInStream = new ObjectInputStream(socketClient.getInputStream());
			this.start();
		} catch (IOException e) {
			Utils.logError("Failed to connect to the server.", e);
			active = false;
			messageReceiver.failedConnection();
		}
	}


	/**
	 * Send a message to the server.
	 *
	 * @param message the message to send.
	 */
	@Override // Of ConnectionToServerInterface.
	public void sendMessage(Message message) {
		try {
			objOutStream.writeObject(message);
		} catch (IOException e) {
			Utils.logError("Error in ClientSocket: sendMessage()", e);
		}
	}

	/**
	 * Returns true if and only if the connection is active.
	 *
	 * @return true if and only if the connection is active.
	 */
	@Override // Of ConnectionToServerInterface.
	public boolean isConnectionActive() {
		return active;
	}

	/**
	 * Closes the connection with the server.
	 */
	@Override // Of ConnectionToServerInterface.
	public void closeConnectionWithServer() {
		active = false;
		try {
			socketClient.close();
		} catch (IOException e) {
			Utils.logError("Error in ClientSocket: closeConnectionWithServer()", e);
		}
	}

	/**
	 * Listens for messages from the server.
	 */
	@Override // Of Thread.
	public void run() {
		try {
			while (isConnectionActive()) {
				final Message message = (Message) objInStream.readObject();

				// Run in another thread so the processing of a message,
				// that includes waiting for user input,
				// doesn't stop the reception of other messages or a connection lost event.
				new Thread(() -> messageReceiver.processMessage(message), "CUSTOM: Socket Message Processing").start();
			}
		} catch (IOException | ClassNotFoundException e) {
			Utils.logError("Connection closed by the server.", e);
			closeConnectionWithServer();
			messageReceiver.lostConnection();
		}
	}
}
