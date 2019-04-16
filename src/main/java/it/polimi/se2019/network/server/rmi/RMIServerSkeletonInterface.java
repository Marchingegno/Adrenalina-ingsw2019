package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.ConnectionInterface;
import it.polimi.se2019.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerSkeletonInterface extends Remote {

	void registerClient(ConnectionInterface client) throws RemoteException;

	void receiveMessage(ConnectionInterface client, Message message) throws RemoteException;
}
