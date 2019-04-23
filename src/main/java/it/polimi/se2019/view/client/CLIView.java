package it.polimi.se2019.view.client;

import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.gamemap.SquareRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author MarcerAndrea
 * @author Desno365
 */
public class CLIView extends RemoteView {

	private ModelRep modelRep;
	private Scanner scanner;
	private RepPrinter repPrinter;

	public CLIView() {
		this.modelRep = new ModelRep();
		this.scanner = new Scanner(System.in);
		this.repPrinter = new RepPrinter(this.modelRep);
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
		decimalFormat.setMaximumFractionDigits(2);
		System.out.println("The match will start in " + decimalFormat.format(delayInMs / 1000d) + " seconds.");
	}

	@Override
	public void displayTimerStopped() {
		System.out.println("Timer for starting the match cancelled.");
	}

	@Override
	public void askMapAndSkullsToUse() {
		System.out.println("\n\nMatch ready to start. Select your preferred configuration.");
		int mapIndex = askMapToUse();
		int skulls = askSkullsForGame();
		System.out.println("Waiting for other clients to answer...");
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
		for(GameConstants.MapType map : GameConstants.MapType.values()) {
			System.out.println(map.ordinal() + ": " + map.getDescription());
		}
		return askForAnInteger(0, GameConstants.MapType.values().length - 1);
	}

	private int askSkullsForGame() {
		System.out.println("Select how many skulls you would like to use, min " + GameConstants.MIN_SKULLS + ", max " + GameConstants.MAX_SKULLS + ".");
		return askForAnInteger(GameConstants.MIN_SKULLS, GameConstants.MAX_SKULLS);
	}

