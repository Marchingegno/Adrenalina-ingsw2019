package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.rmi.RMIClientInterface;
import it.polimi.se2019.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerSkeletonInterface extends Remote {

	void registerClient(RMIClientInterface rmiClientInterface) throws RemoteException;

	void receiveMessage(RMIClientInterface rmiClientInterface, Message message) throws RemoteException;
}
