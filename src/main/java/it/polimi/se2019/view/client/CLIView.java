package it.polimi.se2019.view.client;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gameboard.KillShotRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.gamemap.SquareRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.client.Client;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.polimi.se2019.view.client.CLIPrinter.*;

/**
 * @author MarcerAndrea
 * @author Desno365
 */
public class CLIView extends RemoteView {

	private String nickname;
	private ModelRep modelRep = new ModelRep();
	private Scanner scanner = new Scanner(System.in);
	private RepPrinter repPrinter = new RepPrinter(modelRep);

	public static void print(String string) {
		System.out.print(string);
	}

	public static void printLine(String string) {
		System.out.println(string);
	}

	@Override
	public void askForConnectionAndStartIt() {
		printChooseConnection();
		ArrayList<String> possibleChoices = new ArrayList<>();
		possibleChoices.add("1");
		possibleChoices.add("2");
		if (Integer.parseInt(waitForChoiceInMenu(possibleChoices)) == 1)
			startConnectionWithRMI();
		else
			startConnectionWithSocket();
	}

	@Override
	public void failedConnectionToServer() {
		print("Failed to connect to the server. Try again later.");
		Client.terminateClient();
	}

	@Override
	public void askNickname() {
		if(Utils.DEBUG_BYPASS_CONFIGURATION){
			String randomNickname = UUID.randomUUID().toString().substring(0,3).replace("-","");
			sendMessage(new NicknameMessage(randomNickname, MessageSubtype.ANSWER));
			return;
		}
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
	public void nicknameIsOk(String nickname) {
		Utils.logInfo("Nickname set to: \"" + nickname + "\".");
		this.nickname = nickname;
	}

	@Override
	public void displayWaitingPlayers(String waitingPlayers) {
		//printWaitingRoom(waitingPlayers);
		Utils.logInfo("Players in the waiting room: " + waitingPlayers + ".");
	}

	@Override
	public void displayTimerStarted(long delayInMs) {
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setMaximumFractionDigits(1);
		printLine("The match will start in " + decimalFormat.format(delayInMs / 1000d) + " seconds...");
	}

	@Override
	public void displayTimerStopped() {
		printLine(Color.getColoredString("Timer for starting the match cancelled.", Color.CharacterColorType.RED));
	}

	@Override
	public void askMapAndSkullsToUse() {
		if(Utils.DEBUG_BYPASS_CONFIGURATION){
			GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.ANSWER);
			gameConfigMessage.setMapIndex(0);
			gameConfigMessage.setSkulls(5);
			sendMessage(gameConfigMessage);
			return;
		}
		Utils.logInfo("\n\nMatch ready to start. Select your preferred configuration.");
		int mapIndex = askMapToUse();
		int skulls = askSkullsForGame();
		ArrayList<String> players = new ArrayList<>();
		players.add(nickname);
		printWaitingRoom(players);
		GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.ANSWER);
		gameConfigMessage.setMapIndex(mapIndex);
		gameConfigMessage.setSkulls(skulls);
		sendMessage(gameConfigMessage);
	}

	@Override
	public void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType) {
		Utils.logInfo("Average of voted skulls: " + skulls + ".");
		Utils.logInfo("Most voted map: " + mapType.getDescription());
		Utils.logInfo("Game ready to start.");
		Utils.logInfo("Press Enter to start.");
		scanner.nextLine();
	}

	@Override
	public void displayPossibleActions(List<MacroAction> possibleActions) {

	}

	// TODO remove
	@Override
	public void askActionExample() {
		printLine("Asking the user the action...");
		printLine("Select a number between 0 and 2.");
		int answer = askInteger(0, 2);
		// Send a message to the server with the answer for the request. The server will process it in the VirtualView class.
		sendMessage(new IntMessage(answer, MessageType.EXAMPLE_ACTION, MessageSubtype.ANSWER));
	}

	@Override
	public void askAction() {
		printLine("Asking the user the action...");
		printLine("Choose an action!");
		printLine("Select a number between 0 and 2.");
		int answer = askInteger(0, 2);
		// Send a message to the server with the answer for the request. The server will process it in the VirtualView class.
		sendMessage(new DefaultActionMessage(answer, MessageType.ACTION, MessageSubtype.ANSWER));

	}

	@Override
	public void askGrab() {
		//TODO: Check whether the player is in a spawn square or a weapon square.
		modelRep.getGameMapRep().getPlayerCoordinates(nickname);
		printLine("You chose to grab.");
		printLine("Select a number between 0 and 2.");
		printLine("You chose 0! <:)");
		//int answer = askInteger(0, 2);
		// Send a message to the server with the answer for the request. The server will process it in the VirtualView class.
		sendMessage(new DefaultActionMessage(0, MessageType.GRAB_AMMO, MessageSubtype.ANSWER));
	}

	@Override
	public void askMove() {
		printLine("Enter the coordinates in which you want to move.");
		printLine("Enter X coordinate 0-4");
		int x = askInteger(0,4);
		printLine("Enter Y coordinate 0-4");
		int y = askInteger(0,4);
		Coordinates coordinates = new Coordinates(x,y);
		sendMessage(new MoveActionMessage(coordinates, MessageSubtype.ANSWER));
	}

	@Override
	public void askShoot() {
		printLine("LOL");
		askEnd();
	}

	@Override
	public void askReload() {
		printLine("Which weapon do you want to reload?");
		printLine("Select a number between 0 and 2.");
		int answer = askInteger(0, 2);
		// Send a message to the server with the answer for the request. The server will process it in the VirtualView class.
		sendMessage(new DefaultActionMessage(answer, MessageType.RELOAD, MessageSubtype.ANSWER));
	}

	@Override
	public void askEnd() {
		sendMessage(new Message(MessageType.END_TURN, MessageSubtype.ANSWER));
	}

	@Override
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {
		if (gameMapRepToUpdate == null)
			throw new NullPointerException();
		modelRep.setGameMapRep(gameMapRepToUpdate);
	}

	@Override
	public void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate) {
		if (gameBoardRepToUpdate == null)
			throw new NullPointerException();
		modelRep.setGameBoardRep(gameBoardRepToUpdate);
	}

	@Override
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {
		if (playerRepToUpdate == null)
			throw new NullPointerException();
		modelRep.setPlayersRep(playerRepToUpdate);
	}

	@Override
	public void askSpawn() {
		printLine("Select the Powerup card to use.");
		printLine("Select a number between 0 and 3.");
		int answer = askInteger(0, 3);
		// Send a message to the server with the answer for the request. The server will process it in the VirtualView class.
		sendMessage(new DefaultActionMessage(answer, MessageType.SPAWN, MessageSubtype.ANSWER));
	}

	private int askMapToUse() {
		printChooseMap();
		ArrayList<String> possibleChoices = new ArrayList<>();
		for (int i = 1; i <= GameConstants.MapType.values().length; i++) {
			possibleChoices.add(Integer.toString(i));
		}
		return Integer.parseInt(waitForChoiceInMenu(possibleChoices));
		//return askInteger(0, GameConstants.MapType.values().length - 1);
	}

	private int askSkullsForGame() {
		printChooseSkulls();
		ArrayList<String> possibleChoices = new ArrayList<>();
		for (int i = GameConstants.MIN_SKULLS; i <= GameConstants.MAX_SKULLS; i++)
			possibleChoices.add(Integer.toString(i));
		return Integer.parseInt(waitForChoiceInMenu(possibleChoices));
		//printLine("Select how many skulls you would like to use, min " + GameConstants.MIN_SKULLS + ", max " + GameConstants.MAX_SKULLS + ".");
		//return askInteger(GameConstants.MIN_SKULLS, GameConstants.MAX_SKULLS);
	}

	/**
	 * Displays the main game board
	 */
	public void displayGame() {
		if(modelRep.isModelRepReadyToBeDisplayed())
			repPrinter.displayGame();
		else
			Utils.logInfo("CLIView => displayGame(): Rep not ready to be displayed.");
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
		int input = -1;
		while (input < minInclusive || input > maxInclusive) {
			try {
				input = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				input = -1;
			} finally {
				if (input < minInclusive || input > maxInclusive)
					printLine("The value must be between " + minInclusive + " and " + maxInclusive + ".");
			}
		}
		return input;
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

	private String getNickname(){
		return nickname;
	}
}


