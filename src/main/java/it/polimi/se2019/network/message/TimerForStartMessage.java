package it.polimi.se2019.network.message;

/**
 * Message used to report the start of the timer for the game start.
 * @author Desno365
 */
public class TimerForStartMessage extends Message {

	private final long delayInMs;


	/**
	 * Constructs a message.
	 *
	 * @param delayForTimer  the delay of the timer in milliseconds.
	 * @param messageSubtype the message subtype of this message.
	 */
	public TimerForStartMessage(long delayForTimer, MessageSubtype messageSubtype) {
		super(MessageType.TIMER_FOR_START, messageSubtype);
		this.delayInMs = delayForTimer;
	}


	/**
	 * Returns the delay of the timer.
	 *
	 * @return the delay of the timer.
	 */
	public long getDelayInMs() {
		return delayInMs;
	}
}
