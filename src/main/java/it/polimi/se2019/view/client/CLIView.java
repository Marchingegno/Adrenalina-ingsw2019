package it.polimi.se2019.view.client;

import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.gamemap.SquareRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
		repPrinter.displayGame();
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
		System.out.println(stringToShow);
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
		if(mapToPrint == null)
			initializeMapToPrint(modelRep.getGameMapRep().getMapRep());
		updateMapToPrint();
		displayMap();
		//displayPlayers();
		//displayGameBoard();
	}

	private void updateMapToPrint(){
		GameMapRep gameMapRep = modelRep.getGameMapRep();
		ArrayList<PlayerRep> playersRep = modelRep.getPlayersRep();
		for (int i = 0; i < playersRep.size(); i++) {
				Coordinates playerCoordinates = convertCoordinates(gameMapRep.getPlayersCoordinates().get(playersRep.get(i).getPlayerName()));
				mapToPrint[playerCoordinates.getRow() - 1][playerCoordinates.getColumn() - 2 + i] = Utils.getColoredString("♦", playersRep.get(i).getPlayerColor(), Utils.DEFAULT_BACKGROUND);
		}
	}

	public void displayMap() {
		GameMapRep gameMapRep = modelRep.getGameMapRep();

		printMap(mapToPrint);

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

	private void printMap(String[][] map){
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				System.out.print(map[i][j]);
			}
			System.out.print("\n");
		}
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

		fillSquareCorners(squareRep);
		fillUpDoor(squareRep);
		fillRightDoor(squareRep);
		fillDownDoor(squareRep);
		fillLeftDoor(squareRep);

		mapToPrint[row-1][column-2] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row-1][column-1] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row-1][column] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row-1][column+1] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row-1][column+2] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row][column-2] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row][column-1] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row][column] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row][column+1] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row][column+2] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row+1][column-2] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row+1][column-1] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row+1][column] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row+1][column+1] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
		mapToPrint[row+1][column+2] = Utils.getColoredCell(Utils.DEFAULT_BACKGROUND);
	}

	private void fillSquareCorners(SquareRep squareRep){
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		int roomID = squareRep.getRoomID();

		/*mapToPrint[row-2][column-4] = Utils.getColoredCell(Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row-2][column-3] = Utils.getColoredCell(Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row-2][column+4] = Utils.getColoredCell(Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row-2][column+3] = Utils.getColoredCell(Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+2][column-4] = Utils.getColoredCell(Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+2][column-3] = Utils.getColoredCell(Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+2][column+4] = Utils.getColoredCell(Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+2][column+3] = Utils.getColoredCell(Utils.CYAN_BACKGROUND - roomID);*/

		mapToPrint[row-2][column-4] = Utils.getColoredString(" ", Utils.DEFAULT_COLOR, Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row-2][column-3] = Utils.getColoredString("┌", Utils.DEFAULT_COLOR, Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row-2][column+4] = Utils.getColoredString(" ", Utils.DEFAULT_COLOR, Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row-2][column+3] = Utils.getColoredString("┐", Utils.DEFAULT_COLOR, Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+2][column-4] = Utils.getColoredString(" ", Utils.DEFAULT_COLOR, Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+2][column-3] = Utils.getColoredString("└", Utils.DEFAULT_COLOR, Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+2][column+4] = Utils.getColoredString(" ", Utils.DEFAULT_COLOR, Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+2][column+3] = Utils.getColoredString("┘", Utils.DEFAULT_COLOR, Utils.CYAN_BACKGROUND - roomID);
	}

	private void fillUpDoor(SquareRep squareRep){
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		int roomID = squareRep.getRoomID();
		boolean isUpPossible = squareRep.getPossibleDirection()[CardinalDirection.UP.ordinal()];

		mapToPrint[row-2][column-2] = Utils.getColoredCell( isUpPossible && roomID == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()-1][squareRep.getCoordinates().getColumn()].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row-2][column-1] = Utils.getColoredCell( isUpPossible? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row-2][column] = Utils.getColoredCell( isUpPossible? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row-2][column+1] = Utils.getColoredCell( isUpPossible? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row-2][column+2] = Utils.getColoredCell( isUpPossible && roomID == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()-1][squareRep.getCoordinates().getColumn()].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
	}

	private void fillRightDoor(SquareRep squareRep){
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		int roomID = squareRep.getRoomID();
		boolean isRightPossible = squareRep.getPossibleDirection()[CardinalDirection.RIGHT.ordinal()];

		mapToPrint[row-1][column+3] = Utils.getColoredCell( isRightPossible && roomID == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()+1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row-1][column+4] = Utils.getColoredCell( isRightPossible && roomID == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()+1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row][column+3] = Utils.getColoredCell( isRightPossible ? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row][column+4] = Utils.getColoredCell( isRightPossible ? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+1][column+3] = Utils.getColoredCell( isRightPossible && roomID == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()+1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+1][column+4] = Utils.getColoredCell( isRightPossible && roomID == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()+1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
	}

	private void fillDownDoor(SquareRep squareRep){
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		int roomID = squareRep.getRoomID();
		boolean isDownPossible = squareRep.getPossibleDirection()[CardinalDirection.DOWN.ordinal()];

		mapToPrint[row+2][column-2] = Utils.getColoredCell( isDownPossible && roomID == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()+1][squareRep.getCoordinates().getColumn()].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+2][column-1] = Utils.getColoredCell( isDownPossible? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+2][column] = Utils.getColoredCell( isDownPossible? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+2][column+1] = Utils.getColoredCell( isDownPossible? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+2][column+2] = Utils.getColoredCell( isDownPossible && roomID == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()+1][squareRep.getCoordinates().getColumn()].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
	}

	private void fillLeftDoor(SquareRep squareRep){
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		int roomID = squareRep.getRoomID();
		boolean isLeftPossible = squareRep.getPossibleDirection()[CardinalDirection.LEFT.ordinal()];

		mapToPrint[row-1][column-3] = Utils.getColoredCell( isLeftPossible && roomID == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()-1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row-1][column-4] = Utils.getColoredCell( isLeftPossible && roomID == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()-1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row][column-3] = Utils.getColoredCell( isLeftPossible? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row][column-4] = Utils.getColoredCell( isLeftPossible? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+1][column-3] = Utils.getColoredCell( isLeftPossible && roomID == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()-1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
		mapToPrint[row+1][column-4] = Utils.getColoredCell( isLeftPossible && roomID == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()-1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - roomID);
	}
}