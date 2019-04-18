package it.polimi.se2019.network.client.socket;

import it.polimi.se2019.network.client.Client;
import it.polimi.se2019.network.client.ConnectionToServerInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.utils.Utils;

import java.io.*;
import java.net.Socket;

/**
 * Socket that receives messages from the server and forwards them to the message handler.
 * Also sends client's messages to the server.
 * @author MarcerAndrea
 */
public class ClientSocket extends Thread  implements ConnectionToServerInterface, Closeable {

	private static final String HOST = "localhost";
	private static final int PORT = 12345;

	private Socket socketClient;
	private Client client;
	private ObjectInputStream objInStream;
	private ObjectOutputStream objOutStream;
	private boolean active;

	/**
	 * Creates a socket between the client and the server.
	 * @param client the associated client that will receive the messages.
	 */
	public ClientSocket(Client client){
		this.client = client;
		try {
			socketClient = new Socket(HOST, PORT);
			this.objOutStream = new ObjectOutputStream(this.socketClient.getOutputStream());
			this.objInStream = new ObjectInputStream(this.socketClient.getInputStream());
		} catch (IOException e) {
			Utils.logError("Error in ClientSocket()", e);
		}
		active = true;
		this.start();
	}

	/**
	 * Closes the connection with the server.
	 */
	@Override
	public void close() {
		try {
			socketClient.close();
		} catch (IOException e) {
			Utils.logError("Error in ClientSocket: close()", e);
		}
		active = false;
	}

	/**
	 * Returns true if and only if the socket is active.
	 * @return true if and only if the socket is active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Listens for messages from the server.
	 */
	@Override
	public void run() {
		try{
			while(isActive()){
				Message message = (Message) objInStream.readObject();
				client.processMessage(message);
			}
		} catch (EOFException e) {
			Utils.logInfo("Connection closed by the server.");
		} catch (IOException | ClassNotFoundException e) {
			Utils.logError("Error in ClientSocket: run()", e);
		}finally{
			close();
		}
	}

	/**
	 * Send a message to the server.
	 * @param message the message to send.
	 */
	@Override
	public void sendMessage(Message message) {
		try {
			objOutStream.writeObject(message);
		} catch (IOException e) {
			Utils.logError("Error in ClientSocket: sendMessage()", e);
		}
	}
}
