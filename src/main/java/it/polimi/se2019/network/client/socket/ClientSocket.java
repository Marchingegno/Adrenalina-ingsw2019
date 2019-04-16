package it.polimi.se2019.network.client.socket;

import it.polimi.se2019.network.client.Client;
import it.polimi.se2019.network.client.ClientMessageSenderInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket extends Thread  implements ClientMessageSenderInterface {

	private final String HOST = "localhost";
	private final int PORT = 12345;

	private Socket clientSocket;
	private Client client;
	private ObjectInputStream objInStream;
	private ObjectOutputStream objOutStream;
	private boolean active;

	public ClientSocket(Client client){
		this.client = client;
		active = false;
	}

	public synchronized void closeConnection() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			Utils.logError("Error in ClientSocket: closeConnection()", e);
		}
		active = false;
	}

	public boolean isActive() {
		return active;
	}

	@Override
	public void run() {
		try{
			while(isActive()){
				client.processMessage((Message) objInStream.readObject());
			}
		} catch (IOException | ClassNotFoundException e) {
			Utils.logError("Error in ServerClientSocket: Run()", e);
		}finally{
			closeConnection();
		}
	}

	@Override
	public void registerClient(){
		try {
			clientSocket = new Socket(HOST, PORT);
			this.objOutStream = new ObjectOutputStream(this.clientSocket.getOutputStream());
			this.objInStream = new ObjectInputStream(this.clientSocket.getInputStream());
		} catch (IOException e) {
			Utils.logError("Error in ServerClientSocket: registerClient()", e);
		}
		active = true;
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
