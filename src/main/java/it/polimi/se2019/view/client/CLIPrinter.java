package it.polimi.se2019.view.client;

import java.util.Scanner;

import static it.polimi.se2019.view.client.CLIView.print;

public class CLIPrinter {

	private static final String TITLE = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\u001b[33;49m" +
			"								       ___       _______  .______       _______ .__   __.      ___       __       __  .__   __.      ___      \n" +
			"								      /   \\     |       \\ |   _  \\     |   ____||  \\ |  |     /   \\     |  |     |  | |  \\ |  |     /   \\     \n" +
			"								     /  ^  \\    |  .--.  ||  |_)  |    |  |__   |   \\|  |    /  ^  \\    |  |     |  | |   \\|  |    /  ^  \\    \n" +
			"								    /  /_\\  \\   |  |  |  ||      /     |   __|  |  . `  |   /  /_\\  \\   |  |     |  | |  . `  |   /  /_\\  \\   \n" +
			"								   /  _____  \\  |  '--'  ||  |\\  \\----.|  |____ |  |\\   |  /  _____  \\  |  `----.|  | |  |\\   |  /  _____  \\  \n" +
			"								  /__/     \\__\\ |_______/ | _| `._____||_______||__| \\__| /__/     \\__\\ |_______||__| |__| \\__| /__/     \\__\\ \n\u001b[39;49m";


	public static void cleanConsole(){
		print("\u001b[2J");
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
				"											║                           [1] GUI                              ║ \n" +
				"											║                           [2] CLI                              ║ \n" +
				"											║                           " + saveCursorPosition() + "                                     ║\n " +
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
				"											║                           [1] RMI                              ║ \n" +
				"											║                           [2] Socket                           ║ \n" +
				"											║                           " + saveCursorPosition() + "                                     ║\n " +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");
		loadCursorPosition();
	}

	public static String printChooseNickname() {
		cleanConsole();
		setCursorHome();
		print(TITLE +
				"											╔════════════════════════════════════════════════════════════════╗ \n" +
				"											║                                                                ║ \n" +
				"											║                             NICKNAME                           ║ \n" +
				"											║                                                                ║ \n" +
				"											║                             " + saveCursorPosition() + "                                      ║\n " +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");
		loadCursorPosition();

		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		return line;
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
