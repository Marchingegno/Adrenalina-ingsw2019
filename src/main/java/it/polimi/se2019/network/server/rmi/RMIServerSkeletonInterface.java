package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.ClientMessageReceiverInterface;
import it.polimi.se2019.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerSkeletonInterface extends Remote {

	void registerClient(ClientMessageReceiverInterface client) throws RemoteException;

	void sendMessage(ClientMessageReceiverInterface client, Message message) throws RemoteException;
}
