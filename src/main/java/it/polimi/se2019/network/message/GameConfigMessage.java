package it.polimi.se2019.network.message;

import it.polimi.se2019.utils.GameConstants;

public class GameConfigMessage extends Message {

	private int mapIndex;
	private int skulls;


	public GameConfigMessage(MessageSubtype messageSubtype) {
		super(MessageType.GAME_CONFIG, messageSubtype);
	}


	public int getMapIndex() {
		return mapIndex;
	}

	public void setMapIndex(int mapIndex) {
		if(mapIndex > GameConstants.MapType.values().length - 1)
			mapIndex = GameConstants.MapType.values().length - 1;
		if(mapIndex < 0)
			mapIndex = 0;
		this.mapIndex = mapIndex;
	}

	public int getSkulls() {
		return skulls;
	}

	public void setSkulls(int skulls) {
		if(skulls > GameConstants.MAX_SKULLS)
			skulls = GameConstants.MAX_SKULLS;
		if(skulls < GameConstants.MIN_SKULLS)
			skulls = GameConstants.MIN_SKULLS;
		this.skulls = skulls;
	}

}
