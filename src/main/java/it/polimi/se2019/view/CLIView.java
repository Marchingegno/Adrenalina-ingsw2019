package it.polimi.se2019.view;

import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.gamemap.SquareRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.GameConstants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author MarcerAndrea
 * @author Desno365
 */
public class CLIView implements ViewInterface {

	private ModelRep modelRep;
	private Scanner scanner;

	public CLIView() {
		modelRep = new ModelRep();
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
	public void displayGame() {
		displayMap();
		displayPlayers();
		displayGameBoard();
	}

	private void displayMap() {
		GameMapRep gameMapRep = modelRep.getGameMapRep();
		ArrayList<ArrayList<SquareRep>> map = gameMapRep.getMapRep();

		for (int i = 0; i < gameMapRep.getNumOfRows(); i++) {
			for (int j = 0; j < gameMapRep.getNumOfColumns(); j++) {
				System.out.print(map.get(i).get(j).getRoomID());
			}
			System.out.print("\n");
		}

		try{
			gameMapRep.getPlayersCoordinates().forEach((player, coordinates) -> System.out.println(player + ": " + coordinates));
		}catch(NullPointerException e)
		{
			System.out.println("Player not initializes");
		}
	}

	private void displayGameBoard() {
		GameBoardRep gameBoardRep = modelRep.getGameBoardRep();

		System.out.println("GAME BOARD REP");
	}

	private void displayPlayers() {
		for (PlayerRep playerRep : modelRep.getPlayersRep() ) {
			System.out.println("Nickname: " + playerRep.getPlayerName() + "\n" +
								"MORE INFO\n");
		}
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

	@Override
	public void askAction() {

	}

	@Override
	public void showTargettablePlayers() {

	}

	@Override
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {
		modelRep.setGameMapRep(gameMapRepToUpdate);
	}

	@Override
	public void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate) {
		modelRep.setGameBoardRep(gameBoardRepToUpdate);
	}

	@Override
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {
		modelRep.setPlayersRep(playerRepToUpdate);
	}

	@Override
	public void showMessage(String stringToShow) {

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