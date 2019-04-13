package it.polimi.se2019.model;

import it.polimi.se2019.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameBoardRep implements Serializable {

	private int remainingSkulls;
	private ArrayList<String> doubleKills;
	private ArrayList<KillShotRep> killShoots;
	private String currentPlayer;


	public GameBoardRep(GameBoard gameBoard) {
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