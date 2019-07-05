package it.polimi.se2019.view.client.cli;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCardRep;
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
import it.polimi.se2019.view.client.ModelRep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static it.polimi.se2019.utils.GameConstants.*;
import static it.polimi.se2019.view.client.cli.CLILoginPrinter.cleanConsole;
import static it.polimi.se2019.view.client.cli.CLILoginPrinter.setCursorHome;

/**
 * Helps printing the information in the rep.
 * @author MarcerAndrea
 */
public class RepPrinter {

	private ModelRep modelRep;
	private String[][] mapToPrint;

	public RepPrinter(ModelRep modelRep) {
		this.modelRep = modelRep;
	}

	/**
	 * Displays all the game board.
	 */
	public void displayGame() {
		displayGame(null);
	}

	void displayGame(List<Coordinates> reachableCoordinates) {
		if (!Utils.DEBUG_CLI) {
			setCursorHome();
			cleanConsole();
		}

		CLIView.print("\n");

		displayPlayers();

		CLIView.print("\n\n\n");

		displayGameBoard();

		CLIView.print("\n");

		if (mapToPrint == null) {
			mapToPrint = initializeMapToPrint(modelRep.getGameMapRep().getMapRep());
		}
		updateMapToPrint(reachableCoordinates);
		displayMap();

        CLILoginPrinter.moveCursorUP(AmmoType.values().length * (MAX_NUM_OF_WEAPONS_IN_SPAWN_SQUARE + 1) + 10);

		displayWeapons();

		CLIView.print("\n\n\n\n\n\n\n\n\n");

		displayOwnPlayer();

		CLIView.print("\n\n\n");
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
	 * @param squareRep Square to reset.
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
		CLIView.printLine(getSkullString() + "\n\n" +
				getKillShotTrackString() + "\n\n" +
				getDoubleKillString());
	}

	private void displayWeapons() {
		SquareRep[][] map = modelRep.getGameMapRep().getMapRep();

		for (AmmoType ammoType : AmmoType.values()) {
            Coordinates redSpawnCoordinates = modelRep.getGameMapRep().getSpawnCoordinates(ammoType);
			List<WeaponRep> weaponReps = ((SpawnSquareRep) map[redSpawnCoordinates.getRow()][redSpawnCoordinates.getColumn()]).getWeaponsRep();
			CLIView.printLine(Color.getColoredString(ammoType.getCharacterColorType().toString() + " SPAWN", ammoType.getCharacterColorType()));

			for (int i = 0; i < MAX_NUM_OF_WEAPONS_IN_SPAWN_SQUARE; i++) {
				if (i < weaponReps.size())
					CLIView.printLine(getWeaponRepString(weaponReps.get(i)));
				else
					CLIView.print("\n");
			}
			CLIView.print("\n");
		}

        CLILoginPrinter.moveCursorDOWN(5);
	}

	String getWeaponRepString(WeaponRep weaponRep) {
		return getWeaponRepString(weaponRep, false);
	}

	private String getWeaponRepString(WeaponRep weaponRep, boolean showLoaded) {
		StringBuilder stringBuilder = new StringBuilder();
		for (AmmoType ammoType : weaponRep.getPrice())
			stringBuilder.append(Color.getColoredString("●", ammoType.getCharacterColorType()));
		for (int i = weaponRep.getPrice().size(); i < 4; i++) {
			stringBuilder.append(" ");
		}
		stringBuilder.append(weaponRep.getCardName());
		stringBuilder.append(Color.getColoredString(" ●", weaponRep.isLoaded() && showLoaded ? Color.CharacterColorType.WHITE : Color.CharacterColorType.BLACK));
		for (int i = weaponRep.getWeaponName().length(); i < 19; i++) {
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();
	}

	String getPowerupRepString(PowerupCardRep powerupRep) {
		return Color.getColoredString("● ", powerupRep.getAssociatedAmmo().getCharacterColorType()) +
				Utils.fillWithSpaces(powerupRep.getCardName(), 15);
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
			for (int i = 0; i < GameConstants.MAX_NICKNAME_LENGTH - playerRep.getPlayerName().length(); i++) {
				stringBuilder.append(" ");
			}

			stringBuilder.append(getDamageBoard(playerRep.getDamageBoard()));

			stringBuilder.append("\t\t\t|");
			stringBuilder.append(getMarksBoard(playerRep.getMarks()));

			stringBuilder.append("\t");

			if (!playerRep.getPlayerName().equals(modelRep.getClientPlayerRep().getPlayerName())) {
				for (WeaponRep weaponrep : playerRep.getWeaponReps()) {
					stringBuilder.append("\t" + weaponrep.getWeaponName());
				}
			}

			if (!playerRep.isConnected()) {
				stringBuilder.append("\t\t");
				stringBuilder.append(Color.getColoredString("DISCONNECTED", Color.CharacterColorType.RED));
			}

			CLIView.printLine(stringBuilder.toString());
			stringBuilder = new StringBuilder();
		}
	}


