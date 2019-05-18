package it.polimi.se2019.view.client;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.weapons.WeaponRep;
import it.polimi.se2019.model.gameboard.KillShotRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.gamemap.SpawnSquareRep;
import it.polimi.se2019.model.gamemap.SquareRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.model.player.damagestatus.DamageStatusRep;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static it.polimi.se2019.utils.GameConstants.NUM_OF_COLUMNS_IN_SQUARE;
import static it.polimi.se2019.utils.GameConstants.NUM_OF_ROWS_IN_SQUARE;
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
	public void displayGame() {
		displayGame(null);
	}

	public void displayGame(List<Coordinates> reachableCoordinates) {
		if (!Utils.DEBUG_CLI) {
			setCursorHome();
			cleanConsole();
		}

		CLIView.print("\n");

		displayPlayers();

		CLIView.print("\n");

		displayGameBoard();

		displayWeapons();

		CLIView.print("\n");

		if (mapToPrint == null)
			initializeMapToPrint(modelRep.getGameMapRep().getMapRep());
		updateMapToPrint(reachableCoordinates);
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

		for (int i = 0; i < mapRep.length; i++) {
			for (int j = 0; j < mapRep[0].length; j++) {
				fillEmpty(mapRep[i][j]);
				fillWithElements(mapRep[i][j]);
			}
		}

		updatePlayerPosition();
	}

	private void updateMapToPrint(List<Coordinates> coordinates) {
		GameMapRep gameMapRep = modelRep.getGameMapRep();
		SquareRep[][] mapRep = gameMapRep.getMapRep();

		for (int i = 0; i < mapRep.length; i++) {
			for (int j = 0; j < mapRep[0].length; j++) {
				fillEmpty(mapRep[i][j]);
				fillWithElements(mapRep[i][j], coordinates);
			}
		}
		updatePlayerPosition();
	}

	private void updatePlayerPosition() {
		GameMapRep gameMapRep = modelRep.getGameMapRep();
		List<PlayerRep> playersRep = modelRep.getPlayersRep();
		for (PlayerRep playerRep : playersRep) {
			Coordinates playerCoordinates = gameMapRep.getPlayersCoordinates().get(playerRep.getPlayerName());
			if (playerCoordinates != null)
				mapToPrint[convertCoordinates(playerCoordinates).getRow() - 1][convertCoordinates(playerCoordinates).getColumn() - 2 + playerRep.getPlayerID()] = Color.getColoredString("▲", playerRep.getPlayerColor());
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
			mapToPrint[row + 2][column - 2] = cards[0];
			mapToPrint[row + 2][column] = cards[1];
			mapToPrint[row + 2][column + 2] = cards[2];
		}
	}

	private void fillWithElements(SquareRep squareRep, List<Coordinates> coordinates) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		String[] cards = squareRep.getElementsToPrint();

		if (squareRep.getRoomID() != -1) {
			if (coordinates != null && coordinates.contains(squareRep.getCoordinates()))
				mapToPrint[row][column] = Color.getColoredCell(Color.BackgroundColorType.WHITE);
			mapToPrint[row + 2][column - 2] = cards[0];
			mapToPrint[row + 2][column] = cards[1];
			mapToPrint[row + 2][column + 2] = cards[2];
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

		for (int i = -(NUM_OF_ROWS_IN_SQUARE / 2) + 2; i <= NUM_OF_ROWS_IN_SQUARE / 2 - 2; i++) {
			for (int j = -(NUM_OF_COLUMNS_IN_SQUARE / 2) + 2; j <= NUM_OF_COLUMNS_IN_SQUARE / 2 - 2; j++) {
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

	private void displayWeapons() {
		SquareRep[][] map = modelRep.getGameMapRep().getMapRep();
		Coordinates redSpawnCoordinates = modelRep.getGameMapRep().getSpawncoordinats(AmmoType.RED_AMMO);
		List<WeaponRep> redWeapons = ((SpawnSquareRep) map[redSpawnCoordinates.getRow()][redSpawnCoordinates.getColumn()]).getWeaponsRep();
		Coordinates yellowSpawnCoordinates = modelRep.getGameMapRep().getSpawncoordinats(AmmoType.YELLOW_AMMO);
		List<WeaponRep> yellowWeapons = ((SpawnSquareRep) map[yellowSpawnCoordinates.getRow()][yellowSpawnCoordinates.getColumn()]).getWeaponsRep();
		Coordinates blueSpawnCoordinates = modelRep.getGameMapRep().getSpawncoordinats(AmmoType.BLUE_AMMO);
		List<WeaponRep> blueWeapons = ((SpawnSquareRep) map[blueSpawnCoordinates.getRow()][blueSpawnCoordinates.getColumn()]).getWeaponsRep();
		CLIPrinter.moveCursorUP(5);
		CLIPrinter.moveCursorRIGHT(150);
		CLIView.print(Utils.fillWithSpacesColored("RED SPAWN", 23, Color.CharacterColorType.RED));
		CLIView.print(Utils.fillWithSpacesColored("YELLOW SPAWN", 23, Color.CharacterColorType.YELLOW));
		CLIView.print(Utils.fillWithSpacesColored("BLUE SPAWN", 23, Color.CharacterColorType.BLUE));
		CLIView.print("\n");
		CLIPrinter.moveCursorRIGHT(150);
		CLIView.print(weaponRepToString(redWeapons.get(0)));
		CLIView.print(weaponRepToString(yellowWeapons.get(0)));
		CLIView.print(weaponRepToString(blueWeapons.get(0)));
		CLIView.print("\n");
		CLIPrinter.moveCursorRIGHT(150);
		CLIView.print(weaponRepToString(redWeapons.get(1)));
		CLIView.print(weaponRepToString(yellowWeapons.get(1)));
		CLIView.print(weaponRepToString(blueWeapons.get(1)));
		CLIView.print("\n");
		CLIPrinter.moveCursorRIGHT(150);
		CLIView.print(weaponRepToString(redWeapons.get(2)));
		CLIView.print(weaponRepToString(yellowWeapons.get(2)));
		CLIView.print(weaponRepToString(blueWeapons.get(2)));
		CLIView.print("\n\n");
	}

	private String weaponRepToString(WeaponRep weaponRep) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Utils.fillWithSpaces(weaponRep.getCardName(), 18));
		for (AmmoType ammoType : weaponRep.getPrice())
			stringBuilder.append(Color.getColoredString("●", ammoType.getCharacterColorType()));
		for (int i = weaponRep.getPrice().size(); i < 5; i++) {
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();
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
		CLIView.print(Utils.fillWithSpaces((220 - modelRep.getGameMapRep().getNumOfColumns() * NUM_OF_COLUMNS_IN_SQUARE) / 2 + 4));
		for (int i = 0; i < modelRep.getGameMapRep().getNumOfColumns(); i++) {
			CLIView.print("        " + (i + 1) + "        ");
		}
		CLIView.print("\n\n");
		for (int i = 0; i < mapToPrint.length; i++) {
			CLIView.print(Utils.fillWithSpaces((220 - modelRep.getGameMapRep().getNumOfColumns() * NUM_OF_COLUMNS_IN_SQUARE) / 2));
			CLIView.print(Utils.fillWithSpaces((i - (i / NUM_OF_ROWS_IN_SQUARE) * NUM_OF_ROWS_IN_SQUARE) == NUM_OF_ROWS_IN_SQUARE / 2 ? (i / NUM_OF_ROWS_IN_SQUARE + 1) + "" : "", 4));
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
		return Collections.max(Arrays.asList(
				playerRep.getPowerupCards().size(),
				playerRep.getWeaponReps().size(),
				playerRep.getDamageStatusRep().numOfMacroActions(),
				AmmoType.values().length));
	}

	private void printOwnPlayerLine(PlayerRep playerRep, int lineIndex) {
		StringBuilder stringBuilder = new StringBuilder();
		DamageStatusRep damageStatusRep = playerRep.getDamageStatusRep();

		//Possible move
		if (lineIndex + 1 <= damageStatusRep.numOfMacroActions()) {
			stringBuilder.append(" [");
			stringBuilder.append(lineIndex + 1);
			stringBuilder.append("] ");
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
		if (lineIndex + 1 <= playerRep.getWeaponReps().size())
			stringBuilder.append(weaponRepToString(playerRep.getWeaponReps().get(lineIndex)));
		else
			stringBuilder.append(Utils.fillWithSpaces(21));
		stringBuilder.append("\t\t");

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

		mapToPrint = new String[numOfRows * NUM_OF_ROWS_IN_SQUARE][numOfColumns * NUM_OF_COLUMNS_IN_SQUARE];

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				fillSquare(map[i][j], mapToPrint);
			}
		}

		return mapToPrint;
	}

	private Coordinates convertCoordinates(Coordinates coordinatesToConvert) {
		return new Coordinates(4 + coordinatesToConvert.getRow() * NUM_OF_ROWS_IN_SQUARE, 8 + coordinatesToConvert.getColumn() * NUM_OF_COLUMNS_IN_SQUARE);
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
			for (int i = -NUM_OF_ROWS_IN_SQUARE / 2; i <= NUM_OF_ROWS_IN_SQUARE / 2; i++) {
				for (int j = -NUM_OF_COLUMNS_IN_SQUARE / 2; j <= NUM_OF_COLUMNS_IN_SQUARE / 2; j++) {
					mapToPrint[row + i][column + j] = Color.getColoredCell(Color.BackgroundColorType.DEFAULT);
				}
			}
		}


	}

	private void fillSquareCorners(SquareRep squareRep) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		Color.CharacterColorType squareColor = squareRep.getSquareColor();

		//up left
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = Color.getColoredString("╔", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 2] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 3] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 4] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 2] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 3] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 4] = " ";

		//up right
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = Color.getColoredString("╗", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 2] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 3] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 4] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 2] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 3] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 4] = " ";

		//down right
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 2] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 3] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 4] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = Color.getColoredString("╚", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 2] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 3] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 4] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);

		//down left
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 2] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 3] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 4] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = Color.getColoredString("╝", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 2] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 3] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 4] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
	}

	private void fillUpDoor(SquareRep squareRep) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		Color.CharacterColorType squareColor = squareRep.getSquareColor();
		boolean isUpPossible = squareRep.getPossibleDirection()[CardinalDirection.UP.ordinal()];

		if (isUpPossible) {
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column - 3] = Color.getColoredString("╝", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column - 2] = " ";
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column - 1] = " ";
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column] = " ";
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column + 1] = " ";
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column + 2] = " ";
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column + 3] = Color.getColoredString("╚", squareColor, Color.BackgroundColorType.DEFAULT);
		} else {
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column - 3] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column - 2] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column - 1] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column + 1] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column + 2] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2][column + 3] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		}

		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column - 3] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column - 2] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column - 1] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column + 1] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column + 2] = " ";
		mapToPrint[row - NUM_OF_ROWS_IN_SQUARE / 2 + 1][column + 3] = " ";
	}

	private void fillRightDoor(SquareRep squareRep) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		Color.CharacterColorType squareColor = squareRep.getSquareColor();
		boolean isRightPossible = squareRep.getPossibleDirection()[CardinalDirection.RIGHT.ordinal()];

		if (isRightPossible) {
			mapToPrint[row - 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row + 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row + 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);

			mapToPrint[row - 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = Color.getColoredString("╚", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = " ";
			mapToPrint[row][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = " ";
			mapToPrint[row + 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = " ";
			mapToPrint[row + 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = Color.getColoredString("╔", squareColor, Color.BackgroundColorType.DEFAULT);
		} else {
			mapToPrint[row - 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row + 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row + 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2 - 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);

			mapToPrint[row - 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row - 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row + 1][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row + 2][column + NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
		}
	}

	private void fillDownDoor(SquareRep squareRep) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		Color.CharacterColorType squareColor = squareRep.getSquareColor();
		boolean isDownPossible = squareRep.getPossibleDirection()[CardinalDirection.DOWN.ordinal()];

		if (isDownPossible) {
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column - 3] = Color.getColoredString("╗", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column - 2] = " ";
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column - 1] = " ";
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column] = " ";
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column + 1] = " ";
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column + 2] = " ";
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column + 3] = Color.getColoredString("╔", squareColor, Color.BackgroundColorType.DEFAULT);
		} else {
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column - 3] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column - 2] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column - 1] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column + 1] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column + 2] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2][column + 3] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
		}

		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column - 3] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column - 2] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column - 1] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column + 1] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column + 2] = " ";
		mapToPrint[row + NUM_OF_ROWS_IN_SQUARE / 2 - 1][column + 3] = " ";
	}

	private void fillLeftDoor(SquareRep squareRep) {
		int row = convertCoordinates(squareRep.getCoordinates()).getRow();
		int column = convertCoordinates(squareRep.getCoordinates()).getColumn();
		Color.CharacterColorType squareColor = squareRep.getSquareColor();
		boolean isLeftPossible = squareRep.getPossibleDirection()[CardinalDirection.LEFT.ordinal()];

		if (isLeftPossible) {
			mapToPrint[row - 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row + 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row + 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT);

			mapToPrint[row - 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = Color.getColoredString("╝", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = " ";
			mapToPrint[row][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = " ";
			mapToPrint[row + 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = " ";
			mapToPrint[row + 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = Color.getColoredString("╗", squareColor, Color.BackgroundColorType.DEFAULT);
		} else {
			mapToPrint[row - 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row - 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row + 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
			mapToPrint[row + 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2 + 1] = Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);

			mapToPrint[row - 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row - 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row + 1][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
			mapToPrint[row + 2][column - NUM_OF_COLUMNS_IN_SQUARE / 2] = " ";
		}

		mapToPrint[row - 1][column - 3] = isLeftPossible ? Color.getColoredString("╝", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row - 1][column - 4] = isLeftPossible ? Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row][column - 3] = isLeftPossible ? Color.getColoredCell(Color.BackgroundColorType.DEFAULT) : Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row][column - 4] = isLeftPossible ? Color.getColoredCell(Color.BackgroundColorType.DEFAULT) : Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 1][column - 3] = isLeftPossible ? Color.getColoredString("╗", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString("║", squareColor, Color.BackgroundColorType.DEFAULT);
		mapToPrint[row + 1][column - 4] = isLeftPossible ? Color.getColoredString("═", squareColor, Color.BackgroundColorType.DEFAULT) : Color.getColoredString(" ", squareColor, Color.BackgroundColorType.DEFAULT);
	}
}
