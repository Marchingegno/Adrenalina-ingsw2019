package it.polimi.se2019.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Implements a timer with the isRunning() method.
 *
 * @author Desno365
 */
public class SingleTimer {

	private Timer timer;
	private Runnable runnableOnEnd;


	/**
	 * Starts the timer.
	 *
	 * @param runnableOnEnd runnable to run when the timer ends.
	 * @param delay         delay of the timer.
	 */
	public void start(Runnable runnableOnEnd, long delay) {
		this.runnableOnEnd = runnableOnEnd;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				onEnd();
			}
		}, delay);
		Utils.logInfo("SingleTimer: Scheduled a timer for " + delay + " milliseconds.");
	}

	/**
	 * Returns true if the timer is running.
	 *
	 * @return true if the timer is running.
	 */
	public boolean isRunning() {
		return timer != null;
	}

	/**
	 * Cancels the timer.
	 */
	public void cancel() {
		if (timer != null)
			timer.cancel();
		timer = null;
	}


	private void onEnd() {
		Utils.logInfo("SingleTimer: Timer ended.");
		if (runnableOnEnd != null)
			runnableOnEnd.run();
	}

}
