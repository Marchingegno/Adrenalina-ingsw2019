package it.polimi.se2019.network.server.socket;

import it.polimi.se2019.network.server.MessageHandler;
import it.polimi.se2019.utils.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer extends Thread{

	private final int PORT = 12345;

	private ServerSocket serverSocket; //Server that listens for requests
	private MessageHandler messageHandler; //General messageHandler that works both with RMI and Socket
	private ExecutorService executor = Executors.newFixedThreadPool(128);

	public SocketServer(MessageHandler serverMessageHandler) {
		this.messageHandler = serverMessageHandler;
		startServerSocket();
	}

	private void startServerSocket(){
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			Utils.logError("Error in SocketServer()", e);
		}
	}

	/**
	 * Listens for client connection requests and creates a socket for each client
	 */
	@Override
	public void run() {
		while(true) {
			Socket clientSocket;
			try {
				clientSocket = serverSocket.accept();
				(new ServerClientSocket(this, clientSocket)).start();
			} catch (IOException e) {
				Utils.logError("Error in SocketServer: run()", e);
			}
		}
	}
}
