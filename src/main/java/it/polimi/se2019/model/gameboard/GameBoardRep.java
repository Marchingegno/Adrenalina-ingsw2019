package it.polimi.se2019.model.gameboard;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.player.KillShotRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.Color;

import java.util.ArrayList;

/**
 * A sharable version of the game board
 *
 * @author Desno365
 */
public class GameBoardRep extends Representation {

	private int remainingSkulls;
	private ArrayList<Color.CharacterColorType> doubleKills;
	private ArrayList<KillShotRep> killShoots;
	private String currentPlayer;


	public GameBoardRep(GameBoard gameBoard) {
		super(MessageType.GAME_BOARD_REP, MessageSubtype.INFO);
		this.remainingSkulls = gameBoard.getRemainingSkulls();
		this.doubleKills = new ArrayList<>();
		for (Player player : gameBoard.getDoubleKills())
			doubleKills.add(player.getPlayerColor());
		this.killShoots = new ArrayList<>();
		for (KillShot killShot : gameBoard.getKillShots())
			killShoots.add(new KillShotRep(killShot.getPlayer(), killShot.isOverkill()));
		this.currentPlayer = gameBoard.getCurrentPlayer().getPlayerName();
	}


	public int getRemainingSkulls() {
		return remainingSkulls;
	}

	public ArrayList<Color.CharacterColorType> getDoubleKills() {
		return doubleKills;
	}

	public ArrayList<KillShotRep> getKillShoots() {
		return killShoots;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}
}


