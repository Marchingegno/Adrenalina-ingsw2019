package it.polimi.se2019.view;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.message.MessageType;

import java.util.List;

public interface ViewInterface {

	String getNickname();

	void askAction(List<Integer> activablePowerups);

	void askGrab(MessageType grabType);

	void askMove(List<Coordinates> reachableCoordinates);

	void askShoot();

	void askReload();

	void askSpawn();

	void askChoice(String question, List<String> options);

	void askPowerupChoice(String question, List<String> options);

	void askPowerupCoordinates(String question, List<Coordinates> coordinates);

	void askEnd(List<Integer> activablePowerups);

	void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate);

	void updateGameMapRep(GameMapRep gameMapRepToUpdate);

	void updatePlayerRep(PlayerRep playerRepToUpdate);
}