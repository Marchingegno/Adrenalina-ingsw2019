package it.polimi.se2019.view.client;

import it.polimi.se2019.model.cards.powerups.PowerupCardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.damagestatus.DamageStatusRep;
import it.polimi.se2019.network.client.Client;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.se2019.view.client.CLIPrinter.*;

/**
 * @author MarcerAndrea
 * @author Desno365
 */
public class CLIView extends RemoteView {

	private Scanner scanner = new Scanner(System.in);
	private RepPrinter repPrinter = new RepPrinter(getModelRep());

	public static void print(String string) {
		System.out.print(string);
	}

	public static void printLine(String string) {
		System.out.println(string);
	}

	@Override
	public void askForConnectionAndStartIt() {
		printChooseConnection();
		if (Integer.parseInt(waitForChoiceInMenu("1", "2")) == 1)
			startConnectionWithRMI();
		else
			startConnectionWithSocket();
	}

	@Override
	public void failedConnectionToServer() {
		print("Failed to connect to the server. Try again later.");
		Client.terminateClient();
	}

	@Override
	public void askNickname() {
		printChooseNickname();
		String chosenNickname = scanner.nextLine();
		sendMessage(new NicknameMessage(chosenNickname, MessageSubtype.ANSWER));
	}

	@Override
	public void lostConnectionToServer() {
		printLine("Lost connection with the server. Please restart the game.");
		Client.terminateClient();
	}

	@Override
	public void askNicknameError() {
		printLine("The nickname already exists or is not valid, please use a different one.");
		askNickname();
	}

	@Override
	public void displayWaitingPlayers(List<String> waitingPlayers) {
		printWaitingRoom(waitingPlayers);
	}

	@Override
	public void displayTimerStarted(long delayInMs) {
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setMaximumFractionDigits(1);
		printWaitingMatchStart(delayInMs);
		//printLine("The match will start in " + decimalFormat.format(delayInMs / 1000d) + " seconds...");
	}

	@Override
	public void displayTimerStopped() {
		printLine(Color.getColoredString("Timer for starting the match cancelled.", Color.CharacterColorType.RED));
	}

