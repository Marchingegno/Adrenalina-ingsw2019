package it.polimi.se2019.network.server.socket;

import it.polimi.se2019.network.ConnectionInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.ServerMessageHandler;
import it.polimi.se2019.utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClientSocket extends Thread implements ConnectionInterface {

	private ServerMessageHandler serverMessageHandler;
	private Socket socket;
	private boolean active;
	private ObjectInputStream objInStream;
	private ObjectOutputStream objOutStream;

	public ServerClientSocket(ServerMessageHandler serverMessageHandler, Socket socket){
		this.serverMessageHandler = serverMessageHandler;
		this.socket = socket;

		try {
			this.objOutStream = new ObjectOutputStream(this.socket.getOutputStream());
			this.objInStream = new ObjectInputStream(this.socket.getInputStream());
		} catch (IOException e) {
			Utils.logError("Error in ServerClientSocket()", e);
		}

		active = true;
	}

	public synchronized void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			Utils.logError("Error in closeConnection", e);
		}
		active = false;
	}

	public boolean isActive(){
		return active;
	}

	@Override
	public void run() {
		try{
			while(isActive()){
				serverMessageHandler.onMessageReceived(this, (Message) objInStream.readObject());
			}
		} catch (IOException | ClassNotFoundException e) {
			Utils.logError("Error in ServerClientSocket: Run()", e);
		}finally{
			closeConnection();
		}
	}

	@Override
	public void processMessage(Message message){
		try {
			objOutStream.writeObject(message);
		}catch(IOException e){
			Utils.logError("Error in ServerClientSocket: ProcessMessage()", e);
		}

	}
}
