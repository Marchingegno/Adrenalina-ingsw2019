package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.rmi.RMIClientInterface;
import it.polimi.se2019.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Represents the methods of the server that can be called remotely by the client.
 */
public interface RMIServerSkeletonInterface extends Remote {

    /**
     * Register a new client that is connected to the server. This method is called remotely by the client.
     *
     * @param rmiClientInterface the RMI implementation of the client.
     * @throws RemoteException
     */
    void registerClient(RMIClientInterface rmiClientInterface) throws RemoteException;

    /**
     * Receives a message from the client and handles it. This method is called remotely by the client.
     *
     * @param rmiClientInterface the client sending the message
     * @param message            the message received from the client.
     * @throws RemoteException
     */
    void receiveMessage(RMIClientInterface rmiClientInterface, Message message) throws RemoteException;

    /**
     * Called by the RMI client to check for a connection lost.
     * When this method interrupts the client knows the connection has been lost.
     *
     * @param rmiClientInterface the client checking for connection lost.
     * @throws RemoteException
     * @throws InterruptedException
     */
    void connectionListenerSubjectInServer(RMIClientInterface rmiClientInterface) throws RemoteException, InterruptedException;
}
