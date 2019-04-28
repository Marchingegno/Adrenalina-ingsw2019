package it.polimi.se2019.view.client;

import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.gamemap.MapSquareRep;
import it.polimi.se2019.model.player.KillShotRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.client.Client;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author MarcerAndrea
 * @author Desno365
 */
public class CLIView extends RemoteView {

	private ModelRep modelRep;
	private Scanner scanner;
	private RepPrinter repPrinter;
	private Logger logger = Logger.getLogger(CLIView.class.getName());

	public CLIView() {
		this.modelRep = new ModelRep();
		this.scanner = new Scanner(System.in);
		this.repPrinter = new RepPrinter(this.modelRep);
	}

	@Override
	public void failedConnectionToServer() {
		System.out.println("Failed to connect to the server. Try again later.");
		Client.terminateClient();
	}

	@Override
	public void lostConnectionToServer() {
		System.out.println("Lost connection with the server. Please restart the game.");
		Client.terminateClient();
	}

	@Override
	public void askNickname() {
		System.out.println("Enter your nickname.");
		sendMessage(new NicknameMessage(scanner.nextLine(), MessageSubtype.ANSWER));
	}

	@Override
	public void askNicknameError() {
		System.out.println("The nickname already exists or is not valid, please use a different one.");
		askNickname();
	}

	@Override
	public void nicknameIsOk(String nickname) {
		System.out.println("Nickname set to: \"" + nickname + "\".");
	}

	@Override
	public void displayWaitingPlayers(String waitingPlayers) {
		System.out.println("Players in the waiting room: " + waitingPlayers + ".");
	}

	@Override
	public void displayTimerStarted(long delayInMs) {
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setMaximumFractionDigits(1);
		System.out.println("The match will start in " + decimalFormat.format(delayInMs / 1000d) + " seconds...\n\n");
	}

	@Override
	public void displayTimerStopped() {
		System.out.println(Utils.getColoredString("Timer for starting the match cancelled.", Utils.CharacterColorType.RED));
	}

