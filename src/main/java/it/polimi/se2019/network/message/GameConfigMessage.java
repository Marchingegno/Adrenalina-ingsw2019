package it.polimi.se2019.network.message;

import it.polimi.se2019.utils.GameConstants;

/**
 * Message used to request and to display the configurations of the game.
 */
public class GameConfigMessage extends Message {

	private int mapIndex;
	private int skulls;


	/**
	 * Constructs a message.
	 * @param messageSubtype the messageSubtype of the message.
	 */
	public GameConfigMessage(MessageSubtype messageSubtype) {
		super(MessageType.GAME_CONFIG, messageSubtype);
	}


	/**
	 * Returns the map index, to choose which map to play.
	 * @return the map index, to choose which map to play.
	 */
	public int getMapIndex() {
		return mapIndex;
	}

	/**
	 * Sets the map index, to choose which map to play.
	 * @param mapIndex the map index, to choose which map to play.
	 */
	public void setMapIndex(int mapIndex) {
		if(mapIndex > GameConstants.MapType.values().length - 1)
			mapIndex = GameConstants.MapType.values().length - 1;
		if(mapIndex < 0)
			mapIndex = 0;
		this.mapIndex = mapIndex;
	}

	/**
	 * Returns the skulls.
	 * @return the skulls.
	 */
	public int getSkulls() {
		return skulls;
	}

	/**
	 * Sets the skulls.
	 * @param skulls the skulls.
	 */
	public void setSkulls(int skulls) {
		if(skulls > GameConstants.MAX_SKULLS)
			skulls = GameConstants.MAX_SKULLS;
		if(skulls < GameConstants.MIN_SKULLS)
			skulls = GameConstants.MIN_SKULLS;
		this.skulls = skulls;
	}

}
