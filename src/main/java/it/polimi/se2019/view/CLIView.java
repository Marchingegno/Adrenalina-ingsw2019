package it.polimi.se2019.view;

import it.polimi.se2019.utils.GameConstants;

import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * @author Desno365
 */
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
	public void displayWaitingPlayers(String waitingPlayers) {
		System.out.println("Players in the waiting room: " + waitingPlayers + ".");
	}

	@Override
	public void displayTimerStarted(long delayInMs) {
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setMaximumFractionDigits(2);
		System.out.println("The match will start in " + decimalFormat.format(delayInMs / 1000d) + " seconds.");
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

	/**
	 * Ask the user an integer that must be between minInclusive and maxInclusive.
	 * Repeatedly ask the integer if the input is not in the limits.
	 * @param minInclusive the minimum limit.
	 * @param maxInclusive the maximum limit.
	 * @return the integer chosen by the user.
	 */
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