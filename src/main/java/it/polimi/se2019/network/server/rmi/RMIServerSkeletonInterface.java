package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.ClientInterface;
import it.polimi.se2019.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerSkeletonInterface extends Remote {

	void registerClient(ClientInterface client) throws RemoteException;

	void receiveMessage(ClientInterface client, Message message) throws RemoteException;
}
