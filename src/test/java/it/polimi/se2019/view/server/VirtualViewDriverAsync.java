package it.polimi.se2019.view.server;

import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.utils.Utils;

public class VirtualViewDriverAsync extends VirtualViewDriver {


	public VirtualViewDriverAsync(String nickname, boolean TEST_SHOOT, boolean TEST_MOVE, boolean DISPLAY_REPS) {
		super(nickname, TEST_SHOOT, TEST_MOVE, DISPLAY_REPS);
	}


	@Override
	public void sendReps() {
		Utils.logInfo("VirtualViewDriver -> sendReps(): called sendReps().");
	}


	@Override
	protected void sendMessageToController(final Message message) {
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						onMessageReceived(message);
					}
				}, 10);
	}
}
