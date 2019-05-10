package it.polimi.se2019.view.client;

import it.polimi.se2019.utils.Color;

import java.util.Scanner;

public class CLIPrinter {

	public static void cleanConsole(){
		System.out.println("\033[2J ");
	}

	public CLIPrinter(){}

	public static void printLoginScrean() {

		for (int i = 0; i < 16; i++){
			for (int j = 0; j < 16; j++){
				Integer code = (i * 16 + j);
				String coString = code.toString();
				System.out.print("\u001b[48;5;" + coString + "m " + String.format("%-" + 4 + "s", coString));
			}
			System.out.print("\u001b[0m");
		}

		System.out.print("\u001b[2J");
		System.out.print("\033[H");


		System.out.println("" +
				"\n" + Color.getColoredString("" +
				"		     ___       _______  .______       _______ .__   __.      ___       __       __  .__   __.      ___      \n" +
				"		    /   \\     |       \\ |   _  \\     |   ____||  \\ |  |     /   \\     |  |     |  | |  \\ |  |     /   \\     \n" +
				"		   /  ^  \\    |  .--.  ||  |_)  |    |  |__   |   \\|  |    /  ^  \\    |  |     |  | |   \\|  |    /  ^  \\    \n" +
				"		  /  /_\\  \\   |  |  |  ||      /     |   __|  |  . `  |   /  /_\\  \\   |  |     |  | |  . `  |   /  /_\\  \\   \n" +
				"		 /  _____  \\  |  '--'  ||  |\\  \\----.|  |____ |  |\\   |  /  _____  \\  |  `----.|  | |  |\\   |  /  _____  \\  \n" +
				"		/__/     \\__\\ |_______/ | _| `._____||_______||__| \\__| /__/     \\__\\ |_______||__| |__| \\__| /__/     \\__\\ \n", Color.CharacterColorType.YELLOW) +
				"\n" +
				"                                  " + Color.getColoredString("                                                       ", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.CYAN) + "\n" +
				"                                  " + Color.getColoredString("                                                       ", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.CYAN) + "\n" +
				"                                  " + Color.getColoredString("       Username:" + "\u001b[s" + "                                       ", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.CYAN) + "\n" +
				"                                  " + Color.getColoredString("                                                       ", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.CYAN) + "\n" +
				"                                  " + Color.getColoredString("       RMI/Socket:                                     ", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.CYAN) + "\n" +
				"                                  " + Color.getColoredString("                                                       ", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.CYAN) + "\n" +
				"                                  " + Color.getColoredString("                                                       ", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.CYAN) + "\n" +
				"                                  " + Color.getColoredString("                                                       ", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.CYAN) + "\n"
				);

		System.out.println("\u001b[u");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();

	}
}
