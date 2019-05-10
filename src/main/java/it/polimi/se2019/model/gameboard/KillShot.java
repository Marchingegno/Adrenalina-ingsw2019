package it.polimi.se2019.model.gameboard;

import it.polimi.se2019.model.player.Player;

/**
 * Class that represent the marks on the kill shot track.
 *
 * @author Desno365
 */
class KillShot {

	private Player player;
	private boolean overkill;


	KillShot(Player shootingPlayer, boolean overkill) {
		player = shootingPlayer;
		this.overkill = overkill;
	}

	/**
	 * Returns the Player who has done the kill.
	 *
	 * @return the Player who has done the kill.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns true if and only if the kill is also an overkill.
	 *
	 * @return true if and only if the kill is also an overkill.
	 */
	public boolean isOverkill() {
		return overkill;
	}
}
