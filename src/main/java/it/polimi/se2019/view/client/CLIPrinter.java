package it.polimi.se2019.view.client;

import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

import java.util.List;
import java.util.Scanner;

import static it.polimi.se2019.view.client.CLIView.print;

public class CLIPrinter {

	private static final String ESC = (char) 27 + "[";
	private static final String CLEAN = ESC + "J";
	private static final String HOME = ESC + "H";
	private static final String SAVE = ESC + "s";
	private static final String LOAD = ESC + "u";

	private static final Scanner scanner = new Scanner(System.in);
	private static final String TITLE = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\u001b[33;49m" +
			"								       ___       _______  .______       _______ .__   __.      ___       __       __  .__   __. _______ \n" +
			"								      /   \\     |       \\ |   _  \\     |   ____||  \\ |  |     /   \\     |  |     |  | |  \\ |  ||   ____| \n" +
			"								     /  ^  \\    |  .--.  ||  |_)  |    |  |__   |   \\|  |    /  ^  \\    |  |     |  | |   \\|  ||  |__    \n" +
			"								    /  /_\\  \\   |  |  |  ||      /     |   __|  |  . `  |   /  /_\\  \\   |  |     |  | |  . `  ||   __|   \n" +
			"								   /  _____  \\  |  '--'  ||  |\\  \\----.|  |____ |  |\\   |  /  _____  \\  |  `----.|  | |  |\\   ||  |____  \n" +
			"								  /__/     \\__\\ |_______/ | _| `._____||_______||__| \\__| /__/     \\__\\ |_______||__| |__| \\__||_______| \n\n\n\u001b[39;49m";


	public static void cleanConsole(){
		print(CLEAN);
		System.out.flush();
	}

	public static void setCursorHome() {
		print(HOME);
	}

	public static String saveCursorPosition() {
		return SAVE;
	}

	public static void loadCursorPosition() {
		print(LOAD);
	}

	public static void moveCursorTo(int row, int column) {
		print(ESC + row + ";" + column + "H");
	}

	public static void moveCursorUP(int numOfLines) {
		print(ESC + numOfLines + "A");
	}

	public static String moveCursorDOWN(int numOfLines) {
		return "\u001b[" + numOfLines + "B";
	}

	public static void moveCursorRIGHT(int numOfLines) {
		print(ESC + numOfLines + "C");
	}

	public static void moveCursorLEFT(int numOfLines) {
		print(ESC + numOfLines + "D");
	}

	public CLIPrinter(){}

	public static void printChooseView() {
//		setCursorHome();
//		cleanConsole();
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

	public static void printChooseConnection() {
//		setCursorHome();
//		cleanConsole();
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

	public static void printChooseNickname() {
//		setCursorHome();
//		cleanConsole();
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

	public static void printChooseMap() {
//		setCursorHome();
//		cleanConsole();
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

	public static void printChooseSkulls() {
//		setCursorHome();
//		cleanConsole();
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

	public static void printWaitingRoom(List<String> waitingPlayers) {
//		setCursorHome();
//		cleanConsole();
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗\n" +
				"											║                                                                ║\n" +
				"											║              Waiting for other clients to answer...            ║\n" +
				"											║                                                                ║\n");
		for (int i = 0; i < waitingPlayers.size(); i++) {
			print("											║                   " + Utils.fillWithSpaces("[" + i + "] " + waitingPlayers.get(i), 45) + "║ \n");
		}
		for (int i = waitingPlayers.size(); i <= GameConstants.MAX_PLAYERS; i++) {
			print("											║                                                                ║\n");
		}
		print("											║                                                                ║\n" +
				"											╚════════════════════════════════════════════════════════════════╝\n");
	}

	public static void printWaitingMatchStart(long milliSeconds) {
//		setCursorHome();
//		cleanConsole();
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗\n" +
				"											║                                                                ║\n" +
				"											║                     The match will start in                    ║\n" +
				"											║                                                                ║\n" +
				"											║                                                                ║\n " +
				"											║                                                                ║\n" +
				"											║                                                                ║\n" +
				"											╚════════════════════════════════════════════════════════════════╝\n");


		for (long i = milliSeconds / 1000; i >= 0; i--) {
			moveCursorTo(29, 0);
			print("											║                                " + Utils.fillWithSpaces(Long.toString(i), 32) + "║\n");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Utils.logInfo("Error in match countdown");
				Thread.currentThread().interrupt();
			}
		}
	}

	public static String waitForChoiceInMenu(List<String> possibleChoices) {
		scanner.reset();
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
		scanner.reset();
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
