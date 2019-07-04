package it.polimi.se2019.view.client.cli;

import it.polimi.se2019.model.cards.CardRep;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCardRep;
import it.polimi.se2019.model.cards.weapons.WeaponRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.model.player.damagestatus.DamageStatusRep;
import it.polimi.se2019.network.client.Client;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.*;
import it.polimi.se2019.view.client.RemoteView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.se2019.view.client.cli.CLILoginPrinter.*;

/**
 * Implements the Remote view for the Cli.
 * @author MarcerAndrea
 * @author Desno365
 * @author Marchingegno
 */
public class CLIView extends RemoteView {

	private Scanner scanner = new Scanner(System.in);
	private RepPrinter repPrinter = new RepPrinter(getModelRep());

	static void print(String string) {
		System.out.print(string);
	}

	static void printLine(String string) {
		System.out.println(string);
	}

	@Override
	public void askForConnectionAndStartIt() {
		printChooseConnection();
		int typeOfConnection = Integer.parseInt(waitForChoiceInMenu("1", "2"));

		Scanner scanner = new Scanner(System.in);
		printIpAddressChoice();
		String ipAddress = scanner.nextLine();

		if (typeOfConnection == 1)
			startConnectionWithRMI(ipAddress);
		else
			startConnectionWithSocket(ipAddress);
	}

