package it.polimi.se2019.model.gameboard;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * A sharable version of the game board.
 *
 * @author Desno365
 */
public class GameBoardRep implements Representation {

	private int remainingSkulls;
	private List<Color.CharacterColorType> doubleKills;
	private List<KillShotRep> killShoots;
	private String currentPlayer;
	private int numberOfPlayers;


	public GameBoardRep(GameBoard gameBoard) {
		this.remainingSkulls = gameBoard.getRemainingSkulls();
		this.doubleKills = new ArrayList<>();
		for (Player player : gameBoard.getDoubleKills())
			doubleKills.add(player.getPlayerColor());
		this.killShoots = new ArrayList<>();
		for (KillShot killShot : gameBoard.getKillShots())
			killShoots.add((KillShotRep) killShot.getRep());
		this.currentPlayer = gameBoard.getCurrentPlayer().getPlayerName();
		this.numberOfPlayers = gameBoard.getPlayers().size();
	}

	/**
	 * Returns the number of remaining skulls.
	 *
	 * @return the number of remaining skulls.
	 */
	public int getRemainingSkulls() {
		return remainingSkulls;
	}

	/**
	 * Returns the list of all double kills.
	 *
	 * @return the list of all double kills.
	 */
	public List<Color.CharacterColorType> getDoubleKills() {
		return doubleKills;
	}

	/**
	 * Returns the list of all killshoots.
	 *
	 * @return the list of all killshoots.
	 */
	public List<KillShotRep> getKillShoots() {
		return killShoots;
	}

	/**
	 * Returns the name of the current player.
	 *
	 * @return the name of the current player.
	 */
	public String getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Returns the number of players in the game.
	 *
	 * @return the number of players in the game.
	 */
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
}


