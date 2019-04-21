package it.polimi.se2019.view;

import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;

public interface ViewInterface {

	void displayText(String text);

	void displayGame();

	void askAction();

	void showTargettablePlayers();

	void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate);

	void updateGameMapRep(GameMapRep gameMapRepToUpdate);

	void updatePlayerRep(PlayerRep playerRepToUpdate);

	void showMessage(String stringToShow);
}