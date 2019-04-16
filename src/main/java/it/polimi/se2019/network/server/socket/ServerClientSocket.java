package it.polimi.se2019.network.server.socket;

import it.polimi.se2019.network.ConnectionInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ServerClientSocket extends Thread implements ConnectionInterface {

	private SocketServer server;
	private Socket socket;

	private ObjectInputStream objInStream;
	private ObjectOutputStream objOutStream;

	public ServerClientSocket(SocketServer server, Socket socket){
		this.server = server;
		this.socket = socket;

		try {
			this.objInStream = new ObjectInputStream(this.socket.getInputStream());
			this.objOutStream = new ObjectOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			Utils.logError("Error in ServerClientSocket()", e);
		}
	}

	@Override
	public void run() {
		//TODO message receiver loop.
	}

	@Override
	public void processMessage(Message message) throws RemoteException {
		//TODO sends message throw objOutStream
	}
}
