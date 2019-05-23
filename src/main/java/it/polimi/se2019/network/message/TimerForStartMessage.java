package it.polimi.se2019.network.message;

public class TimerForStartMessage extends Message {

	private final long delayInMs;


	public TimerForStartMessage(long delayForTimer, MessageSubtype messageSubtype) {
		super(MessageType.TIMER_FOR_START, messageSubtype);
		this.delayInMs = delayForTimer;
	}


	public long getDelayInMs() {
		return delayInMs;
	}
}
