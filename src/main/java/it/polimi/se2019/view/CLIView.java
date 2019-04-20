package it.polimi.se2019.view;

import com.sun.javafx.scene.traversal.Direction;
import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.gamemap.Square;
import it.polimi.se2019.model.gamemap.SquareRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

import java.awt.image.DirectColorModel;
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

	public void displayMap() {
		GameMapRep gameMapRep = modelRep.getGameMapRep();
		SquareRep[][] map = gameMapRep.getMapRep();

		printMap(generateMapToPrint(gameMapRep.getMapRep()));

		try{
			gameMapRep.getPlayersCoordinates().forEach((player, coordinates) -> System.out.println(player + ": " + coordinates));
		}catch(NullPointerException e)
		{
			System.out.println("Player not initializes");
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

	private String[][] generateMapToPrint(SquareRep[][] map){
		int numOfRows = map.length;
		int numOfColumns = map[0].length;

		String[][] mapToPrint = new String[numOfRows * 5][numOfColumns * 9];

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				fillSquare(map[i][j], mapToPrint);
			}
		}

		return mapToPrint;
	}

	private Coordinates convertCoordinates(Coordinates coordinatesToConvert){ return new Coordinates(2 + coordinatesToConvert.getRow() * 5,4 + coordinatesToConvert.getColumn() * 9); }

	private void fillSquare(SquareRep squareRep, String[][] mapToPrint){
		boolean[] possibleDirection = squareRep.getPossibleDirection();

		Coordinates coordinates = convertCoordinates(squareRep.getCoordinates());



		//UP Door
		mapToPrint[coordinates.getRow()-2][coordinates.getColumn()-2] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[0] && squareRep.getRoomID() == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()-1][squareRep.getCoordinates().getColumn()].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()-2][coordinates.getColumn()-1] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[0]? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()-2][coordinates.getColumn()] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[0]? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()-2][coordinates.getColumn()+1] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[0]? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()-2][coordinates.getColumn()+2] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[0] && squareRep.getRoomID() == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()-1][squareRep.getCoordinates().getColumn()].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());

		//RIGHT Door
		mapToPrint[coordinates.getRow()-1][coordinates.getColumn()+3] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[1] && squareRep.getRoomID() == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()+1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()-1][coordinates.getColumn()+4] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[1] && squareRep.getRoomID() == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()+1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()][coordinates.getColumn()+3] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[1] ? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()][coordinates.getColumn()+4] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[1] ? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()+1][coordinates.getColumn()+3] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[1] && squareRep.getRoomID() == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()+1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()+1][coordinates.getColumn()+4] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[1] && squareRep.getRoomID() == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()+1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());

		//DOWN Door
		mapToPrint[coordinates.getRow()+2][coordinates.getColumn()-2] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[2] && squareRep.getRoomID() == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()+1][squareRep.getCoordinates().getColumn()].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()+2][coordinates.getColumn()-1] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[2]? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()+2][coordinates.getColumn()] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[2]? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()+2][coordinates.getColumn()+1] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[2]? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()+2][coordinates.getColumn()+2] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[2] && squareRep.getRoomID() == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()+1][squareRep.getCoordinates().getColumn()].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());

		//LEFT Door
		mapToPrint[coordinates.getRow()-1][coordinates.getColumn()-3] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[3] && squareRep.getRoomID() == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()-1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()-1][coordinates.getColumn()-4] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[3] && squareRep.getRoomID() == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()-1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()][coordinates.getColumn()-3] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[3]? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()][coordinates.getColumn()-4] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[3]? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()+1][coordinates.getColumn()-3] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[3] && squareRep.getRoomID() == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()-1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()+1][coordinates.getColumn()-4] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT, possibleDirection[3] && squareRep.getRoomID() == modelRep.getGameMapRep().getMapRep()[squareRep.getCoordinates().getRow()][squareRep.getCoordinates().getColumn()-1].getRoomID()? Utils.DEFAULT_BACKGROUND : Utils.CYAN_BACKGROUND - squareRep.getRoomID());


		mapToPrint[coordinates.getRow()-1][coordinates.getColumn()-2] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()-1][coordinates.getColumn()-1] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()-1][coordinates.getColumn()] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()-1][coordinates.getColumn()+1] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()-1][coordinates.getColumn()+2] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()][coordinates.getColumn()-2] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()][coordinates.getColumn()-1] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()][coordinates.getColumn()] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()][coordinates.getColumn()+1] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()][coordinates.getColumn()+2] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()+1][coordinates.getColumn()-2] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()+1][coordinates.getColumn()-1] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()+1][coordinates.getColumn()] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()+1][coordinates.getColumn()+1] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);
		mapToPrint[coordinates.getRow()+1][coordinates.getColumn()+2] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.DEFAULT_BACKGROUND);

	}

	private void fillSquareCorners(SquareRep squareRep, String[][] mapToPrint){
		Coordinates coordinates = squareRep.getCoordinates();

		//Fills the corners
		mapToPrint[coordinates.getRow()-2][coordinates.getColumn()-4] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()-2][coordinates.getColumn()-3] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()-2][coordinates.getColumn()+4] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()-2][coordinates.getColumn()+3] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()+2][coordinates.getColumn()-4] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()+2][coordinates.getColumn()-3] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()+2][coordinates.getColumn()+4] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.CYAN_BACKGROUND - squareRep.getRoomID());
		mapToPrint[coordinates.getRow()+2][coordinates.getColumn()+3] = Utils.getColoredString(" ", Utils.DEFAULT_TEXT,Utils.CYAN_BACKGROUND - squareRep.getRoomID());
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