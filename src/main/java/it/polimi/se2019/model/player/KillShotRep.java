package it.polimi.se2019.model.player;

import it.polimi.se2019.utils.Color;

import java.io.Serializable;

public class KillShotRep implements Serializable {

	private String playerName;
	private Color.CharacterColorType playerColor;
	private boolean overkill;


	public KillShotRep(Player killer, boolean overkill) {
		playerName = killer.getPlayerName();
		playerColor = killer.getPlayerColor();
		this.overkill = overkill;
	}

	public String getPlayerName() {
		return playerName;
	}

	public Color.CharacterColorType getPlayerColor() {
		return playerColor;
	}

	public boolean isOverkill() {
		return overkill;
	}

	public boolean equals(Object object){
		return (object instanceof KillShotRep &&
				this.playerName.equals(((KillShotRep) object).playerName) &&
				this.overkill == (((KillShotRep) object).overkill));
	}

	public int hashCode(){
		return playerName.hashCode() +
				playerColor.ordinal() +
				(overkill? 1 : 0 );
	}
}
