package it.polimi.se2019.view;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;

import java.util.List;

public interface ViewInterface {

	String getNickname();

	void askAction();

	void askGrab();

	void askMove(List<Coordinates> reachableCoordinates);

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