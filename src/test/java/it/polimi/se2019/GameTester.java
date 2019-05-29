package it.polimi.se2019;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.VirtualView;
import it.polimi.se2019.view.server.VirtualViewDriver;
import it.polimi.se2019.view.server.VirtualViewDriverAsync;
import it.polimi.se2019.view.server.VirtualViewDriverSync;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

public class GameTester {

	// Options for all the tests.
	public static final int MAX_NUMBER_OF_TURNS = 100;

	// Options for the manually started test (main)
	private static final boolean TEST_SHOOT = true;
	private static final boolean TEST_MOVE = false; // Set to false if you think "Move" is a useless action.
	private static final boolean DISPLAY_REPS = true;

	// Options for the automatically started test (runTestGame)
	private static final int NUMBER_OF_GAMES = 10;


	/**
	 * Run this main to start an asynchronous test game.
	 * This test can be personalized using the final attributes in the class.
	 */
	public static void main(String[] args) {
		runSingleAsyncTestGame();
	}


	/**
	 * This test runs a single synchronous game with random responses.
	 * This test is automatically started when compiling so its test variables are set and shouldn't be changed.
	 * DISPLAY_REPS = false, since we don't want to have too many logs;
	 * TEST_SHOOT = true, TEST_MOVE = true, since we want to test everything.
	 */
	@Test
	public void runTestGame() {
		for (int i = 0; i < NUMBER_OF_GAMES; i++) {
			runSingleSyncTestGame();
			Utils.logInfo(Color.getColoredString("####################################", Color.CharacterColorType.GREEN));
			Utils.logInfo(Color.getColoredString("##################", Color.CharacterColorType.GREEN) + " TEST GAME " + (i + 1) + " FINISHED CORRECTLY " + Color.getColoredString("##################", Color.CharacterColorType.GREEN));
			Utils.logInfo(Color.getColoredString("####################################", Color.CharacterColorType.GREEN));
		}
	}


	// ####################################
	// PRIVATE METHODS
	// ####################################

	private static void runSingleAsyncTestGame() {
		// Create virtualViews.
		ArrayList<VirtualView> virtualViewDrivers = new ArrayList<>();
		int numberOfPlayers = getRandomNumberOfPlayers();
		for (int i = 0; i < numberOfPlayers; i++) {
			String nickname = "test" +  i;
			VirtualViewDriver virtualViewDriver = new VirtualViewDriverAsync(nickname, TEST_SHOOT, TEST_MOVE);
			if(DISPLAY_REPS && i == 0)
				virtualViewDriver.setDisplayReps(true);
			virtualViewDrivers.add(virtualViewDriver);
		}

		// Create Controller.
		(new Controller(getRandomMapType(), virtualViewDrivers, getRandomSkulls())).startGame();
	}

	private static void runSingleSyncTestGame() {
		// Create virtualViews.
		ArrayList<VirtualView> virtualViewDrivers = new ArrayList<>();
		int numberOfPlayers = getRandomNumberOfPlayers();
		for (int i = 0; i < numberOfPlayers; i++) {
			String nickname = "test" +  i;
			VirtualViewDriver virtualViewDriver = new VirtualViewDriverSync(nickname, false, true);
			virtualViewDrivers.add(virtualViewDriver);
		}

		// Create Controller.
		(new Controller(getRandomMapType(), virtualViewDrivers, getRandomSkulls())).startGame();
	}

	private static GameConstants.MapType getRandomMapType() {
		int randomIndex = new Random().nextInt(GameConstants.MapType.values().length);
		GameConstants.MapType mapType = GameConstants.MapType.values()[randomIndex];
		Utils.logInfo("Randomly chose map: " + mapType.getMapName());
		return mapType;
	}

	private static int getRandomSkulls() {
		int randomNumber = new Random().nextInt(1 + GameConstants.MAX_SKULLS - GameConstants.MIN_SKULLS);
		int skulls = GameConstants.MIN_SKULLS + randomNumber;
		Utils.logInfo("Randomly chose skulls: " + skulls);
		return skulls;
	}

	private static int getRandomNumberOfPlayers() {
		int randomNumber = new Random().nextInt(1 + GameConstants.MAX_PLAYERS - GameConstants.MIN_PLAYERS);
		int nop = GameConstants.MIN_PLAYERS + randomNumber;
		Utils.logInfo("Randomly chose number of players: " + nop);
		return nop;
	}

}