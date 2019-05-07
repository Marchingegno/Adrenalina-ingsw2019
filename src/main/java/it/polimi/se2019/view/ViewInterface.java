package it.polimi.se2019.view;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.MacroAction;

import java.util.List;

public interface ViewInterface {

	void askActionExample(); // TODO remove

	void askAction();

	void askGrab();

	void askMove();

	void askShoot();

	void askReload();

	void askSpawn();

	void displayPossibleActions(List<MacroAction> possibleActions);

	void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate);

	void updateGameMapRep(GameMapRep gameMapRepToUpdate);

	void updatePlayerRep(PlayerRep playerRepToUpdate);
}