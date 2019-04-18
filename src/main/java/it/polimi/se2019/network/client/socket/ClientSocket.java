package it.polimi.se2019.network.client.socket;

import it.polimi.se2019.network.ConnectionInterface;
import it.polimi.se2019.network.client.ClientMessageSenderInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Socket that receives messages from the server and sends the to the message handler. Also receives messages from the client and sends them to the server.
 * @author MarcerAndrea
 */
public class ClientSocket extends Thread  implements ClientMessageSenderInterface {

	private static final String HOST = "localhost";
	private static final int PORT = 12345;

	private Socket socketClient;
	private ConnectionInterface client;
	private ObjectInputStream objInStream;
	private ObjectOutputStream objOutStream;
	private boolean active;

	/**
	 * Creates a socket to the server
	 * @param client
	 */
	public ClientSocket(ConnectionInterface client){
		this.client = client;
		try {
			socketClient = new Socket(HOST, PORT);
			this.objOutStream = new ObjectOutputStream(this.socketClient.getOutputStream());
			this.objInStream = new ObjectInputStream(this.socketClient.getInputStream());
		} catch (IOException e) {
			Utils.logError("Error in ServerClientSocket: registerClient()", e);
		}
		active = false;
	}

	/**
	 * Closes the connection with the server.
	 */
	public synchronized void closeConnection() {
		try {
			socketClient.close();
		} catch (IOException e) {
			Utils.logError("Error in ClientSocket: closeConnection()", e);
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
				client.processMessage((Message) objInStream.readObject());
			}
		} catch (IOException | ClassNotFoundException e) {
			Utils.logError("Error in ClientSocket: Run()", e);
		}finally{
			closeConnection();
		}
	}

	/**
	 * Activates the communication with the server by starting the listening thread.
	 */
	@Override
	public void registerClient(){
		active = true;
		this.start();
	}

	@Override
	public void sendMessage(Message message) {
		try {
			objOutStream.writeObject(message);
		} catch (IOException e) {
			Utils.logError("Error in ClientSocket: sendMessage()", e);
		}

	}
}
