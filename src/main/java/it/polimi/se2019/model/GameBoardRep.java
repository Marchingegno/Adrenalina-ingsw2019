package it.polimi.se2019.model;

import it.polimi.se2019.model.player.KillShotRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;

/**
 * A sharable version of the game board
 * @author Desno365
 */
public class GameBoardRep extends Message {

	private int remainingSkulls;
	private ArrayList<Utils.CharacterColorType> doubleKills;
	private ArrayList<KillShotRep> killShoots;
	private String currentPlayer;


	public GameBoardRep(GameBoard gameBoard) {
		super(MessageType.GAME_BOARD_REP, MessageSubtype.INFO);
		this.remainingSkulls = gameBoard.getRemainingSkulls();
		this.doubleKills = new ArrayList<>();
		for(Player player : gameBoard.getDoubleKills())
			doubleKills.add(player.getPlayerColor());
		this.killShoots = new ArrayList<>();
		for(KillShot killShot : gameBoard.getKillShots())
			killShoots.add(new KillShotRep(killShot.getPlayer(), killShot.isOverkill()));
		this.currentPlayer = gameBoard.getCurrentPlayer().getPlayerName();
	}


	public int getRemainingSkulls() {
		return remainingSkulls;
	}

	public ArrayList<Utils.CharacterColorType> getDoubleKills() {
		return doubleKills;
	}

	public ArrayList<KillShotRep> getKillShoots() {
		return killShoots;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean equals(Object object){
		return (object instanceof GameBoardRep &&
				this.remainingSkulls == ((GameBoardRep) object).remainingSkulls	&&
				this.doubleKills.equals(((GameBoardRep) object).doubleKills) &&
				this.currentPlayer.equals(((GameBoardRep) object).currentPlayer) &&
				this.killShoots.equals(((GameBoardRep) object).killShoots));
	}

}


