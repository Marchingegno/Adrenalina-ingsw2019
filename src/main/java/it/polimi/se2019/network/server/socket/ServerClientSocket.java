package it.polimi.se2019.network.server.socket;

import it.polimi.se2019.network.server.ConnectionToClientInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.ServerMessageHandler;
import it.polimi.se2019.utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Contains the socket to communicate with a client. It sends messages through the output stream and receives them through the input stream.
 * @author MarcerAndrea
 */
public class ServerClientSocket extends Thread implements ConnectionToClientInterface {

	private ServerMessageHandler serverMessageHandler;
	private Socket socket;
	private boolean active;
	private ObjectInputStream objInStream;
	private ObjectOutputStream objOutStream;

	public ServerClientSocket(ServerMessageHandler serverMessageHandler, Socket socket){
		super("CUSTOM: Socket Connection to Client"); // Give a name to the thread for debugging purposes.
		this.serverMessageHandler = serverMessageHandler;
		this.socket = socket;

		try {
			this.objOutStream = new ObjectOutputStream(this.socket.getOutputStream());
			this.objInStream = new ObjectInputStream(this.socket.getInputStream());
		} catch (IOException e) {
			Utils.logError("Error in ServerClientSocket()", e);
		}

		this.start();

		active = true;
	}

	/**
	 * Closes the connection with the client.
	 */
	public synchronized void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			Utils.logError("Error in closeConnection", e);
		}
		active = false;
	}

	/**
	 * Returns true if and only if the socket is active.
	 * @return true if and only if the socket is active.
	 */
	public boolean isActive(){ return active; }

	/**
	 * Thread that listens for new messages from the client.
	 */
	@Override
	public void run() {
		try{
			while(isActive()){
				serverMessageHandler.onMessageReceived(this, (Message) objInStream.readObject());
			}
		} catch (IOException | ClassNotFoundException e) {
			Utils.logError("Connection lost.", e);
			serverMessageHandler.onConnectionLost(this);
		}finally{
			closeConnection();
		}
	}

	/**
	 * TODO
	 * @param message the message received.
	 */
	@Override
	public void sendMessage(Message message){
		try {
			objOutStream.writeObject(message);
		}catch(IOException e){
			Utils.logError("Error in ServerClientSocket: ProcessMessage()", e);
		}

	}
}
