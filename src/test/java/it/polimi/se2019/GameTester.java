package it.polimi.se2019;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.view.server.VirtualView;
import it.polimi.se2019.view.server.VirtualViewDriver;
import it.polimi.se2019.view.server.VirtualViewDriverAsync;
import it.polimi.se2019.view.server.VirtualViewDriverSync;
import org.junit.Test;

import java.util.ArrayList;

public class GameTester {

	private static final int NUMBER_OF_PLAYERS_IN_TEST = GameConstants.MAX_PLAYERS;

	// Note: these variables are only used in the manually started test.
	private static final boolean TEST_SHOOT = true;
	private static final boolean TEST_MOVE = true; // Set to false if you think "Move" is a useless action.
	private static final boolean DISPLAY_REPS = true;


	/**
	 * Run this main to start an asynchronous test game.
	 * This test can be personalized using the final attributes in the class.
	 */
	public static void main(String[] args) {
		// Create virtualViews.
		ArrayList<VirtualView> virtualViewDrivers = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_PLAYERS_IN_TEST; i++) {
			String nickname = "test" +  i;
			VirtualViewDriver virtualViewDriver = new VirtualViewDriverAsync(nickname, TEST_SHOOT, TEST_MOVE);
			if(DISPLAY_REPS && i == 0)
				virtualViewDriver.setDisplayReps(true);
			virtualViewDrivers.add(virtualViewDriver);
		}

		// Create Controller.
		(new Controller(GameConstants.MapType.SMALL_MAP, virtualViewDrivers, 5)).startGame();
	}


	/**
	 * This test runs a single synchronous game with random responses.
	 * This test is automatically started when compiling so its test variables are set and shouldn't be changed.
	 * DISPLAY_REPS = false, since we don't want to have too many logs;
	 * TEST_SHOOT = true, TEST_MOVE = true, since we want to test everything.
	 */
	@Test
	public void runTestGame() {
		// Create virtualViews.
		ArrayList<VirtualView> virtualViewDrivers = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_PLAYERS_IN_TEST; i++) {
			String nickname = "test" +  i;
			VirtualViewDriver virtualViewDriver = new VirtualViewDriverSync(nickname, false, true);
			virtualViewDrivers.add(virtualViewDriver);
		}

		// Create Controller.
		(new Controller(GameConstants.MapType.SMALL_MAP, virtualViewDrivers, 5)).startGame();
	}

}