/**
 * Helps printing the information.
 */
class RepPrinter {

	private ModelRep modelRep;
	private String[][] mapToPrint;
	private Logger logger = Logger.getLogger(RepPrinter.class.getName());

	RepPrinter(ModelRep modelRep) {
		this.modelRep = modelRep;
	}

	/**
	 * Displays all the game board.
	 */
	void displayGame() {
		CLIView.print("\n");

		displayPlayers();

		CLIView.print("\n");

		displayGameBoard();

		CLIView.print("\n");

		if (mapToPrint == null)
			initializeMapToPrint(modelRep.getGameMapRep().getMapRep());
		updateMapToPrint();
		displayMap();

		CLIView.print("\n");

		displayOwnPlayer(modelRep.getPlayersRep().get(0));
	}

	/**
	 * Prepares the map to be printed by updating the ammo cards and the player positions.
	 */
	private void updateMapToPrint() {
		GameMapRep gameMapRep = modelRep.getGameMapRep();
		SquareRep[][] mapRep = gameMapRep.getMapRep();
		List<PlayerRep> playersRep = modelRep.getPlayersRep();

		for (int i = 0; i < mapRep.length; i++) {
			for (int j = 0; j < mapRep[0].length; j++) {
				fillEmpty(mapRep[i][j]);
				fillCards(mapRep[i][j]);
			}
		}

		//update Players position
		for (PlayerRep playerRep : playersRep) {
			try {
				Coordinates playerCoordinates = convertCoordinates(gameMapRep.getPlayersCoordinates().get(playerRep.getPlayerName()));
				mapToPrint[playerCoordinates.getRow() - 1][playerCoordinates.getColumn() - 2 + playerRep.getPlayerID()] = Color.getColoredString("⧫", playerRep.getPlayerColor());
			} catch (NullPointerException e) {
				logger.log(Level.SEVERE, "{0} has no position", playerRep.getPlayerName());
			}
		}
	}

