package it.polimi.se2019.network.server.socket;

import it.polimi.se2019.network.server.ServerEventsListenerInterface;
import it.polimi.se2019.utils.ServerConfigParser;
import it.polimi.se2019.utils.Utils;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Server that listens for new socket clients and creates a ServerClientSocket for each of them
 * @author MarcerAndrea
 */
public class SocketServer extends Thread implements Closeable {

	private ServerSocket serverSocket; //Server that listens for requests
	private ServerEventsListenerInterface serverEventsListener; //General serverEventsListener that works both with RMI and Socket

	private boolean active = false;

	/**
	 * Starts a thread that listens for new connection requests.
	 * @param serverEventsListener the event listener to which all events are forwarded.
	 */
	public SocketServer(ServerEventsListenerInterface serverEventsListener) throws IOException {
		super("CUSTOM: Socket Connection Request Listener"); // Give a name to the thread for debugging purposes.
		this.serverEventsListener = serverEventsListener;
		startServerSocket();
	}

	/**
	 * Listens for client connection requests and creates a socket for each client. The socket is added to the
	 */
	@Override // Of Thread.
	public void run() {
		Utils.logInfo("Socket server is listening.");

		while(isActive()) {

			Socket newClientSocket;
			ServerClientSocket newServerClientSocket;

			try {
				//The server receives a new connection requests, accepts it and returns the socket associated.
				newClientSocket = serverSocket.accept();

				//The socket is decorated with the logic to handle the message communication
				newServerClientSocket = new ServerClientSocket(serverEventsListener, newClientSocket);

				//The new decorated socket is registered to the server message handler
				serverEventsListener.onClientConnection(newServerClientSocket);

			} catch (SocketException e) {
				if(e.getMessage().equals("Socket closed") || e.getMessage().equals("Socket is closed"))
					Utils.logInfo("Socket connection listening closed by request");
				else
					Utils.logError("Error in SocketServer: run()", e);
			} catch (IOException e) {
				Utils.logError("Error in SocketServer: run()", e);
			}
		}
	}

	/**
	 * Closes the server.
	 */
	@Override // Of Closeable.
	public void close(){
		active = false;
		try {
			serverSocket.close();
			Utils.logInfo("Socket server stopped.");
		}catch (IOException e){
			Utils.logError("Error in SocketServer: close()", e);
		}
	}

	/**
	 * Returns true if and only if the server is active.
	 * @return true if and only if the server is active.
	 */
	public boolean isActive() {	return active;}

	private void startServerSocket() throws IOException {
		serverSocket = new ServerSocket(ServerConfigParser.getSocketPort());
		this.start();
		active = true;
	}
}
