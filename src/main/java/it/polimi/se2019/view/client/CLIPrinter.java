package it.polimi.se2019.view.client;

import java.util.Scanner;

public class CLIPrinter {

	private static final String title = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\u001b[33;49m" +
			"								       ___       _______  .______       _______ .__   __.      ___       __       __  .__   __.      ___      \n" +
			"								      /   \\     |       \\ |   _  \\     |   ____||  \\ |  |     /   \\     |  |     |  | |  \\ |  |     /   \\     \n" +
			"								     /  ^  \\    |  .--.  ||  |_)  |    |  |__   |   \\|  |    /  ^  \\    |  |     |  | |   \\|  |    /  ^  \\    \n" +
			"								    /  /_\\  \\   |  |  |  ||      /     |   __|  |  . `  |   /  /_\\  \\   |  |     |  | |  . `  |   /  /_\\  \\   \n" +
			"								   /  _____  \\  |  '--'  ||  |\\  \\----.|  |____ |  |\\   |  /  _____  \\  |  `----.|  | |  |\\   |  /  _____  \\  \n" +
			"								  /__/     \\__\\ |_______/ | _| `._____||_______||__| \\__| /__/     \\__\\ |_______||__| |__| \\__| /__/     \\__\\ \n\u001b[39;49m";


	public static void cleanConsole(){
		System.out.print("\u001b[2J ");
	}

	public static void setCursorHome() {
		System.out.print("\u001b[H ");
	}

	public static String saveCursorPosition() {
		return "\u001b[s";
	}

	public static void loadCursorPosition() {
		System.out.print("\u001b[u");
	}

	public CLIPrinter(){}

	public static boolean printChooseView() {

		cleanConsole();
		setCursorHome();
		System.out.print(title +
				"											╔════════════════════════════════════════════════════════════════╗ \n" +
				"											║                                                                ║ \n" +
				"											║                           [1] GUI                              ║ \n" +
				"											║                           [2] CLI                              ║ \n" +
				"											║                           " + saveCursorPosition() + "                                     ║\n " +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");

		loadCursorPosition();
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		while ((!line.equals("1") && !line.equals("2"))) {
			System.out.print("\u001b[1A								║                           " + saveCursorPosition() + "                                     ║" + "\u001b[u");
			line = scanner.nextLine();
		}
		return line.equals("1");
	}

	public static int printChooseConnection() {

		cleanConsole();
		setCursorHome();
		System.out.print(title +
				"											╔════════════════════════════════════════════════════════════════╗ \n" +
				"											║                                                                ║ \n" +
				"											║                           [1] RMI                              ║ \n" +
				"											║                           [2] Socket                           ║ \n" +
				"											║                           " + saveCursorPosition() + "                                     ║\n " +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");

		System.out.print("\u001b[u");
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		while ((!line.equals("1") && !line.equals("2"))) {
			System.out.print("\u001b[1A								║                           " + saveCursorPosition() + "                                     ║\u001b[u");
			line = scanner.nextLine();
		}
		return Integer.parseInt(line);
	}

	public static String printChooseNickname() {

		cleanConsole();
		setCursorHome();
		System.out.print(title +
				"											╔════════════════════════════════════════════════════════════════╗ \n" +
				"											║                                                                ║ \n" +
				"											║                             NICKNAME                           ║ \n" +
				"											║                                                                ║ \n" +
				"											║                             " + saveCursorPosition() + "                                      ║\n " +
				"											║                                                                ║ \n" +
				"											║                                                                ║ \n" +
				"											╚════════════════════════════════════════════════════════════════╝ \n");

		System.out.print("\u001b[u");
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
