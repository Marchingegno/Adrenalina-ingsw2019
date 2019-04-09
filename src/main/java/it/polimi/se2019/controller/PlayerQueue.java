package it.polimi.se2019.controller;

import it.polimi.se2019.model.player.Player;

import java.util.ArrayDeque;
import java.util.List;

public class PlayerQueue<Player> extends ArrayDeque {


	public PlayerQueue(List<Player> playerQueue) {
		super(playerQueue);
	}

	public void moveToBottom() {
	}

}