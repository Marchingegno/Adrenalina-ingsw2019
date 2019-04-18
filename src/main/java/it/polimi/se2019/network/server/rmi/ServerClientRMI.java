package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.rmi.RMIClientInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.ConnectionToClientInterface;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;

public class ServerClientRMI implements ConnectionToClientInterface {

	private RMIClientInterface rmiClientInterface;


	public ServerClientRMI(RMIClientInterface rmiClientInterface) {
		this.rmiClientInterface = rmiClientInterface;
	}


	@Override
	public void sendMessage(Message message) {
		new Thread(() -> {
			try {
				rmiClientInterface.receiveMessage(message);
			} catch (RemoteException e) {
				Utils.logError("Error in ServerClientRMI: sendMessage()", e);
			}
		}, "CUSTOM: Server Message Sending").start();
	}
}
