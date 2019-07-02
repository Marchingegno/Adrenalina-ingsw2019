package it.polimi.se2019.network.client.rmi;

import it.polimi.se2019.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote interface used by the RMI server to communicate with the client.
 */
public interface RMIClientInterface extends Remote {

    /**
     * Called by the RMI server in order to send a message.
     *
     * @param message the message sent by the server.
     * @throws RemoteException
     */
    void receiveMessage(Message message) throws RemoteException;

    /**
     * Called by the RMI server to check for a connection lost.
     * When this method interrupts the server knows the connection has been lost.
     *
     * @throws RemoteException
     * @throws InterruptedException
     */
    void connectionListenerSubjectInClient() throws RemoteException, InterruptedException;
}
