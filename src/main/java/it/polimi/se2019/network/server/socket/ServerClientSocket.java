package it.polimi.se2019.network.server.socket;

import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.network.message.RepMessage;
import it.polimi.se2019.network.server.AbstractConnectionToClient;
import it.polimi.se2019.network.server.ServerEventsListenerInterface;
import it.polimi.se2019.utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Contains the socket to communicate with a client. It sends messages through the output stream and receives them through the input stream.
 * @author MarcerAndrea
 */
public class ServerClientSocket extends AbstractConnectionToClient implements Runnable {

	private ServerEventsListenerInterface serverEventsListener;
	private Socket socket;
	private boolean active;
	private ObjectInputStream objInStream;
	private ObjectOutputStream objOutStream;

	public ServerClientSocket(ServerEventsListenerInterface serverEventsListener, Socket socket){
		active = true;
		this.serverEventsListener = serverEventsListener;
		this.socket = socket;

		try {
			objOutStream = new ObjectOutputStream(socket.getOutputStream());
			objInStream = new ObjectInputStream(socket.getInputStream());
			new Thread(this, "CUSTOM: Socket Connection to Client").start();
			Utils.logInfo("ServerClientSocket => ServerClientSocket(): a new connection to a client has been created with Socket.");
		} catch (IOException e) {
			Utils.logError("Error in ServerClientSocket()", e);
			active = false;
		}
	}

	/**
	 * Returns true if and only if the socket is active.
	 * @return true if and only if the socket is active.
	 */
	public boolean isConnectionActive(){ return active; }

	/**
	 * Thread that listens for new messages from the client.
	 */
	@Override
	public void run() {
		try{
			while(isConnectionActive()){
				serverEventsListener.onMessageReceived(this, (Message) objInStream.readObject());
			}
		} catch (IOException | ClassNotFoundException e) {
			Utils.logError("Connection lost.", e);
			serverEventsListener.onConnectionLost(this);
		}finally{
			closeConnectionWithClient();
		}
	}

	/**
	 * Send a message to the client.
	 * @param message the message to send.
	 */
	@Override
	public void sendMessage(Message message){
		Utils.logInfo("ServerClientSocket -> sendMessage(): sending a message to " + hashCode() + " " + message +
				(message.getMessageType().equals(MessageType.UPDATE_REPS) ? " inner message " + ((RepMessage) message).getMessage() : ""));
		try {
			objOutStream.writeObject(message);
		}catch(IOException e){
			Utils.logError("Socket: send message to client failed.", e);
		}
	}

	/**
	 * Closes the connection with the client.
	 */
	@Override
	public void closeConnectionWithClient() {
		active = false;
		try {
			socket.close();
		} catch (IOException e) {
			Utils.logError("Error in closeConnectionWithClient", e);
		}
	}
}
