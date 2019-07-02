package it.polimi.se2019.network.client;

import it.polimi.se2019.network.message.Message;

/**
 * Implemented by classes which want to connect to the server.
 */
public interface ConnectionToServerInterface {

    /**
     * Sends a message to the server.
     *
     * @param message the message to send.
     */
    void sendMessage(Message message);

    /**
     * Returns true if and only if the connection is active.
     *
     * @return true if and only if the connection is active.
     */
    boolean isConnectionActive();

    /**
     * Closes the connection with the server.
     */
    void closeConnectionWithServer();

}