	@Override
	public void askMapAndSkullsToUse() {
		System.out.println("\n\nMatch ready to start. Select your preferred configuration.");
		int mapIndex = askMapToUse();
		int skulls = askSkullsForGame();
		System.out.println("Waiting for other clients to answer...\n\n");
		GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.ANSWER);
		gameConfigMessage.setMapIndex(mapIndex);
		gameConfigMessage.setSkulls(skulls);
		sendMessage(gameConfigMessage);
	}

	@Override
	public void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType) {
		System.out.println("Average of voted skulls: " + skulls + ".");
		System.out.println("Most voted map: " + mapType.getDescription());
		System.out.println("Match started!");
	}

	// TODO remove
	@Override
	public void askActionExample() {
		System.out.println("Asking the user the action...");
		System.out.println("Select a number between 0 and 2.");
		int answer = askForAnInteger(0, 2);
		// Send a message to the server with the answer for the request. The server will process it in the VirtualView class.
		sendMessage(new IntMessage(answer, MessageType.EXAMPLE_ACTION, MessageSubtype.ANSWER));
	}

	@Override
	public void displayPossibleActions(List<MacroAction> possibleActions) {

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

	private int askMapToUse() {
		System.out.println("Select the map you would like to use, available maps:");
		for (GameConstants.MapType map : GameConstants.MapType.values()) {
			System.out.println(map.ordinal() + ": " + map.getDescription());
		}
		return askForAnInteger(0, GameConstants.MapType.values().length - 1);
	}

	private int askSkullsForGame() {
		System.out.println("Select how many skulls you would like to use, min " + GameConstants.MIN_SKULLS + ", max " + GameConstants.MAX_SKULLS + ".");
		return askForAnInteger(GameConstants.MIN_SKULLS, GameConstants.MAX_SKULLS);
	}

	/**
	 * Displays the main game board
	 */
	public void displayGame() {
		repPrinter.displayGame();
	}

	/**
	 * Ask the user an integer that must be between minInclusive and maxInclusive.
	 * Repeatedly ask the integer if the input is not in the limits.
	 *
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
				if (input < minInclusive || input > maxInclusive)
					System.out.println("The value must be between " + minInclusive + " and " + maxInclusive + ".");
			}
		}
		return input;
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
		System.out.println("\n");

		displayPlayers();

		System.out.println("\n");

		displayGameBoard();

		System.out.println("\n");

		if (mapToPrint == null)
			initializeMapToPrint(modelRep.getGameMapRep().getMapRep());
		updateMapToPrint();
		displayMap();

		System.out.println("\n");

		displayOwnPlayer(modelRep.getPlayersRep().get(0));
	}

	/**
	 * Prepares the map to be printed by updating the ammo cards and the player positions.
	 */
	private void updateMapToPrint() {
		GameMapRep gameMapRep = modelRep.getGameMapRep();
		MapSquareRep[][] mapRep = gameMapRep.getMapRep();
		ArrayList<PlayerRep> playersRep = modelRep.getPlayersRep();

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
				mapToPrint[playerCoordinates.getRow() - 1][playerCoordinates.getColumn() - 2 + playerRep.getPlayerID()] = Utils.getColoredString("⧫", playerRep.getPlayerColor());
			} catch (NullPointerException e) {
				logger.log(Level.SEVERE, "{0} has no position", playerRep.getPlayerName());
			}
		}
	}

	/**
	 * Adds to the map to print in the correct square the ammos of the ammo card.
	 *
	 * @param mapSquareRep
	 */
	private void fillCards(MapSquareRep mapSquareRep) {
		int row = convertCoordinates(mapSquareRep.getCoordinates()).getRow();
		int column = convertCoordinates(mapSquareRep.getCoordinates()).getColumn();

		if (mapSquareRep.getRoomID() != -1) {
			mapToPrint[row + 1][column - 1] = Utils.getColoredCell(Utils.BackgroundColorType.YELLOW);
			mapToPrint[row + 1][column] = Utils.getColoredCell(Utils.BackgroundColorType.RED);
			mapToPrint[row + 1][column + 1] = Utils.getColoredCell(Utils.BackgroundColorType.WHITE);
		}
	}

	/**
	 * Resets all the cells in a square.
	 *
	 * @param mapSquareRep
	 */
	private void fillEmpty(MapSquareRep mapSquareRep) {
		int row = convertCoordinates(mapSquareRep.getCoordinates()).getRow();
		int column = convertCoordinates(mapSquareRep.getCoordinates()).getColumn();

		for (int i = -1; i < 2; i++) {
			for (int j = -2; j < 3; j++) {
				mapToPrint[row + i][column + j] = Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT);
			}
		}
	}

	/**
	 * Displays the remaining skulls, the kill shot track and the double kills.
	 */
	private void displayGameBoard() {
		System.out.println(getSkullString() + "\n\n" +
				getKillShotTrackString() + "\n\n" +
				getDoubleKillString() + "\n");
	}

	private String getSkullString() {
		int skulls = modelRep.getGameBoardRep().getRemainingSkulls();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Skulls:\t\t");
		for (int i = 0; i < GameConstants.MAX_SKULLS; i++) {
			stringBuilder.append(Utils.getColoredString("◼", i < (GameConstants.MAX_SKULLS - skulls) ? Utils.CharacterColorType.DEFAULT : Utils.CharacterColorType.RED));
		}
		return stringBuilder.toString();
	}

	private String getKillShotTrackString() {
		ArrayList<KillShotRep> killShotTrackRep = modelRep.getGameBoardRep().getKillShoots();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Killshot:\t|");
		for (KillShotRep killShotRep : killShotTrackRep) {
			stringBuilder.append(Utils.getColoredString(killShotRep.isOverkill() ? "◼◼" : "◼", killShotRep.getPlayerColor()));
			stringBuilder.append("|");
		}
		return stringBuilder.toString();
	}

	private String getDoubleKillString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("DoubleKill:\t|");
		for (Utils.CharacterColorType doubleKiller : modelRep.getGameBoardRep().getDoubleKills()) {
			stringBuilder.append(Utils.getColoredString("◼", doubleKiller));
			stringBuilder.append("|");
		}
		return stringBuilder.toString();
	}

	private void displayPlayers() {
		StringBuilder stringBuilder = new StringBuilder();
		for (PlayerRep playerRep : modelRep.getPlayersRep()) {
			stringBuilder.append(Utils.getColoredString("◼ ", playerRep.getPlayerName().equals(modelRep.getGameBoardRep().getCurrentPlayer()) ? Utils.CharacterColorType.BLACK : Utils.CharacterColorType.DEFAULT));
			stringBuilder.append(Utils.getColoredString(playerRep.getPlayerName(), playerRep.getPlayerColor()));
			for (int i = 0; i < GameConstants.MAX_NICKNAME_LENGHT - playerRep.getPlayerName().length(); i++) {
				stringBuilder.append(" ");
			}

			stringBuilder.append(getDamageBoard(playerRep.getDamageBoard()));

			stringBuilder.append("\t\t\t|");
			stringBuilder.append(getMarksBoard(playerRep.getMarks()));

			System.out.println(stringBuilder.toString());
			stringBuilder = new StringBuilder();
		}
	}

	private String getDamageBoard(List<Utils.CharacterColorType> damageBoard) {
		ArrayList<String> strings = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();
		strings.add("|");
		for (Utils.CharacterColorType damage : damageBoard) {
			strings.add(Utils.getColoredString("◼", damage));
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

	private String getMarksBoard(List<Utils.CharacterColorType> damageBoard) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Utils.CharacterColorType damage : damageBoard) {
			stringBuilder.append(Utils.getColoredString("◼", damage));
		}
		stringBuilder.append("|");
		return stringBuilder.toString();
	}

	private void displayMap() {
		for (int i = 0; i < mapToPrint.length; i++) {
			for (int j = 0; j < mapToPrint[0].length; j++) {
				System.out.print(mapToPrint[i][j]);
			}
			System.out.print("\n");
		}
	}

	private void displayOwnPlayer(PlayerRep playerRep) {
		System.out.println(Utils.getColoredString(playerRep.getPlayerName(), playerRep.getPlayerColor(), Utils.BackgroundColorType.DEFAULT));
		System.out.println(
				"Move 1 >>>\t\t" +
						Utils.getColoredString("◼", Utils.CharacterColorType.YELLOW, Utils.BackgroundColorType.DEFAULT) +
						" Powerup 1\t\t" +
						Utils.getColoredString("◼", Utils.CharacterColorType.WHITE, Utils.BackgroundColorType.DEFAULT) +
						" Weapon 1\t" +
						Utils.getColoredString("◼", Utils.CharacterColorType.YELLOW, Utils.BackgroundColorType.DEFAULT) +
						Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
						Utils.getColoredString("◼", Utils.CharacterColorType.DEFAULT, Utils.BackgroundColorType.DEFAULT) +
						"\t\t\t" +
						Utils.getColoredString("◼", Utils.CharacterColorType.YELLOW, Utils.BackgroundColorType.DEFAULT) +
						Utils.getColoredString("◼", Utils.CharacterColorType.YELLOW, Utils.BackgroundColorType.DEFAULT) +
						Utils.getColoredString("◼", Utils.CharacterColorType.DEFAULT, Utils.BackgroundColorType.DEFAULT));
		System.out.println(
				"Move 2 >>O\t\t" +
						Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
						" Powerup 2\t\t" +
						Utils.getColoredString("◼", Utils.CharacterColorType.BLACK, Utils.BackgroundColorType.DEFAULT) +
						" Weapon 2\t" +
						Utils.getColoredString("◼", Utils.CharacterColorType.BLUE, Utils.BackgroundColorType.DEFAULT) +
						Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
						Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
						"\t\t\t" +
						Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
						Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
						Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT));
		System.out.println(
				"Move 3 >>S\t\t" +
						Utils.getColoredString("◼", Utils.CharacterColorType.BLUE, Utils.BackgroundColorType.DEFAULT) +
						" Powerup 3\t\t" +
						Utils.getColoredString("◼", Utils.CharacterColorType.BLACK, Utils.BackgroundColorType.DEFAULT) +
						" Weapon 3\t" +
						Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
						Utils.getColoredString("◼", Utils.CharacterColorType.DEFAULT, Utils.BackgroundColorType.DEFAULT) +
						Utils.getColoredString("◼", Utils.CharacterColorType.DEFAULT, Utils.BackgroundColorType.DEFAULT) +
						"\t\t\t" +
						Utils.getColoredString("◼", Utils.CharacterColorType.BLUE, Utils.BackgroundColorType.DEFAULT) +
						Utils.getColoredString("◼", Utils.CharacterColorType.DEFAULT, Utils.BackgroundColorType.DEFAULT) +
						Utils.getColoredString("◼", Utils.CharacterColorType.DEFAULT, Utils.BackgroundColorType.DEFAULT));
	}

	private String[][] initializeMapToPrint(MapSquareRep[][] map) {
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

	private void fillSquare(MapSquareRep mapSquareRep, String[][] mapToPrint) {

		int row = convertCoordinates(mapSquareRep.getCoordinates()).getRow();
		int column = convertCoordinates(mapSquareRep.getCoordinates()).getColumn();

		fillEmpty(mapSquareRep);

		if (mapSquareRep.getRoomID() != -1) {
			fillSquareCorners(mapSquareRep);
			fillUpDoor(mapSquareRep);
			fillRightDoor(mapSquareRep);
			fillDownDoor(mapSquareRep);
			fillLeftDoor(mapSquareRep);
		} else {
			for (int i = -2; i < 3; i++) {
				for (int j = -4; j < 5; j++) {
					mapToPrint[row + i][column + j] = Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT);
				}
			}
		}


	}

	private void fillSquareCorners(MapSquareRep mapSquareRep) {
		int row = convertCoordinates(mapSquareRep.getCoordinates()).getRow();
		int column = convertCoordinates(mapSquareRep.getCoordinates()).getColumn();
		int roomID = mapSquareRep.getRoomID();

		mapToPrint[row - 2][column - 4] = Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column - 3] = Utils.getColoredString("╔", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column + 4] = Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column + 3] = Utils.getColoredString("╗", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column - 4] = Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column - 3] = Utils.getColoredString("╚", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column + 4] = Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column + 3] = Utils.getColoredString("╝", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
	}

	private void fillUpDoor(MapSquareRep mapSquareRep) {
		int row = convertCoordinates(mapSquareRep.getCoordinates()).getRow();
		int column = convertCoordinates(mapSquareRep.getCoordinates()).getColumn();
		int roomID = mapSquareRep.getRoomID();
		boolean isUpPossible = mapSquareRep.getPossibleDirection()[CardinalDirection.UP.ordinal()];

		mapToPrint[row - 2][column - 2] = isUpPossible ? Utils.getColoredString("╝", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column - 1] = isUpPossible ? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column] = isUpPossible ? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column + 1] = isUpPossible ? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row - 2][column + 2] = isUpPossible ? Utils.getColoredString("╚", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
	}

	private void fillRightDoor(MapSquareRep mapSquareRep) {
		int row = convertCoordinates(mapSquareRep.getCoordinates()).getRow();
		int column = convertCoordinates(mapSquareRep.getCoordinates()).getColumn();
		int roomID = mapSquareRep.getRoomID();
		boolean isRightPossible = mapSquareRep.getPossibleDirection()[CardinalDirection.RIGHT.ordinal()];

		mapToPrint[row - 1][column + 3] = isRightPossible ? Utils.getColoredString("╚", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("║", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row - 1][column + 4] = isRightPossible ? Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row][column + 3] = isRightPossible ? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("║", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row][column + 4] = isRightPossible ? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row + 1][column + 3] = isRightPossible ? Utils.getColoredString("╔", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("║", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row + 1][column + 4] = isRightPossible ? Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
	}

	private void fillDownDoor(MapSquareRep mapSquareRep) {
		int row = convertCoordinates(mapSquareRep.getCoordinates()).getRow();
		int column = convertCoordinates(mapSquareRep.getCoordinates()).getColumn();
		int roomID = mapSquareRep.getRoomID();
		boolean isDownPossible = mapSquareRep.getPossibleDirection()[CardinalDirection.DOWN.ordinal()];

		mapToPrint[row + 2][column - 2] = isDownPossible ? Utils.getColoredString("╗", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column - 1] = isDownPossible ? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column] = isDownPossible ? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column + 1] = isDownPossible ? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row + 2][column + 2] = isDownPossible ? Utils.getColoredString("╔", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
	}

	private void fillLeftDoor(MapSquareRep mapSquareRep) {
		int row = convertCoordinates(mapSquareRep.getCoordinates()).getRow();
		int column = convertCoordinates(mapSquareRep.getCoordinates()).getColumn();
		int roomID = mapSquareRep.getRoomID();
		boolean isLeftPossible = mapSquareRep.getPossibleDirection()[CardinalDirection.LEFT.ordinal()];

		mapToPrint[row - 1][column - 3] = isLeftPossible ? Utils.getColoredString("╝", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("║", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row - 1][column - 4] = isLeftPossible ? Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row][column - 3] = isLeftPossible ? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("║", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row][column - 4] = isLeftPossible ? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row + 1][column - 3] = isLeftPossible ? Utils.getColoredString("╗", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("║", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row + 1][column - 4] = isLeftPossible ? Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
	}

	private Utils.BackgroundColorType getBackgroundColorForRoomID(int roomID) {
		return Utils.BackgroundColorType.values()[Utils.BackgroundColorType.CYAN.ordinal() - roomID];
	}

	private Utils.CharacterColorType getCharacterColorFromRoomID(int roomID) {
		return Utils.CharacterColorType.values()[Utils.CharacterColorType.CYAN.ordinal() - roomID];
	}
}