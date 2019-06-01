package it.polimi.se2019.view.server;

import it.polimi.se2019.network.message.Message;

public class VirtualViewDriverAsync extends VirtualViewDriver {


	public VirtualViewDriverAsync(String nickname, boolean TEST_MOVE, boolean TEST_SHOOT, boolean TEST_POWERUP) {
		super(nickname, TEST_MOVE, TEST_SHOOT, TEST_POWERUP);
	}


	@Override
	public void sendReps() {
		showRep();
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
