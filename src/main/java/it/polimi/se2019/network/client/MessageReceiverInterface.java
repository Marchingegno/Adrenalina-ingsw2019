package it.polimi.se2019.network.client;

import it.polimi.se2019.network.message.Message;

public interface MessageReceiverInterface {

	void processMessage(Message message);
}
