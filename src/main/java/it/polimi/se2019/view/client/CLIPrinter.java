package it.polimi.se2019.view.client;

import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.se2019.view.client.CLIView.print;

public class CLIPrinter {

	private static final Scanner scanner = new Scanner(System.in);
	private static final String TITLE = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\u001b[33;49m" +
			"								       ___       _______  .______       _______ .__   __.      ___       __       __  .__   __. _______ \n" +
			"								      /   \\     |       \\ |   _  \\     |   ____||  \\ |  |     /   \\     |  |     |  | |  \\ |  ||   ____| \n" +
			"								     /  ^  \\    |  .--.  ||  |_)  |    |  |__   |   \\|  |    /  ^  \\    |  |     |  | |   \\|  ||  |__    \n" +
			"								    /  /_\\  \\   |  |  |  ||      /     |   __|  |  . `  |   /  /_\\  \\   |  |     |  | |  . `  ||   __|   \n" +
			"								   /  _____  \\  |  '--'  ||  |\\  \\----.|  |____ |  |\\   |  /  _____  \\  |  `----.|  | |  |\\   ||  |____  \n" +
			"								  /__/     \\__\\ |_______/ | _| `._____||_______||__| \\__| /__/     \\__\\ |_______||__| |__| \\__||_______| \n\u001b[39;49m";


	public static void cleanConsole(){
		print("\u001b[2J");
		System.out.flush();
	}

	public static void setCursorHome() {
		print("\u001b[H");
	}

	public static String saveCursorPosition() {
		return "\u001b[s";
	}

	public static void loadCursorPosition() {
		print("\u001b[u");
	}

	public static String moveCursorUP(int numOfLines) {
		return "\u001b[" + numOfLines + "A";
	}

	public static String moveCursorDOWN(int numOfLines) {
		return "\u001b[" + numOfLines + "B";
	}

	public static String moveCursorRIGHT(int numOfLines) {
		return "\u001b[" + numOfLines + "C";
	}

	public static String moveCursorLEFT(int numOfLines) {
		return "\u001b[" + numOfLines + "D";
	}

	public CLIPrinter(){}

	public static void printChooseView() {
		cleanConsole();
		setCursorHome();
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗ \n" +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											║                           [1] GUI                              ║ \n" +
				"											║                           [2] CLI                              ║ \n" +
				"											║                              " + saveCursorPosition() + "                                  ║\n " +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");
		loadCursorPosition();
	}

	public static void printChooseConnection() {
		cleanConsole();
		setCursorHome();
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗ \n" +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											║                           [1] RMI                              ║ \n" +
				"											║                           [2] Socket                           ║ \n" +
				"											║                              " + saveCursorPosition() + "                                  ║\n " +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");
		loadCursorPosition();
	}

	public static void printChooseNickname() {

		setCursorHome();
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗ \n" +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											║                           NICKNAME                             ║ \n" +
				"											║                                                                ║ \n" +
				"											║                          " + saveCursorPosition() + "                                      ║\n " +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");
		loadCursorPosition();
	}

	public static void printChooseMap() {
		cleanConsole();
		setCursorHome();
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗ \n" +
				"											║                                                                ║ \n");

		GameConstants.MapType[] maps = GameConstants.MapType.values();
		for (int i = 0; i < maps.length; i++) {
			print("											║                   " + Utils.fillWithSpaces("[" + (i + 1) + "] " + maps[i].getMapName(), 45) + "║ \n");
		}
		print("											║                              " + saveCursorPosition() + "                                  ║\n " +
				"											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");
		loadCursorPosition();
	}

	public static void printChooseSkulls() {
		cleanConsole();
		setCursorHome();
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗ \n" +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											║                        SKULLS [" + GameConstants.MIN_SKULLS + "-" + GameConstants.MAX_SKULLS + "]                            ║ \n" +
				"											║                                                                ║ \n" +
				"											║                              " + saveCursorPosition() + "                                  ║\n " +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");
		loadCursorPosition();
	}

	public static void printWaitingRoom(List<String> waitingPlayers) {
		cleanConsole();
		setCursorHome();
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗ \n" +
				"											║                                                                ║ \n" +
				"											║              Waiting for other clients to answer...            ║ \n" +
				"											║                                                                ║ \n");
		for (int i = 0; i < waitingPlayers.size(); i++) {
			print("											║                   " + Utils.fillWithSpaces("[" + i + "] " + waitingPlayers.get(i), 45) + "║ \n");
		}
		for (int i = waitingPlayers.size(); i <= GameConstants.MAX_PLAYERS; i++) {
			print("											║                                                                ║ \n");
		}
		print("											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");
	}

	public static void printWaitingMatchStart(long milliSeconds) {
		cleanConsole();
		setCursorHome();
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗ \n" +
				"											║                                                                ║ \n" +
				"											║                      The match will start in                   ║ \n" +
				"											║                                                                ║ \n" +
				"											║                              " + saveCursorPosition() + "                                  ║\n " +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");

		for (long i = milliSeconds / 1000; i >= 0; i--) {
			loadCursorPosition();
			print(moveCursorLEFT(1000) + "											║                              " + Utils.fillWithSpaces(Long.toString(i), 34) + "║\n");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Utils.logInfo("Error in match countdown");
				Thread.currentThread().interrupt();
			}
		}
	}

	public static String waitForChoiceInMenu(ArrayList<String> possibleChoices) {
		scanner.reset();
		if (possibleChoices == null || possibleChoices.isEmpty())
			throw new IllegalArgumentException("No options to chose from");
		String choice = scanner.nextLine();
		while (!possibleChoices.contains(choice)) {
			print(moveCursorUP(1) + moveCursorLEFT(10) + "											║                             " + saveCursorPosition() + "                                   ║");
			loadCursorPosition();
			choice = scanner.nextLine();
		}
		cleanConsole();
		setCursorHome();
		return choice;
	}

	public static String waitForChoiceInMenu(String... possibleChoices) {
		scanner.reset();
		if (possibleChoices == null || possibleChoices.length == 0)
			throw new IllegalArgumentException("No options to chose from");
		String choice = scanner.nextLine();
		while (!Utils.contains(possibleChoices, choice)) {
			print(moveCursorUP(1) + moveCursorLEFT(10) + "											║                             " + saveCursorPosition() + "                                   ║");
			loadCursorPosition();
			choice = scanner.nextLine();
		}
		cleanConsole();
		setCursorHome();
		return choice;
	}

	public void printColor() {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				Integer code = (i * 16 + j);
				String coString = code.toString();
				System.out.print("\u001b[48;5;" + coString + "m " + String.format("%-" + 4 + "s", coString));
			}
			System.out.print("\u001b[0m");
		}
	}
}
