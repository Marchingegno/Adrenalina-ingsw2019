package it.polimi.se2019.view;

import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.MacroAction;

import java.util.List;

public interface ViewInterface {

	void askActionExample(); // TODO remove

	void displayPossibleActions(List<MacroAction> possibleActions);

	void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate);

	void updateGameMapRep(GameMapRep gameMapRepToUpdate);

	void updatePlayerRep(PlayerRep playerRepToUpdate);
}