	public void displayGame() {
		repPrinter.displayGame();
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


class RepPrinter{

	private ModelRep modelRep;
	private String[][] mapToPrint;

	public RepPrinter(ModelRep modelRep){
		this.modelRep = modelRep;
	}

	public void displayGame() {
		displayPlayers();

		System.out.println("\n\n");

		displayGameBoard();

		System.out.println("\n\n");

		if(mapToPrint == null)
			initializeMapToPrint(modelRep.getGameMapRep().getMapRep());
		updateMapToPrint();
		displayMap();

		displayOwnPlayer(modelRep.getPlayersRep().get(0));
	}

	private void updateMapToPrint(){
		GameMapRep gameMapRep = modelRep.getGameMapRep();
		SquareRep[][] mapRep = gameMapRep.getMapRep();
		ArrayList<PlayerRep> playersRep = modelRep.getPlayersRep();

		for (int i = 0; i < mapRep.length; i++) {
			for (int j = 0; j < mapRep[0].length; j++) {
				fillEmpty(mapRep[i][j]);
				fillCards(mapRep[i][j]);
			}
		}

		//update Players position
		for (int i = 0; i < playersRep.size(); i++) {
			if (gameMapRep.getPlayersCoordinates().get(playersRep.get(i).getPlayerName()) != null)
			{
				Coordinates playerCoordinates = convertCoordinates(gameMapRep.getPlayersCoordinates().get(playersRep.get(i).getPlayerName()));
				mapToPrint[playerCoordinates.getRow() - 1][playerCoordinates.getColumn() - 2 + i] = Utils.getColoredString("⧫", playersRep.get(i).getPlayerColor(), Utils.BackgroundColorType.DEFAULT);
			}
		}
	}

	private void fillCards(SquareRep squareRep){
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();

		if(squareRep.getRoomID() != -1){
			mapToPrint[row + 1][column-1] = Utils.getColoredCell(Utils.BackgroundColorType.YELLOW);
			mapToPrint[row + 1][column] = Utils.getColoredCell(Utils.BackgroundColorType.RED);
			mapToPrint[row + 1][column+1] = Utils.getColoredCell(Utils.BackgroundColorType.BLUE);
		}
	}

	private void fillEmpty(SquareRep squareRep){
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();

		for (int i = - 1; i < 2; i++) {
			for (int j = - 2; j < 3; j++) {
				mapToPrint[row + i][column + j] = Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT);
			}
		}
	}

	private void displayGameBoard() {
		GameBoardRep gameBoardRep = modelRep.getGameBoardRep();

		System.out.println("SKULLS\tDOUBLEKILL");
	}

	private void displayPlayers() {
		for (PlayerRep playerRep : modelRep.getPlayersRep() ) {
			System.out.println(Utils.getColoredCell(Utils.BackgroundColorType.BLACK) + " " + Utils.getColoredString(playerRep.getPlayerName(), playerRep.getPlayerColor(), Utils.BackgroundColorType.DEFAULT) + "\tDAMAGE\tMARKS\t");
		}
	}

	private void displayMap(){
		for (int i = 0; i < mapToPrint.length; i++) {
			for (int j = 0; j < mapToPrint[0].length; j++) {
				System.out.print(mapToPrint[i][j]);
			}
			System.out.print("\n");
		}
	}

	private void displayOwnPlayer(PlayerRep playerRep){
		System.out.println(Utils.getColoredString(playerRep.getPlayerName(), playerRep.getPlayerColor(), Utils.BackgroundColorType.DEFAULT));
		System.out.println(Utils.getColoredString("◼", Utils.CharacterColorType.YELLOW, Utils.BackgroundColorType.DEFAULT) +
														" Powerup 1\t\t" +
														Utils.getColoredString("◼", Utils.CharacterColorType.YELLOW, Utils.BackgroundColorType.DEFAULT) +
														Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
														Utils.getColoredString("◼", Utils.CharacterColorType.DEFAULT, Utils.BackgroundColorType.DEFAULT) +
														" Weapon 1\t\t" +
														Utils.getColoredString("◼", Utils.CharacterColorType.YELLOW, Utils.BackgroundColorType.DEFAULT) +
														Utils.getColoredString("◼", Utils.CharacterColorType.YELLOW, Utils.BackgroundColorType.DEFAULT) +
														Utils.getColoredString("◼", Utils.CharacterColorType.DEFAULT, Utils.BackgroundColorType.DEFAULT)	);
		System.out.println(Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
														" Powerup 2\t\t" +
														Utils.getColoredString("◼", Utils.CharacterColorType.BLUE, Utils.BackgroundColorType.DEFAULT) +
														Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
														Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
														" Weapon 2\t\t" +
														Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
														Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
														Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT)	);
		System.out.println(Utils.getColoredString("◼", Utils.CharacterColorType.BLUE, Utils.BackgroundColorType.DEFAULT) +
														" Powerup 3\t\t" +
														Utils.getColoredString("◼", Utils.CharacterColorType.RED, Utils.BackgroundColorType.DEFAULT) +
														Utils.getColoredString("◼", Utils.CharacterColorType.DEFAULT, Utils.BackgroundColorType.DEFAULT) +
														Utils.getColoredString("◼", Utils.CharacterColorType.DEFAULT, Utils.BackgroundColorType.DEFAULT) +
														" Weapon 3\t\t" +
														Utils.getColoredString("◼", Utils.CharacterColorType.BLUE, Utils.BackgroundColorType.DEFAULT) +
														Utils.getColoredString("◼", Utils.CharacterColorType.DEFAULT, Utils.BackgroundColorType.DEFAULT) +
														Utils.getColoredString("◼", Utils.CharacterColorType.DEFAULT, Utils.BackgroundColorType.DEFAULT)	);
	}

