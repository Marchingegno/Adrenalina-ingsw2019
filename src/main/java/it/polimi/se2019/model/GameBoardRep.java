package it.polimi.se2019.model;

import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A sharable version of the game board
 * @author Desno365
 */
public class GameBoardRep extends Message {

	private int remainingSkulls;
	private ArrayList<String> doubleKills;
	private ArrayList<KillShotRep> killShoots;
	private String currentPlayer;


	public GameBoardRep(GameBoard gameBoard) {
		super(MessageType.GAME_BOARD_REP, MessageSubtype.INFO);
		this.remainingSkulls = gameBoard.getRemainingSkulls();
		this.doubleKills = new ArrayList<>();
		for(Player player : gameBoard.getDoubleKills())
			doubleKills.add(player.getPlayerName());
		this.killShoots = new ArrayList<>();
		for(KillShot killShot : gameBoard.getKillShots())
			killShoots.add(new KillShotRep(killShot.getPlayer().getPlayerName(), killShot.isOverkill()));
		this.currentPlayer = gameBoard.getCurrentPlayer().getPlayerName();
	}


	public int getRemainingSkulls() {
		return remainingSkulls;
	}

	public List<String> getDoubleKills() {
		return doubleKills;
	}

	public List<KillShotRep> getKillShoots() {
		return killShoots;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

}


class KillShotRep implements Serializable {

	private String playerName;
	private boolean overkill;


	public KillShotRep(String shootingPlayerName, boolean overkill) {
		playerName = shootingPlayerName;
		this.overkill = overkill;
	}


	public String getPlayerName() {
		return playerName;
	}

	public boolean isOverkill() {
		return overkill;
	}
}