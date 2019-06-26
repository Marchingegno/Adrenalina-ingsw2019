package it.polimi.se2019.utils;

import it.polimi.se2019.model.player.PlayerRep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayersPosition implements Serializable {

	private final ArrayList<PlayerRep> playerReps;


	public PlayersPosition() {
		playerReps = new ArrayList<>();
	}


	public List<PlayerRep> getPlayerReps() {
		return playerReps;
	}

	public void addInPosition(PlayerRep playerRep) {
		playerReps.add(playerRep);
	}

}