	private String[][] initializeMapToPrint(SquareRep[][] map){
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

	private Coordinates convertCoordinates(Coordinates coordinatesToConvert){ return new Coordinates(2 + coordinatesToConvert.getRow() * 5,4 + coordinatesToConvert.getColumn() * 9); }

	private void fillSquare(SquareRep squareRep, String[][] mapToPrint){

		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();

		fillEmpty(squareRep);

		if (squareRep.getRoomID() != -1){
			fillSquareCorners(squareRep);
			fillUpDoor(squareRep);
			fillRightDoor(squareRep);
			fillDownDoor(squareRep);
			fillLeftDoor(squareRep);
		}
		else{
			for (int i = - 2; i < 3; i++) {
				for (int j = - 4; j < 5; j++) {
					mapToPrint[row + i][column + j] = Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT);
				}
			}
		}


	}

	private void fillSquareCorners(SquareRep squareRep){
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		int roomID = squareRep.getRoomID();

		mapToPrint[row-2][column-4] = Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row-2][column-3] = Utils.getColoredString("╔", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row-2][column+4] = Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row-2][column+3] = Utils.getColoredString("╗", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row+2][column-4] = Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row+2][column-3] = Utils.getColoredString("╚", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row+2][column+4] = Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row+2][column+3] = Utils.getColoredString("╝", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
	}

	private void fillUpDoor(SquareRep squareRep){
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		int roomID = squareRep.getRoomID();
		boolean isUpPossible = squareRep.getPossibleDirection()[CardinalDirection.UP.ordinal()];

		mapToPrint[row-2][column-2] = isUpPossible?  Utils.getColoredString("╝",getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row-2][column-1] = isUpPossible? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString( "═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row-2][column] = isUpPossible? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString( "═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row-2][column+1] = isUpPossible? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString( "═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row-2][column+2] = isUpPossible?  Utils.getColoredString("╚",getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
	}

	private void fillRightDoor(SquareRep squareRep){
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		int roomID = squareRep.getRoomID();
		boolean isRightPossible = squareRep.getPossibleDirection()[CardinalDirection.RIGHT.ordinal()];

		mapToPrint[row-1][column+3] = isRightPossible? Utils.getColoredString("╚", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("║", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row-1][column+4] = isRightPossible? Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row][column+3] = isRightPossible? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("║", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row][column+4] = isRightPossible? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row+1][column+3] = isRightPossible? Utils.getColoredString("╔", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("║", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row+1][column+4] = isRightPossible? Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
	}

	private void fillDownDoor(SquareRep squareRep){
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		int roomID = squareRep.getRoomID();
		boolean isDownPossible = squareRep.getPossibleDirection()[CardinalDirection.DOWN.ordinal()];

		mapToPrint[row+2][column-2] = isDownPossible?  Utils.getColoredString("╗",getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row+2][column-1] = isDownPossible? Utils.getColoredCell( Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row+2][column] = isDownPossible? Utils.getColoredCell( Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row+2][column+1] = isDownPossible? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row+2][column+2] = isDownPossible? Utils.getColoredString("╔",getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
	}

	private void fillLeftDoor(SquareRep squareRep){
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		int roomID = squareRep.getRoomID();
		boolean isLeftPossible = squareRep.getPossibleDirection()[CardinalDirection.LEFT.ordinal()];

		mapToPrint[row-1][column-3] = isLeftPossible? Utils.getColoredString("╝", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("║", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row-1][column-4] = isLeftPossible? Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row][column-3] = isLeftPossible? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("║", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row][column-4] = isLeftPossible? Utils.getColoredCell(Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row+1][column-3] = isLeftPossible? Utils.getColoredString("╗", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString("║", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
		mapToPrint[row+1][column-4] = isLeftPossible? Utils.getColoredString("═", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT) : Utils.getColoredString(" ", getCharacterColorFromRoomID(roomID), Utils.BackgroundColorType.DEFAULT);
	}

	private Utils.BackgroundColorType getBackgroundColorForRoomID(int roomID) {
		return Utils.BackgroundColorType.values()[Utils.BackgroundColorType.CYAN.ordinal() - roomID];
	}

	private Utils.CharacterColorType getCharacterColorFromRoomID(int roomID) {
		return Utils.CharacterColorType.values()[Utils.CharacterColorType.CYAN.ordinal() - roomID];
	}
}