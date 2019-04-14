package it.polimi.se2019.network.server;

import it.polimi.se2019.network.Message;
import it.polimi.se2019.network.client.RMIClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {

	void registerClient(RMIClientInterface client) throws RemoteException;

	void sendMessage(Message message) throws RemoteException;

	void startRMIServer() throws RemoteException;
}
