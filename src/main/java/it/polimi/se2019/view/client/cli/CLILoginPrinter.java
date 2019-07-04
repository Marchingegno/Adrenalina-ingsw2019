package it.polimi.se2019.view.client.cli;

import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

import java.util.List;
import java.util.Scanner;

import static it.polimi.se2019.view.client.cli.CLIView.print;

/**
 * Prints the login scene in the Cli.
 *
 * @author MarcerAndrea
 */
public class CLILoginPrinter {

	private static final Object lock = new Object();
	private static final String ESC = (char) 27 + "[";
	private static final String CLEAN = ESC + "J";
	private static final String HOME = ESC + "H";

	private static final String TITLE = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\u001b[33;49m" +
			"								       ___       _______  .______       _______ .__   __.      ___       __       __  .__   __. _______ \n" +
			"								      /   \\     |       \\ |   _  \\     |   ____||  \\ |  |     /   \\     |  |     |  | |  \\ |  ||   ____| \n" +
			"								     /  ^  \\    |  .--.  ||  |_)  |    |  |__   |   \\|  |    /  ^  \\    |  |     |  | |   \\|  ||  |__    \n" +
			"								    /  /_\\  \\   |  |  |  ||      /     |   __|  |  . `  |   /  /_\\  \\   |  |     |  | |  . `  ||   __|   \n" +
			"								   /  _____  \\  |  '--'  ||  |\\  \\----.|  |____ |  |\\   |  /  _____  \\  |  `----.|  | |  |\\   ||  |____  \n" +
			"								  /__/     \\__\\ |_______/ | _| `._____||_______||__| \\__| /__/     \\__\\ |_______||__| |__| \\__||_______| \n\n\n\u001b[39;49m";


	static void cleanConsole() {
		print(CLEAN);
		System.out.flush();
	}

	static void setCursorHome() {
		print(HOME);
	}

	private static void moveCursorTo(int row, int column) {
		print(ESC + row + ";" + column + "H");
	}

	static void moveCursorUP(int numOfLines) {
		print(ESC + numOfLines + "A");
	}

	static void moveCursorDOWN(int numOfLines) {
		print(ESC + numOfLines + "C");
	}

	static void moveCursorRIGHT(int numOfLines) {
		print(ESC + numOfLines + "C");
	}

	private static void moveCursorLEFT(int numOfLines) {
		print(ESC + numOfLines + "D");
	}

	public static void printChooseView() {
		if (!Utils.DEBUG_CLI) {
			setCursorHome();
			cleanConsole();
		}
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗\n" +
				"											║                                                                ║\n" +
				"											║                                                                ║\n" +
				"											║                           [1] GUI                              ║\n" +
				"											║                           [2] CLI                              ║\n" +
				"											║                                                                ║\n " +
				"											║                                                                ║\n" +
				"											║                                                                ║\n" +
				"											╚════════════════════════════════════════════════════════════════╝\n");
		moveCursorUP(3);
	}

