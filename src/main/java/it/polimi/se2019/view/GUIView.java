package it.polimi.se2019.view;

public class GUIView implements RemoteViewInterface {

	public ModelRep modelRep;

	public GUIView() {
		this.modelRep = new ModelRep();
	}


	@Override
	public String askNickname() {
		return null;
	}

	@Override
	public void displayWaitingPlayers(String waitingPlayers) {

	}

	@Override
	public void displayTimerStarted(long delayInMs) {

	}

	@Override
	public void displayText(String text) {

	}

	@Override
	public int askMapToUse() {
		return 0;
	}

	@Override
	public int askSkullsForGame() {
		return 0;
	}
}