	@Override
	public void failedConnectionToServer() {
		printLine("Failed to connect to the server. Try again later.");
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
		new Thread(() -> printWaitingMatchStart(delayInMs)).start();
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
		printWaitingRoom(players);
		GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.ANSWER);
		gameConfigMessage.setMapIndex(mapIndex);
		gameConfigMessage.setSkulls(skulls);
		sendMessage(gameConfigMessage);
	}

	@Override
	public void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType) {
		Utils.logInfo("CLIView -> showMapAndSkullsInUse(): Average of voted skulls: " + skulls + ", most voted map " + mapType.toString() + ".");
	}


	@Override
	public void askAction(boolean activablePowerups, boolean activableWeapons) {
		DamageStatusRep damageStatusRep = getModelRep().getClientPlayerRep().getDamageStatusRep();

		printLine("Choose an action!");
		int macroActionTotal = getModelRep().getClientPlayerRep().getDamageStatusRep().getNumberOfMacroActionsPerTurn();
		int macroActionsNum = macroActionTotal - getModelRep().getClientPlayerRep().getDamageStatusRep().getNumberOfMacroActionsToPerform() + 1;
		printLine("Action " + macroActionsNum + " of " + macroActionTotal + ".");

		List<Integer> possibleAnswers = new ArrayList<>();
		int i;
		for (i = 0; i < damageStatusRep.numOfMacroActions(); i++) {
			if (!activableWeapons && damageStatusRep.isShootWithoutReload(i)) {
				printLine("X) No weapons loaded");
			} else {
				possibleAnswers.add(i);
				printLine((i + 1) + ") " + damageStatusRep.getMacroActionName(i) + " " + damageStatusRep.getMacroActionString(i));
			}
		}
		if (activablePowerups)
			possibleAnswers.add(i);
		printLine((activablePowerups ? ((i + 1) + ") Powerup") : "X) No powerup activable"));

		int answer = askIntegerFromList(possibleAnswers, -1);
		if (answer == i) // If answer is powerup.
			sendMessage(new Message(MessageType.ACTIVATE_ON_TURN_POWERUP, MessageSubtype.ANSWER));
		else
			sendMessage(new IntMessage(answer, MessageType.ACTION, MessageSubtype.ANSWER));
	}

	@Override
	public void askGrabWeapon(List<Integer> indexesOfTheGrabbableWeapons) {
		List<CardRep> weaponCards = getModelRep().getGameMapRep().getPlayerSquare(getNickname()).getCards();
		printLine("Select the weapon to grab:");
		for (int i = 0; i < weaponCards.size(); i++) {
			if (indexesOfTheGrabbableWeapons.contains(i))
				printLine((i + 1) + ") " + repPrinter.getWeaponRepString(((WeaponRep) weaponCards.get(i))));
		}
		sendMessage(new IntMessage(askIntegerFromList(indexesOfTheGrabbableWeapons, -1), MessageType.GRAB_WEAPON, MessageSubtype.ANSWER));
	}

	@Override
	public void askSwapWeapon(List<Integer> indexesOfTheGrabbableWeapons) {
		int indexOfTheWeaponToDiscard;
		int indexOfTheWeaponToGrab;
		List<CardRep> weaponsInSpawn = getModelRep().getGameMapRep().getPlayerSquare(getNickname()).getCards();
		List<WeaponRep> weaponsOfThePlayer = getModelRep().getClientPlayerRep().getWeaponReps();

		printLine("Select the weapon to grab:");
		for (int i = 0; i < weaponsInSpawn.size(); i++) {
			if (indexesOfTheGrabbableWeapons.contains(i))
				printLine((i + 1) + ") " + repPrinter.getWeaponRepString(((WeaponRep) weaponsInSpawn.get(i))));
		}
		indexOfTheWeaponToGrab = askIntegerFromList(indexesOfTheGrabbableWeapons, -1);

		printLine("Select the weapon to discard:");
		for (int i = 0; i < weaponsOfThePlayer.size(); i++) {
			printLine((i + 1) + ") " + repPrinter.getWeaponRepString(weaponsOfThePlayer.get(i)));
		}
		indexOfTheWeaponToDiscard = askInteger(1, weaponsOfThePlayer.size()) - 1;

		sendMessage(new SwapMessage(indexOfTheWeaponToGrab, indexOfTheWeaponToDiscard));
	}

	@Override
	public void askMove(List<Coordinates> reachableCoordinates) {
		repPrinter.displayGame(reachableCoordinates);
		printLine("Enter the coordinates in which you want to move.");
		Coordinates answer = askCoordinates(reachableCoordinates);
		sendMessage(new CoordinatesAnswerMessage(answer, MessageType.MOVE));
	}

	@Override
	public void askShoot(List<Integer> shootableWeapons) {
		List<WeaponRep> weaponReps = getModelRep().getClientPlayerRep().getWeaponReps();
		printLine("Select the weapon to use:");
		for (int i = 0; i < weaponReps.size(); i++) {
			if (shootableWeapons.contains(i))
				printLine((i + 1) + ") " + weaponReps.get(i).getCardName());
		}
		int answer = askIntegerFromList(shootableWeapons, -1);
		sendMessage(new IntMessage(answer, MessageType.WEAPON, MessageSubtype.ANSWER));
	}

	@Override
	public void askReload(List<Integer> loadableWeapons) {
		printLine("Which weapon do you want to reload?");
		List<WeaponRep> weaponReps = getModelRep().getClientPlayerRep().getWeaponReps();
		for (int i = 0; i < weaponReps.size(); i++) {
			if (loadableWeapons.contains(i)) {
				printLine((i + 1) + ") " + weaponReps.get(i).getCardName());
			}
		}
		int answer = askIntegerFromList(loadableWeapons, -1);
		// Send a message to the server with the answer for the request. The server will process it in the VirtualView class.
		sendMessage(new IntMessage(answer, MessageType.RELOAD, MessageSubtype.ANSWER));
	}

	@Override
	public void askSpawn() {
		List<PowerupCardRep> powerupCards = getModelRep().getClientPlayerRep().getPowerupCards();
		printLine("Select the Powerup card to discard in order to spawn:");
		for (int i = 0; i < powerupCards.size(); i++)
			printLine((i + 1) + ") " + powerupCards.get(i).getCardName() + Color.getColoredString(" ●", powerupCards.get(i).getAssociatedAmmo().getCharacterColorType()));
		int answer = askInteger(1, powerupCards.size());

		// Send a message to the server with the answer for the request. The server will process it in the VirtualView class.
		sendMessage(new IntMessage(answer - 1, MessageType.SPAWN, MessageSubtype.ANSWER));
	}

	@Override
	public void askWeaponChoice(QuestionContainer questionContainer) {
		askQuestionContainerAndSendAnswer(questionContainer, MessageType.WEAPON);
	}

	@Override
	public void askPowerupActivation(List<Integer> activablePowerups) {
		List<PowerupCardRep> powerupCards = getModelRep().getClientPlayerRep().getPowerupCards();
		printLine("Select the Powerup card to activate:");

		// Cancel option.
		activablePowerups.add(-1);
		printLine("0) Cancel");

		// Activable powerups options.
		for (int i = 0; i < powerupCards.size(); i++) {
			if (activablePowerups.contains(i))
				printLine((i + 1) + ") " + powerupCards.get(i).getCardName() + Color.getColoredString(" ●", powerupCards.get(i).getAssociatedAmmo().getCharacterColorType()));
		}

		// Send answer.
		int answer = askIntegerFromList(activablePowerups, -1);
		sendMessage(new IntMessage(answer, MessageType.POWERUP, MessageSubtype.ANSWER));
	}

	@Override
	public void askOnDamagePowerupActivation(List<Integer> activablePowerups, String shootingPlayer) {
		printLine("You have just been damaged by " + shootingPlayer + "!");
		printLine("Do you want to use a powerup for revenge? [yes/no]");
		boolean usePowerup = askBoolean();
		if (usePowerup)
			askPowerupActivation(activablePowerups);
		else
			sendMessage(new IntMessage(-1, MessageType.POWERUP, MessageSubtype.ANSWER));
	}

	@Override
	public void askPowerupChoice(QuestionContainer questionContainer) {
		askQuestionContainerAndSendAnswer(questionContainer, MessageType.POWERUP);
	}

	@Override
	public void askEnd(boolean activablePowerups) {
		printLine("Choose an action!");
		printLine("1) End turn");
		printLine("2) Reload");
		printLine((activablePowerups ? ("3) Powerup") : "X) No powerup activable"));

		int answer = (activablePowerups ? askInteger(1, 3) : askInteger(1, 2));
		if (answer == 1)
			sendMessage(new Message(MessageType.END_TURN, MessageSubtype.ANSWER)); // End turn.
		else if (answer == 2)
			sendMessage(new Message(MessageType.RELOAD, MessageSubtype.REQUEST)); // Reload.
		else if (answer == 3)
			sendMessage(new Message(MessageType.ACTIVATE_ON_TURN_POWERUP, MessageSubtype.ANSWER)); // Powerup activation.
	}

	@Override
	public void endOfGame(List<PlayerRepPosition> finalPlayersInfo) {

		CLILoginPrinter.cleanConsole();

		printLine("" +
				"\t\t\t\t\t\t _______  _______  __   __  _______    _______  __   __  _______  ______   \n" +
				"\t\t\t\t\t\t|       ||   _   ||  |_|  ||       |  |       ||  | |  ||       ||    _ |  \n" +
				"\t\t\t\t\t\t|    ___||  |_|  ||       ||    ___|  |   _   ||  |_|  ||    ___||   | ||  \n" +
				"\t\t\t\t\t\t|   | __ |       ||       ||   |___   |  | |  ||       ||   |___ |   |_||_ \n" +
				"\t\t\t\t\t\t|   ||  ||       ||       ||    ___|  |  |_|  ||       ||    ___||    __  |\n" +
				"\t\t\t\t\t\t|   |_| ||   _   || ||_|| ||   |___   |       | |     | |   |___ |   |  | |\n" +
				"\t\t\t\t\t\t|_______||__| |__||_|   |_||_______|  |_______|  |___|  |_______||___|  |_|\n\n\n");

		CLIView.printLine("\t\t\t\t\t\t\t\t┌───┬────────────────────────────────┬─────┐");
		for (int i = 0; i < finalPlayersInfo.size(); i++) {
			for (PlayerRep playerRep : finalPlayersInfo.get(i).getPlayerReps()) {
				printLine("\t\t\t\t\t\t\t\t│ " + (i + 1) + " │ " + Utils.fillWithSpacesColored(playerRep.getPlayerName(), 30, playerRep.getPlayerColor()) + " │ " + Utils.fillWithSpaces(Integer.toString(playerRep.getPoints()), 4) + "│");
			}
		}
		CLIView.printLine("\t\t\t\t\t\t\t\t└───┴────────────────────────────────┴─────┘");

		// Display winner(s).
		if (finalPlayersInfo.get(0).getPlayerReps().size() > 1)
			printLine("The winners are...");
		else
			printLine("The winner is...");
		for (PlayerRep playerRep : finalPlayersInfo.get(0).getPlayerReps()) {
			printLine(Color.getColoredString(playerRep.getPlayerName(), playerRep.getPlayerColor()));
		}

		// Display ranking.
//		for (int i = 0; i < finalPlayersInfo.size(); i++) {
//			for (PlayerRep playerRep : finalPlayersInfo.get(i).getPlayerReps()) {
//				printLine((i + 1) + ") " + Color.getColoredString(playerRep.getPlayerName(), playerRep.getPlayerColor()) + ": " + playerRep.getPoints() + " points.");
//			}
//		}

		Client.terminateClient();
	}

	/**
	 * Displays the main game board
	 */
	public void updateDisplay() {
		repPrinter.displayGame();
	}


	@Override
	public void askToPay(List<AmmoType> price, boolean canAffordAlsoWithAmmo) {
		List<Integer> answer = new ArrayList<>();
		List<AmmoType> priceToPay = new ArrayList<>(price);
		List<Integer> usablePowerups = new ArrayList<>();
		List<PowerupCardRep> playerPowerups = getModelRep().getClientPlayerRep().getPowerupCards();
		List<PowerupCardRep> remainingPowerups = new ArrayList<>(playerPowerups);

		int choice = -1;
		boolean canAffordAlsoWithOnlyAmmo = canAffordAlsoWithOnlyAmmo(price);
		int numOfOptions = 0;
		int index = 1;

		printLine("Which powerup you want to discard to pay?");
		if (canAffordAlsoWithOnlyAmmo) {
			printLine("0) End payment");
			numOfOptions++;
		}


		for (int i = 0; i < playerPowerups.size(); i++) {
			if (remainingPowerups.contains(playerPowerups.get(i)) && priceToPay.contains(playerPowerups.get(i).getAssociatedAmmo())) {
				printLine((index) + ") " + repPrinter.getPowerupRepString(playerPowerups.get(i)));
				usablePowerups.add(i);
				numOfOptions++;
				index++;
			}
		}

		while (!usablePowerups.isEmpty() && choice != 0) {
			Utils.logInfo("CLIView -> askToPay: price " + priceToPay + " with  " + remainingPowerups);
			choice = askInteger(canAffordAlsoWithOnlyAmmo ? 0 : 1, numOfOptions - (canAffordAlsoWithOnlyAmmo ? 1 : 0));
			if (choice != 0) {
				Utils.logInfo("CLIView -> askToPay(): player has chosen " + playerPowerups.get(usablePowerups.get(choice - 1)) + " index of the usable powerups " + (choice - 1));
				Utils.logInfo("CLIView -> askToPay(): removing from price " + playerPowerups.get(usablePowerups.get(choice - 1)).getAssociatedAmmo());
				Utils.logInfo("CLIView -> askToPay(): adding to answer " + usablePowerups.get(choice - 1));
				priceToPay.remove(playerPowerups.get(usablePowerups.get(choice - 1)).getAssociatedAmmo());
				answer.add(usablePowerups.get(choice - 1));
				remainingPowerups.remove(playerPowerups.get(usablePowerups.get(choice - 1)));
				usablePowerups = new ArrayList<>();
				numOfOptions = 0;
				index = 1;
				canAffordAlsoWithOnlyAmmo = canAffordAlsoWithOnlyAmmo(priceToPay);
				if (canAffordAlsoWithOnlyAmmo) {
					numOfOptions++;
					printLine("0) End payment");
				}
				for (int i = 0; i < playerPowerups.size(); i++) {
					if (remainingPowerups.contains(playerPowerups.get(i)) && priceToPay.contains(playerPowerups.get(i).getAssociatedAmmo())) {
						usablePowerups.add(i);
						printLine((index) + ") " + repPrinter.getPowerupRepString(playerPowerups.get(i)));
						numOfOptions++;
						index++;
					}
				}
			} else {
				Utils.logInfo("CLIView -> askToPay(): player decided to use ammo");
			}
			Utils.logInfo("CLIView -> askToPay(): indexes of usable powerups " + usablePowerups);
		}
		sendMessage(new PaymentMessage(priceToPay, MessageSubtype.ANSWER).setPowerupsUsed(answer));
	}

	private boolean canAffordAlsoWithOnlyAmmo(List<AmmoType> price) {
		List<AmmoType> priceToPay = new ArrayList<>(price);
		List<AmmoType> playerAmmo = new ArrayList<>();
		for (int i = 0; i < getModelRep().getClientPlayerRep().getAmmo().length; i++) {
			for (int j = 0; j < getModelRep().getClientPlayerRep().getAmmo()[i]; j++) {
				playerAmmo.add(AmmoType.values()[i]);
			}
		}
		Utils.logInfo("CLIView -> canAffordAlsoWithOnlyAmmo(): player ammo " + playerAmmo + " price to pay " + priceToPay);

		for (int i = priceToPay.size() - 1; i >= 0; i--) {
			if (playerAmmo.contains(priceToPay.get(i))) {
				playerAmmo.remove(priceToPay.get(i));
				priceToPay.remove(i);
			} else {
				Utils.logInfo("CLIView -> canAffordAlsoWithOnlyAmmo(): player ammo " + playerAmmo + " price to pay " + priceToPay + " => player cannot afford");
				return false;
			}
		}
		Utils.logInfo("CLIView -> canAffordAlsoWithOnlyAmmo(): player ammo " + playerAmmo + " remaining price to pay " + priceToPay + " => player can afford");

		return priceToPay.isEmpty();
	}

	private int askMapToUse() {
		printChooseMap();
		ArrayList<String> possibleChoices = new ArrayList<>();
		for (int i = 1; i <= GameConstants.MapType.values().length; i++) {
			possibleChoices.add(Integer.toString(i));
		}
		return Integer.parseInt(waitForChoiceInMenu(possibleChoices)) - 1;
	}

	private int askSkullsForGame() {
		printChooseSkulls();
		ArrayList<String> possibleChoices = new ArrayList<>();
		for (int i = GameConstants.MIN_SKULLS; i <= GameConstants.MAX_SKULLS; i++)
			possibleChoices.add(Integer.toString(i));
		return Integer.parseInt(waitForChoiceInMenu(possibleChoices));
	}

	private void askQuestionContainerAndSendAnswer(QuestionContainer questionContainer, MessageType messageType) {
		int answer = -1;
		if (questionContainer.isAskString()) {
			// Options question.
			printLine(questionContainer.getQuestion());
			for (int i = 1; i < questionContainer.getOptions().size() + 1; i++) {
				printLine(i + ") " + questionContainer.getOptions().get(i - 1));
			}
			answer = askInteger(1, questionContainer.getOptions().size());
			answer = answer - 1;
		} else if (questionContainer.isAskCoordinates()) {
			// Coordinates question.
			repPrinter.displayGame(questionContainer.getCoordinates());
			printLine(questionContainer.getQuestion());
			Coordinates coordinates = askCoordinates(questionContainer.getCoordinates());
			answer = questionContainer.getCoordinates().indexOf(coordinates);
		}
		sendMessage(new IntMessage(answer, messageType, MessageSubtype.ANSWER));
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
		int input = 0;
		boolean ok;
		do {
			try {
				input = Integer.parseInt(scanner.nextLine());
				ok = true;
			} catch (NumberFormatException e) {
				ok = false;
			}
			if (!ok || input < minInclusive || input > maxInclusive) { // ok must be true and input must be between min and max.
				printLine("The value must be between " + minInclusive + " and " + maxInclusive + ".");
			}
		} while (!ok || input < minInclusive || input > maxInclusive);
		return input;
	}

	/**
	 * Ask the user an integer that must be in the options list.
	 * Repeatedly ask the integer if the input is the list.
	 *
	 * @param options the list containing the possible options.
	 * @param offset  number to add to the answer before checking if it is contained in the list.
	 * @return the integer chosen by the user + the offset.
	 */
	private int askIntegerFromList(List<Integer> options, int offset) {
		int input = 0;
		boolean ok;
		do {
			try {
				input = Integer.parseInt(scanner.nextLine());
				input += offset;
				ok = true;
			} catch (NumberFormatException e) {
				ok = false;
			}
			if (!ok || !options.contains(input)) { // ok must be true and input must be in the options list.
				printLine("The value must be in the options.");
			}
		} while (!ok || !options.contains(input));
		return input;
	}

	private Coordinates askCoordinates(List<Coordinates> reachableCoordinates) {
		Coordinates coordinates;
		do {
			printLine("Enter Row coordinate 1-" + getModelRep().getGameMapRep().getNumOfRows());
			int x = askInteger(1, getModelRep().getGameMapRep().getNumOfRows());
			printLine("Enter Column coordinate 1-" + getModelRep().getGameMapRep().getNumOfColumns());
			int y = askInteger(1, getModelRep().getGameMapRep().getNumOfColumns());
			coordinates = new Coordinates(x - 1, y - 1);
			CLILoginPrinter.moveCursorUP(4);
			CLILoginPrinter.cleanConsole();
		} while (!reachableCoordinates.contains(coordinates));

		return coordinates;
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


