package it.polimi.se2019.network.server;

import it.polimi.se2019.network.client.ClientInterface;
import it.polimi.se2019.network.message.Message;

public interface ServerReceiverInterface {

	void onRegisterClient(ClientInterface client);

	void onReceiveMessage(ClientInterface client, Message message);
}
