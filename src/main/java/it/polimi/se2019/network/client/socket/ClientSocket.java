package it.polimi.se2019.network.client.socket;

import it.polimi.se2019.network.client.ClientInterface;
import it.polimi.se2019.network.client.ClientMessageSenderInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.utils.Utils;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * Socket that receives messages from the server and sends the to the message handler. Also receives messages from the client and sends them to the server.
 * @author MarcerAndrea
 */
public class ClientSocket extends Thread  implements ClientMessageSenderInterface, Closeable {

	private static final String HOST = "localhost";
	private static final int PORT = 12345;

	private Socket socketClient;
	private ClientInterface client;
	private ObjectInputStream objInStream;
	private ObjectOutputStream objOutStream;
	private boolean active;

	/**
	 * Creates a socket to the server
	 * @param client
	 */
	public ClientSocket(ClientInterface client){
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
	@Override
	public synchronized void close() {
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
				Message message = (Message) objInStream.readObject();
					try {
						client.processMessage(message);
					}catch (RemoteException e){
						Utils.logError("Error in ClientSocket: Run()", e);
					}
			}
		} catch (IOException | ClassNotFoundException e) {
			Utils.logError("Error in ClientSocket: Run()", e);
		}finally{
			close();
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
