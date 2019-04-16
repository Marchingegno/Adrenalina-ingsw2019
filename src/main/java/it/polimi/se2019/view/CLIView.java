package it.polimi.se2019.view;

import it.polimi.se2019.utils.GameConstants;

import java.util.Scanner;

public class CLIView implements RemoteViewInterface {

	private Scanner scanner;

	public CLIView() {
		scanner = new Scanner(System.in);
	}

	@Override
	public String askNickname() {
		System.out.println("Enter your nickname.");
		return scanner.nextLine();
	}

	@Override
	public void displayText(String text) {
		System.out.println(text);
	}

	@Override
	public int askMapToUse() {
		System.out.println("Select the map you would like to use, available maps:");
		for(GameConstants.MapType map : GameConstants.MapType.values()) {
			System.out.println(map.ordinal() + ": " + map.getDescription());
		}
		return askForAnInteger(0, GameConstants.MapType.values().length - 1);
	}

	@Override
	public int askSkullsForGame() {
		System.out.println("Select how many skulls you would like to use, min " + GameConstants.MIN_SKULLS + ", max " + GameConstants.MAX_SKULLS + ".");
		return askForAnInteger(GameConstants.MIN_SKULLS, GameConstants.MAX_SKULLS);
	}

	private int askForAnInteger(int minInclusive, int maxInclusive) {
		int input = -1;
		while (input < minInclusive || input > maxInclusive) {
			try {
				input = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				input = -1;
			} finally {
				if(input < minInclusive || input > maxInclusive)
					System.out.println("The value must be between " + minInclusive + " and " + maxInclusive + ".");
			}
		}
		return input;
	}
}