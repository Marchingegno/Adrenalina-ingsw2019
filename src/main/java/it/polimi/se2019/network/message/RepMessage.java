package it.polimi.se2019.network.message;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;

import java.util.ArrayList;
import java.util.List;

/**
 * Message used to send the reps of the model to the player.
 * @author MarcerAndrea
 */
public class RepMessage extends Message {

	private Message message = null;
	private GameMapRep gameMapRep = null;
	private GameBoardRep gameBoardRep = null;
	private List<PlayerRep> playersRep = new ArrayList<>();
	private boolean hasReps = false;


	/**
	 * Constructs a message.
	 */
	public RepMessage() {
		super(MessageType.UPDATE_REPS, MessageSubtype.INFO);
	}


	/**
	 * Adds an inner message to this RepMessage.
	 *
	 * @param message the message to add.
	 */
	public void addMessage(Message message) {
		this.message = message;
	}

	/**
	 * Adds a the rep of the GameMap.
	 *
	 * @param gameMapRep the rep of the GameMap.
	 */
	public void addGameMapRep(GameMapRep gameMapRep) {
		this.gameMapRep = gameMapRep;
		hasReps = true;
	}

	/**
	 * Adds a the rep of the GameBoard.
	 *
	 * @param gameBoardRep the rep of the GameBoard.
	 */
	public void addGameBoardRep(GameBoardRep gameBoardRep) {
		this.gameBoardRep = gameBoardRep;
		hasReps = true;
	}

	/**
	 * Adds a rep of a player.
	 *
	 * @param playerRepToAdd a rep of a player.
	 */
	public void addPlayersRep(PlayerRep playerRepToAdd) {
		hasReps = true;
		for (int i = 0; i < playersRep.size(); i++) {
			if (playersRep.get(i).getPlayerName().equals(playerRepToAdd.getPlayerName())) {
				playersRep.set(i, playerRepToAdd);
				return;
			}
		}
		playersRep.add(playerRepToAdd);
	}


	/**
	 * Returns the inner message contained in this RepMessage.
	 *
	 * @return the inner message contained in this RepMessage.
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * Returns the rep of the GameMap.
	 *
	 * @return the rep of the GameMap.
	 */
	public GameMapRep getGameMapRep() {
		return gameMapRep;
	}

	/**
	 * Returns the rep of the GameBoard.
	 *
	 * @return the rep of the GameBoard.
	 */
	public GameBoardRep getGameBoardRep() {
		return gameBoardRep;
	}

	/**
	 * Returns a list containing the reps of the players.
	 *
	 * @return a list containing the reps of the players.
	 */
	public List<PlayerRep> getPlayersRep() {
		return playersRep;
	}

	/**
	 * Returns true if this RepMessage contains reps.
	 *
	 * @return true if this RepMessage contains reps.
	 */
	public boolean hasReps() {
		return hasReps;
	}
}