	@Override
	public void askMapAndSkullsToUse() {
		Utils.logInfo("\n\nMatch ready to start. Select your preferred configuration.");
		int mapIndex = askMapToUse();
		int skulls = askSkullsForGame();
		ArrayList<String> players = new ArrayList<>();
		players.add(getNickname());
		printWaitingRoom(players);
		GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.ANSWER);
		gameConfigMessage.setMapIndex(mapIndex);
		gameConfigMessage.setSkulls(skulls);
		sendMessage(gameConfigMessage);
	}

	@Override
	public void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType) {
		Utils.logInfo("CLIView => showMapAndSkullsInUse(): Average of voted skulls: " + skulls + ", most voted map " + mapType.toString() + ".");
	}

	@Override
	public void askAction() {
		DamageStatusRep damageStatusRep = getModelRep().getClientPlayerRep().getDamageStatusRep();

		printLine("Choose an action!");
		for (int i = 0; i < damageStatusRep.numOfMacroActions(); i++)
			printLine((i + 1) + ") " + damageStatusRep.getMacroActionName(i) + " " + damageStatusRep.getMacroActionString(i));
		int answer = askInteger(1, damageStatusRep.numOfMacroActions());

		// Send a message to the server with the answer for the request. The server will process it in the VirtualView class.
		sendMessage(new DefaultActionMessage(answer - 1, MessageType.ACTION, MessageSubtype.ANSWER));

	}

	@Override
	public void askGrab() {
		//TODO: Check whether the player is in a spawn square or a weapon square.
		getModelRep().getGameMapRep().getPlayerCoordinates(getNickname());
		printLine("You chose to grab.");
		printLine("Select a number between 0 and 2.");
		printLine("You chose 0! <:)");
		//int answer = askInteger(0, 2);
		// Send a message to the server with the answer for the request. The server will process it in the VirtualView class.
		sendMessage(new DefaultActionMessage(0, MessageType.GRAB_AMMO, MessageSubtype.ANSWER));
	}

	@Override
	public void askMove(List<Coordinates> reachableCoordinates) {
		repPrinter.displayGame(reachableCoordinates);
		printLine("Enter the coordinates in which you want to move.");
		List<Coordinates> answer = askCoordinates(reachableCoordinates);
		sendMessage(new MoveActionMessage(answer, MessageSubtype.ANSWER));
	}

	@Override
	public void askShoot() {
		printLine("LOL");
		askEnd();
	}

	@Override
	public void askReload() {
		printLine("Which weapon do you want to reload?");
		printLine("Select a number between 0 and 2.");
		int answer = askInteger(0, 2);
		// Send a message to the server with the answer for the request. The server will process it in the VirtualView class.
		sendMessage(new DefaultActionMessage(answer, MessageType.RELOAD, MessageSubtype.ANSWER));
	}

	@Override
	public void askEnd() {
		sendMessage(new Message(MessageType.END_TURN, MessageSubtype.ANSWER));
	}

	@Override
	public void askSpawn() {
		List<PowerupCardRep> powerupCards = getModelRep().getClientPlayerRep().getPowerupCards();
		printLine("Select the Powerup card to discard in order to spawn: ");
		for (int i = 0; i < powerupCards.size(); i++)
			printLine((i + 1) + ") " + powerupCards.get(i).getCardName() + Color.getColoredString(" ●", powerupCards.get(i).getAssociatedAmmo().getCharacterColorType()));
		int answer = askInteger(1, powerupCards.size());

		// Send a message to the server with the answer for the request. The server will process it in the VirtualView class.
		sendMessage(new DefaultActionMessage(answer - 1, MessageType.SPAWN, MessageSubtype.ANSWER));
	}

	private int askMapToUse() {
		printChooseMap();
		ArrayList<String> possibleChoices = new ArrayList<>();
		for (int i = 1; i <= GameConstants.MapType.values().length; i++) {
			possibleChoices.add(Integer.toString(i));
		}
		return Integer.parseInt(waitForChoiceInMenu(possibleChoices));
		//return askInteger(0, GameConstants.MapType.values().length - 1);
	}

	private int askSkullsForGame() {
		printChooseSkulls();
		ArrayList<String> possibleChoices = new ArrayList<>();
		for (int i = GameConstants.MIN_SKULLS; i <= GameConstants.MAX_SKULLS; i++)
			possibleChoices.add(Integer.toString(i));
		return Integer.parseInt(waitForChoiceInMenu(possibleChoices));
		//printLine("Select how many skulls you would like to use, min " + GameConstants.MIN_SKULLS + ", max " + GameConstants.MAX_SKULLS + ".");
		//return askInteger(GameConstants.MIN_SKULLS, GameConstants.MAX_SKULLS);
	}

	/**
	 * Displays the main game board
	 */
	public void updateDisplay() {
		repPrinter.displayGame();
	}

	/**
	 * Ask the user an integer that must be between minInclusive and maxInclusive.
	 * Repeatedly ask the integer if the input is not in the limits.
	 *
	 * @param minInclusive the minimum limit.
	 * @param maxInclusive the maximum limit.
	 * @return the integer chosen by the user.
	 */
	private int askInteger(int minInclusive, int maxInclusive) {
		int input = -1;
		while (input < minInclusive || input > maxInclusive) {
			try {
				input = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				input = -1;
			} finally {
				if (input < minInclusive || input > maxInclusive)
					printLine("The value must be between " + minInclusive + " and " + maxInclusive + ".");
			}
		}
		return input;
	}

	private List<Coordinates> askCoordinates(List<Coordinates> reachableCoordinates) {
		Coordinates coordinates;
		do {
			printLine("Enter Row coordinate 1-" + getModelRep().getGameMapRep().getNumOfRows());
			int x = askInteger(1, getModelRep().getGameMapRep().getNumOfRows());
			printLine("Enter Column coordinate 1-" + getModelRep().getGameMapRep().getNumOfColumns());
			int y = askInteger(1, getModelRep().getGameMapRep().getNumOfColumns());
			coordinates = new Coordinates(x - 1, y - 1);
		} while (!reachableCoordinates.contains(coordinates));

		List<Coordinates> coordinatesList = new ArrayList<>();
		coordinatesList.add(coordinates);
		return coordinatesList;
	}

	/**
	 * Ask the user a boolean.
	 *
	 * @return the boolean chosen by the user.
	 */
	private boolean askBoolean() {
		String input = "";
		while (!(input.equals("n") || input.equals("y") || input.equals("yes") || input.equals("no"))) {
			input = scanner.nextLine().toLowerCase();
			if (!(input.equals("n") || input.equals("y") || input.equals("yes") || input.equals("no")))
				printLine("Please write \"y\" or \"n\".");
		}
		return input.equals("y") || input.equals("yes");
	}
}