	static void printChooseConnection() {
		if (!Utils.DEBUG_CLI) {
			setCursorHome();
			cleanConsole();
		}
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗\n" +
				"											║                                                                ║\n" +
				"											║                                                                ║\n" +
				"											║                           [1] RMI                              ║\n" +
				"											║                           [2] Socket                           ║\n" +
				"											║                                                                ║\n " +
				"											║                                                                ║\n" +
				"											║                                                                ║\n" +
				"											╚════════════════════════════════════════════════════════════════╝\n");
		moveCursorUP(3);
	}

	static void printChooseNickname() {
		if (!Utils.DEBUG_CLI) {
			setCursorHome();
			cleanConsole();
		}
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗\n" +
				"											║                                                                ║\n" +
				"											║                                                                ║\n" +
				"											║                           NICKNAME                             ║\n" +
				"											║                                                                ║\n" +
				"											║                                                                ║\n " +
				"											║                                                                ║\n" +
				"											║                                                                ║\n" +
				"											╚════════════════════════════════════════════════════════════════╝\n");
		moveCursorUP(3);
		moveCursorRIGHT(120);
	}

	static void printIpAddressChoice() {
		if (!Utils.DEBUG_CLI) {
			setCursorHome();
			cleanConsole();
		}
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗\n" +
				"											║                                                                ║\n" +
				"											║                                                                ║\n" +
				"											║                      Choose the ip address                     ║\n" +
				"											║                                                                ║\n" +
				"											║                                                                ║\n " +
				"											║                                                                ║\n" +
				"											║                                                                ║\n" +
				"											╚════════════════════════════════════════════════════════════════╝\n");
		moveCursorUP(3);
		moveCursorRIGHT(120);
	}

	static void printChooseMap() {
		if (!Utils.DEBUG_CLI) {
			setCursorHome();
			cleanConsole();
		}
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗\n" +
				"											║                                                                ║\n" +
				"											║                 Choose a map:                                  ║\n");

		GameConstants.MapType[] maps = GameConstants.MapType.values();
		for (int i = 0; i < maps.length; i++) {
			print("											║                   " + Utils.fillWithSpaces("[" + (i + 1) + "] " + maps[i].getMapName(), 45) + "║\n");
		}
		print("											║                                                                ║\n " +
				"											║                                                                ║\n" +
				"											╚════════════════════════════════════════════════════════════════╝\n");
		moveCursorUP(2);
		moveCursorLEFT(100);
	}

	static void printChooseSkulls() {
		if (!Utils.DEBUG_CLI) {
			setCursorHome();
			cleanConsole();
		}
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗ \n" +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											║                        SKULLS [" + GameConstants.MIN_SKULLS + "-" + GameConstants.MAX_SKULLS + "]                            ║ \n" +
				"											║                                                                ║ \n" +
				"											║                                                                ║\n " +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");
		moveCursorUP(3);
	}

	static void printWaitingRoom(List<String> waitingPlayers) {
		synchronized (lock) {
			if (!Utils.DEBUG_CLI) {
				setCursorHome();
				cleanConsole();
			}
			print(TITLE +
					"											╔════════════════════════════════════════════════════════════════╗\n" +
					"											║                                                                ║\n" +
					"											║              Waiting for other clients to connect...           ║\n" +
					"											║                                                                ║\n");
			for (int i = 0; i < waitingPlayers.size(); i++) {
				print("											║                   " + Utils.fillWithSpaces("[" + (i + 1) + "] " + waitingPlayers.get(i), 45) + "║ \n");
			}
			for (int i = waitingPlayers.size(); i <= GameConstants.MAX_PLAYERS; i++) {
				print("											║                                                                ║\n");
			}
			print("											║                                                                ║\n" +
					"											╚════════════════════════════════════════════════════════════════╝\n");
		}
	}

	static void printWaitingMatchStart(long milliSeconds) {
		for (long i = milliSeconds / 1000; i > 0; i--) {
			synchronized (lock) {
				setCursorHome();
				moveCursorTo(27, 145);
				print(Utils.fillWithSpaces(Long.toString(i), 3));
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Utils.logInfo("Error in match countdown");
				Thread.currentThread().interrupt();
			}
		}
	}

	public static String waitForChoiceInMenu(List<String> possibleChoices) {
		Scanner scanner = new Scanner(System.in);
		if (possibleChoices == null || possibleChoices.isEmpty())
			throw new IllegalArgumentException("No options to chose from");
		String choice;
		do {
			moveCursorUP(1);
			print("											║                                                                ║");
			moveCursorLEFT(34);
			choice = scanner.nextLine();
		} while (!possibleChoices.contains(choice));
		return choice;
	}

	public static String waitForChoiceInMenu(String... possibleChoices) {
		Scanner scanner = new Scanner(System.in);
		if (possibleChoices == null || possibleChoices.length == 0)
			throw new IllegalArgumentException("No options to chose from");
		String choice;
		do {
			moveCursorUP(1);
			print("											║                                                                ║");
			moveCursorLEFT(34);
			choice = scanner.nextLine();
		} while (!Utils.contains(possibleChoices, choice));
		return choice;
	}
}