	/**
	 * Adds to the map to print in the correct square the ammos of the ammo card.
	 *
	 * @param squareRep
	 */
	private void fillCards(SquareRep squareRep) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		String[] cards = squareRep.getElementsToPrint();

		if (squareRep.getRoomID() != -1) {
			mapToPrint[row + 1][column - 1] = cards[0];
			mapToPrint[row + 1][column] = cards[1];
			mapToPrint[row + 1][column + 1] = cards[2];
		}
	}

	/**
	 * Resets all the cells in a square.
	 *
	 * @param squareRep
	 */
	private void fillEmpty(SquareRep squareRep) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();

		for (int i = -1; i < 2; i++) {
			for (int j = -2; j < 3; j++) {
				mapToPrint[row + i][column + j] = Color.getColoredCell(Color.BackgroundColorType.DEFAULT);
			}
		}
	}

	/**
	 * Displays the remaining skulls, the kill shot track and the double kills.
	 */
	private void displayGameBoard() {
		CLIView.printLine(getSkullString() + "\n\n" +
				getKillShotTrackString() + "\n\n" +
				getDoubleKillString() + "\n");
	}

	private String getSkullString() {
		int skulls = modelRep.getGameBoardRep().getRemainingSkulls();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Skulls:\t\t");
		for (int i = 0; i < GameConstants.MAX_SKULLS; i++) {
			stringBuilder.append(Color.getColoredString("◼", i < (GameConstants.MAX_SKULLS - skulls) ? Color.CharacterColorType.DEFAULT : Color.CharacterColorType.RED));
		}
		return stringBuilder.toString();
	}

	private String getKillShotTrackString() {
		List<KillShotRep> killShotTrackRep = modelRep.getGameBoardRep().getKillShoots();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Killshot:\t|");
		for (KillShotRep killShotRep : killShotTrackRep) {
			stringBuilder.append(Color.getColoredString(killShotRep.isOverkill() ? "◼◼" : "◼", killShotRep.getPlayerColor()));
			stringBuilder.append("|");
		}
		return stringBuilder.toString();
	}

	private String getDoubleKillString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("DoubleKill:\t|");
		for (Color.CharacterColorType doubleKiller : modelRep.getGameBoardRep().getDoubleKills()) {
			stringBuilder.append(Color.getColoredString("◼", doubleKiller));
			stringBuilder.append("|");
		}
		return stringBuilder.toString();
	}

	private void displayPlayers() {
		StringBuilder stringBuilder = new StringBuilder();
		for (PlayerRep playerRep : modelRep.getPlayersRep()) {
			stringBuilder.append(Color.getColoredString("◼ ", playerRep.getPlayerName().equals(modelRep.getGameBoardRep().getCurrentPlayer()) ? Color.CharacterColorType.BLACK : Color.CharacterColorType.DEFAULT));
			stringBuilder.append(Color.getColoredString(playerRep.getPlayerName(), playerRep.getPlayerColor()));
			for (int i = 0; i < GameConstants.MAX_NICKNAME_LENGHT - playerRep.getPlayerName().length(); i++) {
				stringBuilder.append(" ");
			}

			stringBuilder.append(getDamageBoard(playerRep.getDamageBoard()));

			stringBuilder.append("\t\t\t|");
			stringBuilder.append(getMarksBoard(playerRep.getMarks()));

			CLIView.print(stringBuilder.toString());
			stringBuilder = new StringBuilder();
		}
	}


	private String getDamageBoard(List<Color.CharacterColorType> damageBoard) {
		ArrayList<String> strings = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();
		strings.add("|");
		for (Color.CharacterColorType damage : damageBoard) {
			strings.add(Color.getColoredString("◼", damage));
		}
		for (int i = damageBoard.size(); i < 11; i++) {
			strings.add("◼");
		}
		strings.add("|");

		for (int i = 0; i < strings.size(); i++) {
			stringBuilder.append(strings.get(i));
			if (i == 2 || i == 5 || i == 9)
				stringBuilder.append("|");
		}
		return stringBuilder.toString();
	}

	private String getMarksBoard(List<Color.CharacterColorType> damageBoard) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Color.CharacterColorType damage : damageBoard) {
			stringBuilder.append(Color.getColoredString("◼", damage));
		}
		stringBuilder.append("|");
		return stringBuilder.toString();
	}

	private void displayMap() {
		for (int i = 0; i < mapToPrint.length; i++) {
			for (int j = 0; j < mapToPrint[0].length; j++) {
				CLIView.print(mapToPrint[i][j]);
			}
			CLIView.print("\n");
		}
	}

	private void displayOwnPlayer(PlayerRep playerRep) {
		CLIView.print(Color.getColoredString(playerRep.getPlayerName(), playerRep.getPlayerColor(), Color.BackgroundColorType.DEFAULT));
		CLIView.print(
				"Move 1 >>>\t\t" +
						Color.getColoredString("◼", Color.CharacterColorType.YELLOW, Color.BackgroundColorType.DEFAULT) +
						" Powerup 1\t\t" +
						Color.getColoredString("◼", Color.CharacterColorType.WHITE, Color.BackgroundColorType.DEFAULT) +
						" Weapon 1\t" +
						Color.getColoredString("◼", Color.CharacterColorType.YELLOW, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("◼", Color.CharacterColorType.RED, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("◼", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.DEFAULT) +
						"\t\t\t" +
						Color.getColoredString("◼", Color.CharacterColorType.YELLOW, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("◼", Color.CharacterColorType.YELLOW, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("◼", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.DEFAULT));
		CLIView.print(
				"Move 2 >>O\t\t" +
						Color.getColoredString("◼", Color.CharacterColorType.RED, Color.BackgroundColorType.DEFAULT) +
						" Powerup 2\t\t" +
						Color.getColoredString("◼", Color.CharacterColorType.BLACK, Color.BackgroundColorType.DEFAULT) +
						" Weapon 2\t" +
						Color.getColoredString("◼", Color.CharacterColorType.BLUE, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("◼", Color.CharacterColorType.RED, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("◼", Color.CharacterColorType.RED, Color.BackgroundColorType.DEFAULT) +
						"\t\t\t" +
						Color.getColoredString("◼", Color.CharacterColorType.RED, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("◼", Color.CharacterColorType.RED, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("◼", Color.CharacterColorType.RED, Color.BackgroundColorType.DEFAULT));
		CLIView.print(
				"Move 3 >>S\t\t" +
						Color.getColoredString("◼", Color.CharacterColorType.BLUE, Color.BackgroundColorType.DEFAULT) +
						" Powerup 3\t\t" +
						Color.getColoredString("◼", Color.CharacterColorType.BLACK, Color.BackgroundColorType.DEFAULT) +
						" Weapon 3\t" +
						Color.getColoredString("◼", Color.CharacterColorType.RED, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("◼", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("◼", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.DEFAULT) +
						"\t\t\t" +
						Color.getColoredString("◼", Color.CharacterColorType.BLUE, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("◼", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("◼", Color.CharacterColorType.DEFAULT, Color.BackgroundColorType.DEFAULT));
	}

	private String[][] initializeMapToPrint(SquareRep[][] map) {
		int numOfRows = map.length;
		int numOfColumns = map[0].length;

		mapToPrint = new String[numOfRows * 5][numOfColumns * 9];

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				fillSquare(map[i][j], mapToPrint);
			}
		}

		return mapToPrint;
	}

	private Coordinates convertCoordinates(Coordinates coordinatesToConvert) {
		return new Coordinates(2 + coordinatesToConvert.getRow() * 5, 4 + coordinatesToConvert.getColumn() * 9);
	}

	private void fillSquare(SquareRep squareRep, String[][] mapToPrint) {

		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();

		fillEmpty(squareRep);

		if (squareRep.getRoomID() != -1) {
			fillSquareCorners(squareRep);
			fillUpDoor(squareRep);
			fillRightDoor(squareRep);
			fillDownDoor(squareRep);
			fillLeftDoor(squareRep);
		} else {
			for (int i = -2; i < 3; i++) {
				for (int j = -4; j < 5; j++) {
					mapToPrint[row + i][column + j] = Color.getColoredCell(Color.BackgroundColorType.DEFAULT);
				}
			}
		}


	}

	private void fillSquareCorners(SquareRep squareRep) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		Color.CharacterColorType squareColor = squareRep.getSquareColor();

		mapToPrint[row - 2][column - 4] = Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column - 3] = Color.getColoredString("╔", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column + 4] = Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column + 3] = Color.getColoredString("╗", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column - 4] = Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column - 3] = Color.getColoredString("╚", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column + 4] = Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column + 3] = Color.getColoredString("╝", squareColor, Color.BackgroundColorType.DEFAULT);
	}

	private void fillUpDoor(SquareRep squareRep) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		Color.CharacterColorType squareColor = squareRep.getSquareColor();
		boolean isUpPossible = squareRep.getPossibleDirection()[CardinalDirection.UP.ordinal()];

		mapToPrint[row - 2][column - 2] = isUpPossible ? Color.getColoredString("╝", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column - 1] = isUpPossible ? Color.getColoredCell(Color.BackgroundColorType.DEFAULT) : Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column] = isUpPossible ? Color.getColoredCell(Color.BackgroundColorType.DEFAULT) : Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column + 1] = isUpPossible ? Color.getColoredCell(Color.BackgroundColorType.DEFAULT) : Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column + 2] = isUpPossible ? Color.getColoredString("╚", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
	}

	private void fillRightDoor(SquareRep squareRep) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		Color.CharacterColorType squareColor = squareRep.getSquareColor();
		boolean isRightPossible = squareRep.getPossibleDirection()[CardinalDirection.RIGHT.ordinal()];

		mapToPrint[row - 1][column + 3] = isRightPossible ? Color.getColoredString("╚", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - 1][column + 4] = isRightPossible ? Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row][column + 3] = isRightPossible ? Color.getColoredCell(Color.BackgroundColorType.DEFAULT) : Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row][column + 4] = isRightPossible ? Color.getColoredCell(Color.BackgroundColorType.DEFAULT) : Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 1][column + 3] = isRightPossible ? Color.getColoredString("╔", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 1][column + 4] = isRightPossible ? Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
	}

	private void fillDownDoor(SquareRep squareRep) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		Color.CharacterColorType squareColor = squareRep.getSquareColor();
		boolean isDownPossible = squareRep.getPossibleDirection()[CardinalDirection.DOWN.ordinal()];

		mapToPrint[row + 2][column - 2] = isDownPossible ? Color.getColoredString("╗", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column - 1] = isDownPossible ? Color.getColoredCell(Color.BackgroundColorType.DEFAULT) : Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column] = isDownPossible ? Color.getColoredCell(Color.BackgroundColorType.DEFAULT) : Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column + 1] = isDownPossible ? Color.getColoredCell(Color.BackgroundColorType.DEFAULT) : Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column + 2] = isDownPossible ? Color.getColoredString("╔", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
	}

	private void fillLeftDoor(SquareRep squareRep) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		Color.CharacterColorType squareColor = squareRep.getSquareColor();
		boolean isLeftPossible = squareRep.getPossibleDirection()[CardinalDirection.LEFT.ordinal()];

		mapToPrint[row - 1][column - 3] = isLeftPossible ? Color.getColoredString("╝", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - 1][column - 4] = isLeftPossible ? Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row][column - 3] = isLeftPossible ? Color.getColoredCell(Color.BackgroundColorType.DEFAULT) : Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row][column - 4] = isLeftPossible ? Color.getColoredCell(Color.BackgroundColorType.DEFAULT) : Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 1][column - 3] = isLeftPossible ? Color.getColoredString("╗", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 1][column - 4] = isLeftPossible ? Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
	}
}