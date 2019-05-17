package it.polimi.se2019.view;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.Pair;

import java.util.List;

public interface ViewInterface {

	void askAction();

	void askGrab();

	void askMove();

	void askShoot();

	void askReload();

	void askSpawn();

	void askWeapon(String question, List<String> options);

	void askChoice(String question, List<String> options);

	//Maybe this will be useful later? For now it is used to end the turn.
	//Maybe use it for reloading.
	void askEnd();

	void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate);

	void updateGameMapRep(GameMapRep gameMapRepToUpdate);

	void updatePlayerRep(PlayerRep playerRepToUpdate);
}