	private String getDamageBoard(List<Color.CharacterColorType> damageList) {
		ArrayList<String> strings = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();
		strings.add("|");
		List<Color.CharacterColorType> damageBoard = new ArrayList<>();
		for (int i = 0; i < damageList.size(); i++) {
			if (i < OVERKILL_DAMAGE)
				damageBoard.add(damageList.get(i));
		}
		for (Color.CharacterColorType damage : damageBoard) {
			strings.add(Color.getColoredString("●", damage));
		}
		for (int i = damageBoard.size(); i < OVERKILL_DAMAGE; i++) {
			strings.add("●");
		}
		strings.add("|");

		for (int i = 0; i < strings.size(); i++) {
			stringBuilder.append(strings.get(i));
			if (i == 2 || i == 5 || i == 10)
				stringBuilder.append("|");
		}
		return stringBuilder.toString();
	}

	private String getMarksBoard(List<Color.CharacterColorType> marksBoard) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Color.CharacterColorType damage : marksBoard) {
			stringBuilder.append(Color.getColoredString("●", damage));
		}
		stringBuilder.append("|");
		return stringBuilder.toString();
	}

	private void displayMap() {
        CLILoginPrinter.moveCursorRIGHT(84);
		for (int i = 0; i < modelRep.getGameMapRep().getNumOfColumns(); i++) {
			CLIView.print("        " + (i + 1) + "        ");
		}
		CLIView.print("\n\n");
		for (int i = 0; i < mapToPrint.length; i++) {
            CLILoginPrinter.moveCursorRIGHT(80);
			CLIView.print(Utils.fillWithSpaces((i - (i / NUM_OF_ROWS_IN_SQUARE) * NUM_OF_ROWS_IN_SQUARE) == NUM_OF_ROWS_IN_SQUARE / 2 ? (i / NUM_OF_ROWS_IN_SQUARE + 1) + "" : "", 4));
			for (int j = 0; j < mapToPrint[0].length; j++) {
				CLIView.print(mapToPrint[i][j]);
			}
			CLIView.print("\n");
		}
	}

	private void displayOwnPlayer() {
		PlayerRep playerRep = modelRep.getClientPlayerRep();
		CLIView.printLine(" ┌──────────────────┐                                                           ┌─────┐");
		CLIView.printLine(" │ " + Utils.fillWithSpacesColored(playerRep.getPlayerName(), MAX_NICKNAME_LENGTH, playerRep.getPlayerColor()) + " │                                                           │ " + Utils.fillWithSpaces(Integer.toString(playerRep.getPoints()), 3) + " │");
		CLIView.printLine(" ├──────────────────┴───┬───────────────────────┬───────────────────────────────┼─────┤");
		for (int i = 0; i < getNumOfLine(playerRep); i++) {
			printOwnPlayerLine(playerRep, i);
		}
		CLIView.printLine(" └──────────────────────┴───────────────────────┴───────────────────────────────┴─────┘");
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
		List<WeaponRep> playerWeapons = playerRep.getWeaponReps();
		List<PowerupCardRep> playerPowerups = playerRep.getPowerupCards();

		stringBuilder.append(" │ ");
		//Possible move
		if (lineIndex + 1 <= damageStatusRep.numOfMacroActions()) {
			stringBuilder.append(" [");
			stringBuilder.append(lineIndex + 1);
			stringBuilder.append("] ");
			stringBuilder.append(Utils.fillWithSpaces(damageStatusRep.getMacroActionName(lineIndex), 8));
			stringBuilder.append(Utils.fillWithSpaces(damageStatusRep.getMacroActionString(lineIndex), 7));
		} else
			stringBuilder.append(Utils.fillWithSpaces("", 16));

		stringBuilder.append("\t│ ");

		//Powerups
		if (lineIndex + 1 <= playerPowerups.size())
			stringBuilder.append(getPowerupRepString(playerPowerups.get(lineIndex)));
		else
			stringBuilder.append(Utils.fillWithSpaces(17));

		stringBuilder.append("\t│ ");

		//Weapons
		if (lineIndex + 1 <= playerWeapons.size())
			stringBuilder.append(getWeaponRepString(playerWeapons.get(lineIndex), true));
		else
			stringBuilder.append(Utils.fillWithSpaces(22));

		stringBuilder.append("\t│ ");

		//Ammo
		if (lineIndex + 1 <= AmmoType.values().length) {
			AmmoType ammoType = AmmoType.values()[lineIndex];
			for (int i = 0; i < playerRep.getAmmo(ammoType); i++) {
				stringBuilder.append(Color.getColoredString("●", ammoType.getCharacterColorType()));
			}
			for (int i = playerRep.getAmmo(ammoType); i < 4; i++) {
				stringBuilder.append(" ");
			}
		} else
			stringBuilder.append(Utils.fillWithSpaces(4));
		stringBuilder.append("│");
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
