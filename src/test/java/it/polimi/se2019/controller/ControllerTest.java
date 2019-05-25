package it.polimi.se2019.controller;

import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.view.server.VirtualView;
import it.polimi.se2019.view.server.VirtualViewDriver;

import java.util.ArrayList;

public class ControllerTest {

	private static final int NUMBER_OF_PLAYERS_IN_TEST = GameConstants.MAX_PLAYERS;


	/**
	 * Run this main to start a test match.
	 */
	public static void main(String[] args) {
		// Create virtualViews.
		ArrayList<VirtualView> virtualViewDrivers = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_PLAYERS_IN_TEST; i++) {
			String nickname = "test" +  i;
			VirtualViewDriver virtualViewDriver =  new VirtualViewDriver(nickname);
			virtualViewDrivers.add(virtualViewDriver);
		}

		// Create Controller.
		(new Controller(GameConstants.MapType.SMALL_MAP, virtualViewDrivers, 5)).startGame();
	}

}