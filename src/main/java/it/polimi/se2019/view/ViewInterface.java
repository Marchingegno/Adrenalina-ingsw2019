package it.polimi.se2019.view;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;

public interface ViewInterface {

	void askAction();

	void askGrab();

	void askMove();

	void askShoot();

	void askReload();

	void askSpawn();

	//Maybe this will be useful later? For now it is used to end the turn.
	//Maybe use it for reloading.
	void askEnd();

	void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate);

	void updateGameMapRep(GameMapRep gameMapRepToUpdate);

	void updatePlayerRep(PlayerRep playerRepToUpdate);
}