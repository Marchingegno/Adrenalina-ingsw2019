package it.polimi.se2019.view.client;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gameboard.KillShotRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.gamemap.SquareRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.model.player.damagestatus.DamageStatusRep;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2019.view.client.CLIPrinter.cleanConsole;
import static it.polimi.se2019.view.client.CLIPrinter.setCursorHome;

/**
 * Helps printing the information.
 */
class RepPrinter {

	private ModelRep modelRep;
	private String[][] mapToPrint;

	RepPrinter(ModelRep modelRep) {
		this.modelRep = modelRep;
	}

	/**
	 * Displays all the game board.
	 */
	void displayGame() {
		if (!Utils.DEBUG_CLI) {
			setCursorHome();
			cleanConsole();
		}

		CLIView.print("\n");

		displayPlayers();

		CLIView.print("\n");

		displayGameBoard();

		CLIView.print("\n\n\n");

		if (mapToPrint == null)
			initializeMapToPrint(modelRep.getGameMapRep().getMapRep());
		updateMapToPrint();
		displayMap();

		CLIView.print("\n\n\n");

		displayOwnPlayer();

		CLIView.print("\n\n\n");
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
				fillWithElements(mapRep[i][j]);
			}
		}

		//update Players position
		for (PlayerRep playerRep : playersRep) {
			try {
				Coordinates playerCoordinates = convertCoordinates(gameMapRep.getPlayersCoordinates().get(playerRep.getPlayerName()));
				mapToPrint[playerCoordinates.getRow() - 1][playerCoordinates.getColumn() - 2 + playerRep.getPlayerID()] = Color.getColoredString("▲", playerRep.getPlayerColor());
			} catch (NullPointerException e) {
				Utils.logInfo(playerRep.getPlayerName() + " has no position");
			}
		}
	}

	/**
	 * Adds to the map to print in the correct square the ammo of the ammo card.
	 *
	 * @param squareRep
	 */
	private void fillWithElements(SquareRep squareRep) {
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
		CLIView.printLine("\n\n" +
				getSkullString() + "\n\n" +
				getKillShotTrackString() + "\n\n" +
				getDoubleKillString() + "\n");
	}

	private String getSkullString() {
		int skulls = modelRep.getGameBoardRep().getRemainingSkulls();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Skulls:\t\t");
		for (int i = 0; i < GameConstants.MAX_SKULLS; i++) {
			stringBuilder.append(Color.getColoredString("●", i < (GameConstants.MAX_SKULLS - skulls) ? Color.CharacterColorType.BLACK : Color.CharacterColorType.RED));
		}
		return stringBuilder.toString();
	}

	private String getKillShotTrackString() {
		List<KillShotRep> killShotTrackRep = modelRep.getGameBoardRep().getKillShoots();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Killshot:\t|");
		for (KillShotRep killShotRep : killShotTrackRep) {
			stringBuilder.append(Color.getColoredString(killShotRep.isOverkill() ? "●●" : "●", killShotRep.getPlayerColor()));
			stringBuilder.append("|");
		}
		return stringBuilder.toString();
	}

	private String getDoubleKillString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("DoubleKill:\t|");
		for (Color.CharacterColorType doubleKiller : modelRep.getGameBoardRep().getDoubleKills()) {
			stringBuilder.append(Color.getColoredString("●", doubleKiller));
			stringBuilder.append("|");
		}
		return stringBuilder.toString();
	}

	private void displayPlayers() {
		StringBuilder stringBuilder = new StringBuilder();
		for (PlayerRep playerRep : modelRep.getPlayersRep()) {
			stringBuilder.append(Color.getColoredString("● ", playerRep.getPlayerName().equals(modelRep.getGameBoardRep().getCurrentPlayer()) ? Color.CharacterColorType.WHITE : Color.CharacterColorType.BLACK));
			stringBuilder.append(Color.getColoredString(playerRep.getPlayerName(), playerRep.getPlayerColor()));
			for (int i = 0; i < GameConstants.MAX_NICKNAME_LENGHT - playerRep.getPlayerName().length(); i++) {
				stringBuilder.append(" ");
			}

			stringBuilder.append(getDamageBoard(playerRep.getDamageBoard()));

			stringBuilder.append("\t\t\t|");
			stringBuilder.append(getMarksBoard(playerRep.getMarks()));

			CLIView.printLine(stringBuilder.toString());
			stringBuilder = new StringBuilder();
		}
	}


	private String getDamageBoard(List<Color.CharacterColorType> damageBoard) {
		ArrayList<String> strings = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();
		strings.add("|");
		for (Color.CharacterColorType damage : damageBoard) {
			strings.add(Color.getColoredString("●", damage));
		}
		for (int i = damageBoard.size(); i < 11; i++) {
			strings.add("●");
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
			stringBuilder.append(Color.getColoredString("●", damage));
		}
		stringBuilder.append("|");
		return stringBuilder.toString();
	}

	private void displayMap() {
		for (int i = 0; i < mapToPrint.length; i++) {
			CLIView.print("\t\t\t\t\t\t\t\t\t\t");
			for (int j = 0; j < mapToPrint[0].length; j++) {
				CLIView.print(mapToPrint[i][j]);
			}
			CLIView.print("\n");
		}
	}

	private void displayOwnPlayer() {
		PlayerRep playerRep = modelRep.getClientPlayerRep();
		CLIView.print(Color.getColoredString(playerRep.getPlayerName(), playerRep.getPlayerColor(), Color.BackgroundColorType.DEFAULT));
		CLIView.printLine(" [" + playerRep.getPoints() + "]");
		for (int i = 0; i < getNumOfLine(playerRep); i++) {
			printOwnPlayerLine(playerRep, i);
		}
	}

	private int getNumOfLine(PlayerRep playerRep) {
		//playerRep.getPowerupCards();
		playerRep.getDamageStatusRep().numOfMacroActions();
		playerRep.getDamageStatusRep().numOfMacroActions();
		//TODO add weapons
		return 3;
	}

	private void printOwnPlayerLine(PlayerRep playerRep, int lineIndex) {
		StringBuilder stringBuilder = new StringBuilder();
		DamageStatusRep damageStatusRep = playerRep.getDamageStatusRep();

		//Possible move
		if (lineIndex + 1 <= damageStatusRep.numOfMacroActions()) {
			stringBuilder.append(" [" + (lineIndex + 1) + "] ");
			stringBuilder.append(Utils.fillWithSpaces(damageStatusRep.getMacroActionName(lineIndex), 8));
			stringBuilder.append(Utils.fillWithSpaces(damageStatusRep.getMacroActionString(lineIndex), 7));
		}

		//Powerups
		stringBuilder.append(Color.getColoredString("● ", lineIndex + 1 <= playerRep.getPowerupCards().size() ?
				playerRep.getPowerupCards().get(lineIndex).getAssociatedAmmo().getCharacterColorType() :
				Color.CharacterColorType.BLACK));
		stringBuilder.append(Utils.fillWithSpaces(lineIndex + 1 <= playerRep.getPowerupCards().size() ?
				playerRep.getPowerupCards().get(lineIndex).getCardName() :
				"", 20));

		//Weapons
		stringBuilder.append(
				"Weapon 1\t" +
						Color.getColoredString("●", Color.CharacterColorType.YELLOW, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("●", Color.CharacterColorType.RED, Color.BackgroundColorType.DEFAULT) +
						Color.getColoredString("●", Color.CharacterColorType.BLACK, Color.BackgroundColorType.DEFAULT) +
						"\t");

		//Ammo
		if (lineIndex + 1 <= AmmoType.values().length) {
			AmmoType ammoType = AmmoType.values()[lineIndex];
			for (int i = 0; i < playerRep.getAmmo(ammoType); i++) {
				stringBuilder.append(Color.getColoredString("●", ammoType.getCharacterColorType()));
			}
		}
		CLIView.printLine(stringBuilder.toString());
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
