package it.polimi.se2019.view;

import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;

public interface ViewInterface {

	String askNickname();

	void displayWaitingPlayers(String waitingPlayers);

	void displayTimerStarted(long delayInMs);

	void displayText(String text);

	void displayGame();

	int askMapToUse();

	int askSkullsForGame();

	void askAction();

	void showTargettablePlayers();

	void updateGameMapRep(GameMapRep gameMapRepToUpdate);

	void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate);

	void updatePlayerRep(PlayerRep playerRepToUpdate);

	void showMessage(String stringToShow);
}