package it.polimi.se2019.view.client;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.MacroAction;

import java.util.List;

public class GUIView extends RemoteView {

	private ModelRep modelRep;

	public GUIView() {
		this.modelRep = new ModelRep();
	}

	@Override
	public void askForConnectionAndStartIt() {
		/*if(connection == 1)
			Client.startConnectionWithRMI(this);
		else
			Client.startConnectionWithSocket(this);*/
	}

	@Override
	public void failedConnectionToServer() {

	}

	@Override
	public void lostConnectionToServer() {

	}

	@Override
	public void askNickname() {

	}

	@Override
	public void askNicknameError() {

	}

	@Override
	public void nicknameIsOk(String nickname) {

	}

	@Override
	public void displayWaitingPlayers(String waitingPlayers) {

	}

	@Override
	public void displayGame() {

	}

	@Override
	public void displayTimerStarted(long delayInMs) {

	}

	@Override
	public void displayTimerStopped() {

	}

	@Override
	public void askMapAndSkullsToUse() {

	}

	@Override
	public void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType) {

	}

	// TODO remove
	@Override
	public void askActionExample() {

	}

	@Override
	public void askAction() {

	}

	@Override
	public void askGrab() {

	}

	@Override
	public void askMove() {

	}

	@Override
	public void askShoot() {

	}

	@Override
	public void askReload() {

	}

	@Override
	public void displayPossibleActions(List<MacroAction> possibleActions) {

	}

	@Override
	public void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate) {

	}

	@Override
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {

	}

	@Override
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {

	}
}