package it.polimi.se2019.view.client;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that conatains the representation of the model.
 * Each representation in updates whenever the client receive a message of type UPDATE_REPS.
 *
 * @author MarcerAndrea
 */
public class ModelRep {

	private GameBoardRep gameBoardRep;
	private GameMapRep gameMapRep;
	private List<PlayerRep> playersRep = new ArrayList<>();

	public GameBoardRep getGameBoardRep() {
		return gameBoardRep;
	}

	/**
	 * Sets the gameboard rep.
	 * @param gameBoardRep rep to set.
	 */
	public void setGameBoardRep(GameBoardRep gameBoardRep) {
		this.gameBoardRep = gameBoardRep;
		Utils.logRep("ModelRep -> setGameBoardRep(): Updated the GameBoardRep");
	}

	/**
	 * Returns the game map rep.
	 * @return the game map rep.
	 */
	public GameMapRep getGameMapRep() {
		return gameMapRep;
	}

	/**
	 * Sets the gamemap rep.
	 * @param gameMapRep rep to set.
	 */
	public void setGameMapRep(GameMapRep gameMapRep) {
		this.gameMapRep = gameMapRep;
		Utils.logRep("ModelRep -> setGameMapRep(): Updated the GameMapRep");
	}

	/**
	 * Returns the list of players rep.
	 * @return the list of players rep.
	 */
	public List<PlayerRep> getPlayersRep() {
		return playersRep;
	}

	/**
	 * Returns the player rep of the client.
	 * @return the player rep of the client.
	 */
	public PlayerRep getClientPlayerRep() {
		for (PlayerRep playerRep : playersRep) {
			if (!playerRep.isHidden())
				return playerRep;
		}
		throw new IllegalStateException("Player rep of Client not found.");
	}

	/**
	 * Sets the player rep.
	 * @param playerRepToSet player rep to set
	 */
	public void setPlayerRep(PlayerRep playerRepToSet) {
		int numOfPlayersRep = playersRep.size();
		for (int i = 0; i < numOfPlayersRep; i++) {
			if (playersRep.get(i).getPlayerName().equals(playerRepToSet.getPlayerName())) {
				playersRep.set(i, playerRepToSet);
				Utils.logRep("ModelRep -> setPlayersRep(): Updated the PlayersRep of " + playerRepToSet.getPlayerName());
				return;
			}
		}
		//There is no PlayerRep for this player so I add it
		Utils.logRep("ModelRep -> setPlayersRep(): Received a new PlayersRep of " + playerRepToSet.getPlayerName());
		playersRep.add(playerRepToSet);
	}
}