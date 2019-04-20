package it.polimi.se2019.view;

import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;

public class GUIView implements ViewInterface {

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
	public void displayGame() {

	}

	@Override
	public int askMapToUse() {
		return 0;
	}

	@Override
	public int askSkullsForGame() {
		return 0;
	}

	@Override
	public void askAction() {

	}

	@Override
	public void showTargettablePlayers() {

	}

	@Override
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {

	}

	@Override
	public void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate) {

	}

	@Override
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {

	}

	@Override
	public void showMessage(String stringToShow) {

	}
}