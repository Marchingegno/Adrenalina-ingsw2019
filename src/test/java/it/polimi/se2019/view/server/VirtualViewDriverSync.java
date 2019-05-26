package it.polimi.se2019.view.server;

import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.utils.Utils;

public class VirtualViewDriverSync extends VirtualViewDriver {

	private Message messageBuffer;

	public VirtualViewDriverSync(String nickname, boolean TEST_SHOOT, boolean TEST_MOVE) {
		super(nickname, TEST_SHOOT, TEST_MOVE);
	}


	/**
	 * Since we are playing a game in local there is no need to send the reps;
	 * However in local we can't have asynchronous processes for testing,
	 * So we use this method to know when the VirtualViewDriver can actually send an answer message.
	 */
	@Override
	public void sendReps() {
		showRep();
		if(messageBuffer != null) {
			Utils.logInfo("VirtualViewDriver -> sendReps(): sending message in buffer to the controller.");

			// Use a new Message variable since we need to set messageBuffer to null BEFORE calling onMessageReceived
			final Message message = messageBuffer;
			messageBuffer = null;
			onMessageReceived(message);
		}
	}


	@Override
	protected void sendMessageToController(Message message) {
		messageBuffer = message;
	}
}
