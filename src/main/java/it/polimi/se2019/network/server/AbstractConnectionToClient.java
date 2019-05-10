package it.polimi.se2019.network.server;

import it.polimi.se2019.network.message.Message;

/**
 * Represents the methods of the client that can be called by the server.
 * Basically this class represents what the server can do with the client.
 */
public abstract class AbstractConnectionToClient {

	private String nickname ;


	/**
	 * Returns true if a nickname has been set for this client.
	 * @return true if a nickname has been set for this client.
	 */
	public boolean isNicknameSet() {
		return nickname != null;
	}

	/**
	 * Get the current nickname of the client or null if the client doesn't have a nickname.
	 * @return the nickname of the client or null if the client doesn't have a nickname.
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Set a nickname to the client.
	 * Note: a nickname can't be changed after setting it.
	 * @param nickname the nickname to set to the client.
	 */
	public void setNickname(String nickname) {
		if(isNicknameSet())
			throw new IllegalStateException("A nickname for this client has been already set.");
		else
			this.nickname = nickname;
	}

	/**
	 * Send a message to the client.
	 * @param message the message to send.
	 */
	public abstract void sendMessage(Message message);

	/**
	 * Closes the connection with the client.
	 */
	public abstract void closeConnectionWithClient();
}
