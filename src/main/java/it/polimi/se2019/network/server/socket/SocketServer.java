package it.polimi.se2019.network.server.socket;

import it.polimi.se2019.network.server.ServerMessageHandler;
import it.polimi.se2019.utils.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Server that listens for new socket clients and creates a ServerClientSocket for each of them
 * @author MarcerAndrea
 */
public class SocketServer extends Thread{

	private final int PORT = 12345;

	private ServerSocket serverSocket; //Server that listens for requests
	private ServerMessageHandler serverMessageHandler; //General serverMessageHandler that works both with RMI and Socket

	private boolean active = false;

	/**
	 * Starts a thread that listens for new connection requests
	 * @param serverMessageHandler the message handler to which all messages are sent
	 */
	public SocketServer(ServerMessageHandler serverMessageHandler) {
		super("CUSTOM: Socket Connection Request Listener"); // Give a name to the thread for debugging purposes.
		this.serverMessageHandler = serverMessageHandler;
		startServerSocket();
	}

	/**
	 * Listens for client connection requests and creates a socket for each client. The socket is added to the
	 */
	@Override
	public void run() {
		Utils.logInfo("Socket server is listening");

		while(isActive()) {

			Socket newClientSocket;
			ServerClientSocket newServerClientSocket;

			try {
				//The server receives a new connection requests, accepts it and returns the socket associated.
				newClientSocket = serverSocket.accept();

				//The socket is decorated with the logic to handle the message communication
				newServerClientSocket = new ServerClientSocket(serverMessageHandler, newClientSocket);

				//The new decorated socket is registered to the server message handler
				serverMessageHandler.onClientRegistration(newServerClientSocket);

			} catch (SocketException e) {
				if(e.getMessage().equals("Socket closed") || e.getMessage().equals("Socket is closed"))
					Utils.logInfo("Connection closed by the server.");
				else
					Utils.logError("Error in SocketServer: run()", e);
			} catch (IOException e) {
				Utils.logError("Error in SocketServer: run()", e);
			}
		}
	}

	/**
	 * Returns true if and only if the server is active.
	 * @return true if and only if the server is active.
	 */
	public boolean isActive() {	return active;}

	/**
	 * Closes the server.
	 */
	public void close(){

		try {
			serverSocket.close();
		}catch (IOException e){
			Utils.logError("Error in SocketServer: close()", e);
		}active = false;
	}

	private void startServerSocket(){
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			Utils.logError("Error in SocketServer()", e);
		}
		active = true;
	}
}
