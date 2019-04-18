package it.polimi.se2019.view;

public interface RemoteViewInterface {

	String askNickname();

	void displayWaitingPlayers(String waitingPlayers);

	void displayTimerStarted(long delayInMs);

	void displayText(String text);

	int askMapToUse();

	int askSkullsForGame();
}